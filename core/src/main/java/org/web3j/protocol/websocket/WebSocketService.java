package org.web3j.protocol.websocket;

import java.io.IOException;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.subjects.BehaviorSubject;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSubscribe;
import org.web3j.protocol.core.methods.response.EthUnsubscribe;
import org.web3j.protocol.websocket.events.Notification;

/**
 * Web socket service that allows to interact with JSON-RPC via WebSocket protocol.
 *
 * <p>Allows to interact with JSON-RPC either by sending individual requests or by
 * subscribing to a stream of notifications. To subscribe to a notification it first
 * sends a special JSON-RPC request that returns a unique subscription id. A subscription
 * id is used to identify events for a single notifications stream.
 *
 * <p>To unsubscribe from a stream of notifications it should send another JSON-RPC
 * request.
 */
public class WebSocketService implements Web3jService {

    private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);

    // Timeout for JSON-RPC requests
    static final long REQUEST_TIMEOUT = 60;

    // WebSocket client
    private final WebSocketClient webSocketClient;
    // Executor to schedule request timeouts
    private final ScheduledExecutorService executor;
    // Object mapper to map incoming JSON objects
    private final ObjectMapper objectMapper;

    // Map of a sent request id to objects necessary to process this request
    private Map<Long, WebSocketRequest<?>> requestForId = new ConcurrentHashMap<>();
    // Map of a subscription id to objects necessary to process incoming events
    private Map<String, WebSocketSubscription<?>> subscriptionForId = new ConcurrentHashMap<>();

    public WebSocketService(String serverUrl, boolean includeRawResponses) {
        this(new WebSocketClient(parseURI(serverUrl)), includeRawResponses);
    }

    public WebSocketService(WebSocketClient webSocketClient,
                     boolean includeRawResponses) {
        this(webSocketClient, Executors.newScheduledThreadPool(1), includeRawResponses);
    }

    WebSocketService(WebSocketClient webSocketClient,
                     ScheduledExecutorService executor,
                     boolean includeRawResponses) {
        this.webSocketClient = webSocketClient;
        this.executor = executor;
        this.objectMapper = ObjectMapperFactory.getObjectMapper(includeRawResponses);
    }

    /**
     * Connect to a WebSocket server.
     *
     * @throws ConnectException thrown if failed to connect to the server via WebSocket protocol
     */
    public void connect() throws ConnectException {
        try {
            boolean connected = webSocketClient.connectBlocking();
            if (!connected) {
                throw new ConnectException("Failed to connect to WebSocket");
            }
            webSocketClient.setListener(this::onMessage);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while connecting via WebSocket protocol");
        }
    }

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
        try {
            return sendAsync(request, responseType).get();
        } catch (InterruptedException e) {
            Thread.interrupted();
            throw new IOException("Interrupted WebSocket request", e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }

            throw new RuntimeException("Unexpected exception", e.getCause());
        }
    }

    @Override
    public <T extends Response> CompletableFuture<T> sendAsync(
            Request request,
            Class<T> responseType) {

        CompletableFuture<T> result = new CompletableFuture<>();
        long requestId = request.getId();
        requestForId.put(requestId, new WebSocketRequest<>(result, responseType));
        try {
            sendRequest(request, requestId);
        } catch (IOException e) {
            closeRequest(requestId, e);
        }

        return result;
    }

    private void sendRequest(Request request, long requestId) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(request);
        log.debug("Sending request: {}", payload);
        webSocketClient.send(payload);
        setRequestTimeout(requestId);
    }

    private void setRequestTimeout(long requestId) {
        executor.schedule(
                () -> closeRequest(
                    requestId,
                    new IOException(
                        String.format("Request with id %d timed out", requestId))),
                REQUEST_TIMEOUT,
                TimeUnit.SECONDS);
    }

    void closeRequest(long requestId, Exception e) {
        CompletableFuture result = requestForId.get(requestId).getCompletableFuture();
        requestForId.remove(requestId);
        result.completeExceptionally(e);
    }

    void onMessage(String messageStr) throws IOException {
        JsonNode replyJson = parseToTree(messageStr);

        if (isReply(replyJson)) {
            processRequestReply(messageStr, replyJson);
        } else if (isSubscriptionEvent(replyJson)) {
            processSubscriptionEvent(messageStr, replyJson);
        } else {
            throw new IOException("Unknown message type");
        }
    }

    private void processRequestReply(String replyStr, JsonNode replyJson) throws IOException {
        long replyId = getReplyId(replyJson);
        WebSocketRequest request = getAndRemoveRequest(replyId);
        try {
            Object reply = objectMapper.convertValue(replyJson, request.getResponseType());

            sendReplyToListener(request, reply);
        } catch (IllegalArgumentException e) {
            sendExceptionToListener(replyStr, request, e);
        }
    }

    private void sendReplyToListener(WebSocketRequest request, Object reply) {
        request.getCompletableFuture().complete(reply);
    }

    private void sendExceptionToListener(
            String replyStr,
            WebSocketRequest request,
            IllegalArgumentException e) {
        request.getCompletableFuture().completeExceptionally(
                new IOException(
                        String.format(
                                "Failed to parse '%s' as type %s",
                                replyStr,
                                request.getResponseType()),
                        e));
    }

    private void processSubscriptionEvent(String replyStr, JsonNode replyJson) {
        log.info("Processing event: {}", replyStr);
        String subscriptionId = extractSubscriptionId(replyJson);
        WebSocketSubscription subscription = subscriptionForId.get(subscriptionId);

        if (subscription != null) {
            sendEventToSubscriber(replyJson, subscription);
        } else {
            log.warn("No subscriber for WebSocket event with subscription id {}", subscriptionId);
        }
    }

    private String extractSubscriptionId(JsonNode replyJson) {
        return replyJson.get("params").get("subscription").asText();
    }

    private void sendEventToSubscriber(JsonNode replyJson, WebSocketSubscription subscription) {
        Object event = objectMapper.convertValue(replyJson, subscription.getResponseType());
        subscription.getSubject().onNext(event);
    }

    private boolean isReply(JsonNode replyJson) {
        return replyJson.has("id");
    }

    private boolean isSubscriptionEvent(JsonNode replyJson) {
        return replyJson.has("method");
    }

    private JsonNode parseToTree(String replyStr) throws IOException {
        try {
            return objectMapper.readTree(replyStr);
        } catch (IOException e) {
            throw new IOException("Failed to parse incoming WebSocket message", e);
        }
    }

    private WebSocketRequest getAndRemoveRequest(long id) throws IOException {
        if (!requestForId.containsKey(id)) {
            throw new IOException(
                    String.format("Received reply for unexpected request id: %d",
                            id));
        }
        WebSocketRequest request = requestForId.get(id);
        requestForId.remove(id);
        return request;
    }

    private long getReplyId(JsonNode replyJson) throws IOException {
        JsonNode idField = replyJson.get("id");
        if (idField == null) {
            throw new IOException("'id' field is missing in the reply");
        }

        if (!idField.isIntegralNumber()) {
            throw new IOException(
                    String.format("'id' expected to be long, but it is: '%s'",
                            idField.asText()));
        }

        return idField.longValue();
    }

    private static URI parseURI(String serverUrl) {
        try {
            return new URI(serverUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Failed to parse URL: '%s'", serverUrl), e);
        }
    }

    @Override
    public <T extends Notification<?>> Observable<T> subscribe(
            Request request,
            String unsubscribeMethod,
            Class<T> responseType) {
        // We can't use usual Observer since we can call "onError"
        // before first client is subscribed and we need to
        // preserve it
        BehaviorSubject<T> subject = BehaviorSubject.create();

        // We need to subscribe synchronously, since if we return
        // an Observable to a client before we got a reply
        // a client can unsubscribe before we know a subscription
        // id and this can cause a race condition
        subscribeToEventsStream(request, subject, responseType);

        return subject
                .doOnUnsubscribe(() -> closeSubscription(subject, unsubscribeMethod));

    }

    private <T extends Notification<?>> void closeSubscription(
            BehaviorSubject<T> subject, String unsubscribeMethod) {
        subject.onCompleted();
        String subscriptionId = getSubscriptionId(subject);
        if (subscriptionId != null) {
            subscriptionForId.remove(subscriptionId);
            unsubscribeFromEventsStream(subscriptionId, unsubscribeMethod);
        } else {
            log.warn("Trying to unsubscribe from a non-existing subscription. Race condition?");
        }
    }

    private <T extends Notification<?>> void subscribeToEventsStream(
            Request request,
            BehaviorSubject<T> subject, Class<T> responseType) {

        try {
            processSubscriptionResponse(request, subject, responseType);
        } catch (IOException e) {
            log.error("Failed to subscribe to RPC events with request id {}",
                    request.getId());
            subject.onError(e);
        }
    }

    private <T extends Notification<?>> void processSubscriptionResponse(
            Request request, BehaviorSubject<T> subject, Class<T> responseType) throws IOException {
        EthSubscribe subscriptionReply = send(request, EthSubscribe.class);
        if (subscriptionReply.hasError()) {
            reportSubscriptionError(subject, subscriptionReply);
        } else {
            establishSubscription(subject, responseType, subscriptionReply);
        }
    }

    private <T extends Notification<?>> void establishSubscription(
            BehaviorSubject<T> subject, Class<T> responseType, EthSubscribe subscriptionReply) {
        log.info("Subscribed to RPC events with id {}",
                subscriptionReply.getSubscriptionId());
        subscriptionForId.put(
                subscriptionReply.getSubscriptionId(),
                new WebSocketSubscription<>(subject, responseType));
    }

    private <T extends Notification<?>> String getSubscriptionId(BehaviorSubject<T> subject) {
        return subscriptionForId.entrySet().stream()
                .filter(entry -> entry.getValue().getSubject() == subject)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private <T extends Notification<?>> void reportSubscriptionError(
            BehaviorSubject<T> subject, EthSubscribe subscriptionReply) {
        Response.Error error = subscriptionReply.getError();
        log.error("Subscription request returned error: {}", error.getMessage());
        subject.onError(
                new IOException(String.format(
                        "Subscription request failed with error: %s",
                        error.getMessage()
                ))
        );
    }

    private void unsubscribeFromEventsStream(String subscriptionId, String unsubscribeMethod) {
        sendAsync(unsubscribeRequest(subscriptionId, unsubscribeMethod), EthUnsubscribe.class)
                .thenAccept(ethUnsubscribe -> {
                    log.debug("Successfully unsubscribed from subscription with id {}",
                            subscriptionId);
                })
                .exceptionally(throwable -> {
                    log.error("Failed to unsubscribe from subscription with id {}", subscriptionId);
                    return null;
                });
    }

    private Request<String, EthUnsubscribe> unsubscribeRequest(
            String subscriptionId, String unsubscribeMethod) {
        return new Request<>(
                        unsubscribeMethod,
                        Arrays.asList(subscriptionId),
                        this,
                        EthUnsubscribe.class);
    }

    @Override
    public void close() {
        webSocketClient.close();
        executor.shutdown();
    }

    // Method visible for unit-tests
    boolean isWaitingForReply(long requestId) {
        return requestForId.containsKey(requestId);
    }
}

