package org.web3j.protocol.core;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import org.web3j.utils.Async;

public interface AsyncCallable<T> extends Callable<T> {

    /**
     * Perform request asynchronously with a future.
     *
     * @return a future containing our function
     */
    default CompletableFuture<T> callAsync() {
        return Async.run(this);
    }
}
