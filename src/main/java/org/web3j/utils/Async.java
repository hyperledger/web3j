package org.web3j.utils;

import java.util.concurrent.*;

/**
 * Async task facilitation.
 */
public class Async {

    private static ExecutorService executorService;

    static {
        executorService = Executors.newFixedThreadPool(getCpuCount());
    }

    public static <T> Future<T> run(Callable<T> callable) {
        return executorService.submit(callable);
    }

    private static int getCpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static ExecutorService defaultExecutorService() {
        return executorService;
    }
}
