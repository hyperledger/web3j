package org.web3j.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Async task facilitation.
 */
public class Async {

    private static final Executor executor = Executors.newCachedThreadPool();

    public static <T> CompletableFuture<T> run(Callable<T> callable) {
        CompletableFuture<T> result = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            // we need to explicitly catch any exceptions,
            // otherwise they will be silently discarded
            try {
                result.complete(callable.call());
            } catch (Throwable e) {
                result.completeExceptionally(e);
            }
        }, executor);
        return result;
    }

    private static int getCpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static ScheduledExecutorService defaultExecutorService() {
        return Executors.newScheduledThreadPool(getCpuCount());
    }
}
