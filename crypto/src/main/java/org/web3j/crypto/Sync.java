package org.web3j.crypto;

import java.util.function.Consumer;

class Sync {
    public static <T, U, R> R call(BiAsync<T, U, R> async, T t, U u) {
        Holder<R> complete = new Holder<>();  
        Holder<Throwable> error = new Holder<>();  
        async.apply(t, u, complete, error);
        return result(complete, error);
    }

    public static <T, R> R call(Async<T,R> async, T t) {
        Holder<R> complete = new Holder<>();  
        Holder<Throwable> error = new Holder<>();  
        async.apply(t, complete, error);
        return result(complete, error);
    }

    private static <R> R result(Holder<R> complete, Holder<Throwable> error) {
        if (error.result != null) {
            throw new RuntimeException(error.result);
        } else {
            return complete.result;
        }
    }

    public static interface Async<T, R> {
        public void apply(T t, Consumer<R> complete, Consumer<Throwable> error);
    }

    public static interface BiAsync<T, U, R> {
        public void apply(T t, U u, Consumer<R> complete, Consumer<Throwable> error);
    }

    static class Holder<T> implements Consumer<T> {
        T result;

        @Override
        public void accept(T result) {
            this.result = result;
        }
    }

}
