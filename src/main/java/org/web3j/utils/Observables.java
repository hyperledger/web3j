package org.web3j.utils;

import java.math.BigInteger;

import rx.Observable;

/**
 * Observable utility functions.
 */
public class Observables {

    /**
     * Simple Observable implementation to emit a range of BigInteger values.
     *
     * @param startValue first value to emit in range
     * @param endValue final value to emit in range
     * @return Observable to omit this range of values
     */
    public static Observable<BigInteger> range(BigInteger startValue, BigInteger endValue) {
        if (startValue.compareTo(BigInteger.ZERO) == -1) {
            throw new IllegalArgumentException("Negative start index cannot be used");
        } else if (startValue.compareTo(endValue) > -1) {
            throw new IllegalArgumentException(
                    "Negative start index cannot be greater then end index");
        }

        return Observable.create(subscriber -> {
            for (BigInteger i = startValue;
                    i.compareTo(endValue) < 1
                            && !subscriber.isUnsubscribed();
                    i = i.add(BigInteger.ONE)) {
                subscriber.onNext(i);
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        });
    }
}
