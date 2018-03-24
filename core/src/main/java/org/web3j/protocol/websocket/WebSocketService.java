package org.web3j.protocol.websocket;

import java.io.IOException;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

public class WebSocketService implements Web3jService {

    private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);

    static final long REQUEST_TIMEOUT = 60;

    private final WebSocketClient webSocketClient;
    private final ScheduledExecutorService executor;
    private final ObjectMapper objectMapper;

    private Map<Long, WebSocketRequest<?>> requestForId = new HashMap<>();

    public WebSocketService(String serverUrl, boolean includeRawResponses) {
        this.webSocketClient = new WebSocketClient(parseURI(serverUrl), this);
        this.executor = Executors.newScheduledThreadPool(1);
        this.objectMapper = ObjectMapperFactory.getObjectMapper(includeRawResponses);
    }

    WebSocketService(WebSocketClient webSocketClient,
                     ScheduledExecutorService executor,
                     boolean includeRawResponses) {
        this.webSocketClient = webSocketClient;
        this.executor = executor;
        this.objectMapper = ObjectMapperFactory.getObjectMapper(includeRawResponses);
    }

    public void connect() throws ConnectException {
        try {
            boolean connected = webSocketClient.connectBlocking();
            if (!connected) {
                throw new ConnectException("Failed to connect to WebSocket");
            }
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

            throw new RuntimeException("Unexpected exception", e);
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
            String payload = objectMapper.writeValueAsString(request);
            webSocketClient.send(payload);
            setTimeout(requestId);
        } catch (IOException e) {
            closeRequest(requestId, e);
        }

        return result;
    }

    private void setTimeout(long requestId) {
        executor.schedule(
                () -> closeRequest(
                    requestId,
                    new IOException(
                        String.format("Request with id %d timed out", requestId))),
                REQUEST_TIMEOUT,
                TimeUnit.SECONDS);
    }

    private void closeRequest(long requestId, Exception e) {
        CompletableFuture result = requestForId.get(requestId).getCompletableFuture();
        requestForId.remove(requestId);
        result.completeExceptionally(e);
    }

    boolean isWaitingForReply(long requestId) {
        return requestForId.containsKey(requestId);
    }

    void onReply(String replyStr) throws IOException {
        JsonNode replyJson = parseToTree(replyStr);

        WebSocketRequest request = getAndRemoveRequest(replyJson);
        Class<?> responseType = request.getResponseType();
        CompletableFuture<Object> future = request.getCompletableFuture();

        try {
            Object reply = objectMapper.convertValue(replyJson, responseType);
            future.complete(reply);
        } catch (IllegalArgumentException e) {
            future.completeExceptionally(
                    new IOException(
                            String.format(
                                    "Failed to parse '%s' as type %s",
                                    replyStr,
                                    responseType),
                            e));
        }
    }

    private JsonNode parseToTree(String replyStr) throws IOException {
        try {
            return objectMapper.readTree(replyStr);
        } catch (IOException e) {
            throw new IOException("Failed to parse incoming WebSocket message", e);
        }
    }

    private WebSocketRequest getAndRemoveRequest(JsonNode replyJson) throws IOException {
        long id = getReplyId(replyJson);
        if (!requestForId.containsKey(id)) {
            throw new IOException(String.format(
                    "Received reply for unexpected request id: %d",
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
            throw new IOException(String.format(
                    "'id' expected to be long, but it is: '%s'",
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

    public void close() {
        webSocketClient.close();
        executor.shutdown();
    }
}

