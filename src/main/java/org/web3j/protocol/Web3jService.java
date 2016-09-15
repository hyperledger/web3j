package org.web3j.protocol;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.web3j.protocol.jsonrpc20.Request;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * Services API.
 */
public interface Web3jService {
    <T extends Response> T sendRequest(
            Request request, Class<T> type) throws IOException;

    <T extends Response> CompletableFuture<T> sendRequestAsync(
            Request request, Class<T> type);
}
