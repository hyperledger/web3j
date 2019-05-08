package org.web3j.crypto;

public class Pair {
    private final Object first;
    private final Object second;

    public Object getFirst() {
        return first;
    }

    public Object getSecond() {
        return second;
    }

    public Pair(Object first, Object second) {
        this.first = first;
        this.second = second;
    }
}
