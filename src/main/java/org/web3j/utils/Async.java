package org.web3j.utils;

import java.util.concurrent.*;

/**
 * Async task facilitation.
 */
public class Async {

    private static ExecutorService executorService;

    private static int additionalCpus = getCpuCount() - 1;

    static {
        if (additionalCpus > 1) {
            // Fork join pool is preferable for Java 7
            // CompletableFutures for Java 8
            executorService = Executors.newFixedThreadPool(additionalCpus);
        } else {
            throw new RuntimeException("More then one core is required for operation");
        }
    }

    public static <T> Future<T> run(Callable<T> callable) {
        return executorService.submit(callable);
    }

    private static int getCpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }
}
