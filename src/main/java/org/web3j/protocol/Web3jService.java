package org.web3j.protocol;

import java.io.IOException;
import java.util.concurrent.Future;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

/**
 * Services API.
 */
public interface Web3jService {
    <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException;

    <T extends Response> Future<T> sendAsync(
            Request request, Class<T> responseType);
}
