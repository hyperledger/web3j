package org.web3j.protocol.core;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import io.reactivex.Flowable;
import io.reactivex.Single;

import org.web3j.utils.Async;

/**
 * A common type for wrapping remote requests.
 *
 * @param <T> Our return type.
 */
public class RemoteCall<T> {

    private Callable<T> callable;

    public RemoteCall(Callable<T> callable) {
        this.callable = callable;
    }

    /**
     * Perform request synchronously.
     *
     * @return result of enclosed function
     * @throws Exception if the function throws an exception
     */
    public T send() throws Exception {
        return callable.call();
    }

    /**
     * Perform request asynchronously with a future.
     *
     * @return a future containing our function
     */
    public CompletableFuture<T> sendAsync() {
        return Async.run(this::send);
    }

    /**
     * Provide an observable to emit result from our function.
     *
     * @return an observable
     */
    public Flowable<T> observable() {
        return single().toFlowable();
    }

    public Single<T> single() {
        return Single.fromCallable(this::send);
    }
}
