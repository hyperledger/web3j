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
package org.web3j.protocol.besu;

import java.util.concurrent.ScheduledExecutorService;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

import org.web3j.protocol.besu.filters.PrivateLogFilter;
import org.web3j.protocol.core.methods.response.Log;

public class JsonRpc2_0BesuRx {

    private final Besu besu;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0BesuRx(Besu besu, ScheduledExecutorService scheduledExecutorService) {
        this.besu = besu;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public Flowable<Log> privLogFlowable(
            String privacyGroupId,
            org.web3j.protocol.core.methods.request.EthFilter ethFilter,
            long pollingInterval) {
        return Flowable.create(
                subscriber -> {
                    PrivateLogFilter logFilter =
                            new PrivateLogFilter(
                                    besu, subscriber::onNext, privacyGroupId, ethFilter);

                    run(logFilter, subscriber, pollingInterval);
                },
                BackpressureStrategy.BUFFER);
    }

    private <T> void run(
            org.web3j.protocol.core.filters.Filter<T> filter,
            FlowableEmitter<? super T> emitter,
            long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        emitter.setCancellable(filter::cancel);
    }
}
