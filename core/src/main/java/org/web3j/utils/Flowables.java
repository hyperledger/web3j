/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.utils;

import java.math.BigInteger;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/** Flowable utility functions. */
public class Flowables {

    public static Flowable<BigInteger> range(
            final BigInteger startValue, final BigInteger endValue) {
        return range(startValue, endValue, true);
    }

    /**
     * Simple {@link Flowable} implementation to emit a range of BigInteger values.
     *
     * @param startValue first value to emit in range
     * @param endValue final value to emit in range
     * @param ascending direction to iterate through range
     * @return a {@link Flowable} instance to emit this range of values
     */
    public static Flowable<BigInteger> range(
            final BigInteger startValue, final BigInteger endValue, final boolean ascending) {
        if (startValue.compareTo(BigInteger.ZERO) == -1) {
            throw new IllegalArgumentException("Negative start index cannot be used");
        } else if (startValue.compareTo(endValue) > 0) {
            throw new IllegalArgumentException(
                    "Negative start index cannot be greater then end index");
        }

        if (ascending) {
            return Flowable.create(
                    subscriber -> {
                        for (BigInteger i = startValue;
                                i.compareTo(endValue) < 1 && !subscriber.isCancelled();
                                i = i.add(BigInteger.ONE)) {
                            subscriber.onNext(i);
                        }

                        if (!subscriber.isCancelled()) {
                            subscriber.onComplete();
                        }
                    },
                    BackpressureStrategy.BUFFER);
        } else {
            return Flowable.create(
                    subscriber -> {
                        for (BigInteger i = endValue;
                                i.compareTo(startValue) > -1 && !subscriber.isCancelled();
                                i = i.subtract(BigInteger.ONE)) {
                            subscriber.onNext(i);
                        }

                        if (!subscriber.isCancelled()) {
                            subscriber.onComplete();
                        }
                    },
                    BackpressureStrategy.BUFFER);
        }
    }
}
