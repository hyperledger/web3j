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
package org.web3j.protocol.parity.methods.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Derivation used in following methods.
 *
 * <ol>
 *   <li>parity_deriveAddressHash
 *   <li>parity_deriveAddressIndex
 * </ol>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Derivation {

    private final Integer index;
    private final String hash;
    private final String type;

    private Derivation(final Integer index, final String hash, final String type) {
        this.index = index;
        this.hash = hash;
        this.type = type;
    }

    public static Derivation createDerivationHash(final String hash, final String type) {

        return new Derivation(null, hash, type);
    }

    public static Derivation createDerivationIndex(final Integer index, final String type) {

        return new Derivation(index, null, type);
    }

    public Integer getIndex() {
        return index;
    }

    public String getHash() {
        return hash;
    }

    public String getType() {
        return type;
    }
}
