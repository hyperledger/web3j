package org.web3j.protocol;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Async;

/**
 * Base service implementation
 */
public abstract class Service implements Web3jService{

    protected final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    @Override
    public <T extends Response> Future<T> sendAsync(
            final Request jsonRpc20Request, final Class<T> responseType) {
        return Async.run(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return Service.this.send(jsonRpc20Request, responseType);
            }
        });
    }
}
