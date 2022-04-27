/*
 * Copyright 2022 Web3 Labs Ltd.
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
package org.web3j.model;

public enum StateMutability {
    PURE("pure"),
    VIEW("view"),
    NON_PAYABLE("nonpayable"),
    PAYABLE("payable");

    private final String name;

    StateMutability(String state) {
        this.name = state;
    }

    public static StateMutability findByName(String name) {
        for (StateMutability state : values()) {
            if (state.name().equals(name)) {
                return state;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public boolean isPayable() {
        return this == StateMutability.PAYABLE;
    }

    public boolean isPure() {
        return this == StateMutability.PURE;
    }

    public boolean isView() {
        return this == StateMutability.VIEW;
    }

    public static boolean isPayable(String state) {
        return PAYABLE.name.equals(state);
    }

    public static boolean isPure(String state) {
        return PURE.name.equals(state);
    }

    public static boolean isView(String state) {
        return VIEW.name.equals(state);
    }
}
