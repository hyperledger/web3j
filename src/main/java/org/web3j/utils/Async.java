package org.web3j.utils;


import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * Async task facilitation.
 */
public class Async {

    public static <T> CompletableFuture<T> run(Callable<T> callable) {
        CompletableFuture<T> result = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            // we need to explicityly catch any exceptions,
            // otherwise they will be silently discarded
            try {
                result.complete(callable.call());
            } catch (Throwable e) {
                result.completeExceptionally(e);
            }
        });
        return result;
    }
}
