package org.web3j.crypto;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Async {

    public static <R> R sync(CompletableFuture<R> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) { 
            throw new RuntimeException(e);
        }
    }
}

