/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.web3j.protocol.admin.methods.response;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java8.util.function.Function;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.Transaction;

/** txpool_content */
public final class TxPoolContent extends Response<TxPoolContent.TxPoolContentResult> {
    public static class TxPoolContentResult {

        private Map<String, Map<BigInteger, Transaction>> pending;
        private Map<String, Map<BigInteger, Transaction>> queued;

        public TxPoolContentResult() {}

        public TxPoolContentResult(
                Map<String, Map<BigInteger, Transaction>> pending,
                Map<String, Map<BigInteger, Transaction>> queued) {
            this.pending = immutableCopy(pending, val -> immutableCopy(val, t -> t));
            this.queued = immutableCopy(queued, val -> immutableCopy(val, t -> t));
        }

        public Map<String, Map<BigInteger, Transaction>> getPending() {
            return pending;
        }

        public Map<String, Map<BigInteger, Transaction>> getQueued() {
            return queued;
        }

        public List<Transaction> getPendingTransactions() {
            return StreamSupport.stream(pending.values())
                    .map(Map::values)
                    .flatMap(StreamSupport::stream)
                    .collect(Collectors.toList());
        }

        public List<Transaction> getQueuedTransactions() {
            return StreamSupport.stream(queued.values())
                    .map(Map::values)
                    .flatMap(StreamSupport::stream)
                    .collect(Collectors.toList());
        }

        private static <K, V> Map<K, V> immutableCopy(Map<K, V> map, Function<V, V> valueMapper) {
            Map<K, V> result = new HashMap<>();
            for (Map.Entry<K, V> entry : map.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                result.put(key, valueMapper.apply(value));
            }
            return Collections.unmodifiableMap(result);
        }
    }
}
