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
package org.web3j.protocol.websocket.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Generic class for a notification param. Contains a subscription id and a data item.
 *
 * @param <T> type of data return by a particular subscription
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationParams<T> {
    private T result;
    private String subsciption;

    public T getResult() {
        return result;
    }

    public String getSubsciption() {
        return subsciption;
    }
}
