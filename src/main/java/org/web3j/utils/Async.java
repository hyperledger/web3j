package org.web3j.utils;

import java.util.concurrent.*;

/**
 * Async task facilitation.
 */
public class Async {

    private static ExecutorService executorService = new ForkJoinPool();


    private static Executor executor;
    private static int additionalCpus = getCpuCount() - 1;

    static {
        if (additionalCpus > 1) {
            executor = new ForkJoinPool(additionalCpus);
        } else {

        }
    }

    public static <T> Future<T> run(Callable<T> callable) {
        return executorService.submit(callable);
    }

//    public static <T> Future<T> run(Callable callable) {
//        final Task<T> task = new Task<>(callable);
//        executor.execute(task);
//
//        return new FutureTask<>(new Callable<T>() {
//            @Override
//            public T call() throws Exception {
//                return task.get();
//            }
//        });
//    }

    private static int getCpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    private static final class ThreadPerTaskExecutor implements Executor {
        public void execute(Runnable r) { new Thread(r).start(); }
    }

    private static class Task<T> implements Runnable {
        private volatile T result;
        private final Callable<T> callable;
        private Thread caller;

        public Task(Callable<T> callable) {
            this.callable = callable;
        }

        public T get() {
            try {
                caller.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return result;
        }

        @Override
        public void run()  {
            try {
                caller = Thread.currentThread();
                result = callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
