/*
 * Copyright 2019 Web3 Labs LTD.
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
package org.web3j.ens;

import org.web3j.tx.ChainIdLong;

/** ENS registry contract addresses. */
public class Contracts {

    public static final String MAINNET = "0x314159265dd8dbb310642f98f50c066173c1259b";
    public static final String ROPSTEN = "0x112234455c3a32fd11230c42e7bccd4a84e02010";
    public static final String RINKEBY = "0xe7410170f87102df0055eb195163a03b7f2bff4a";

    public static String resolveRegistryContract(String chainId) {
        final Long chainIdLong = Long.parseLong(chainId);
        if (chainIdLong.equals(ChainIdLong.MAINNET)) {
            return MAINNET;
        } else if (chainIdLong.equals(ChainIdLong.ROPSTEN)) {
            return ROPSTEN;
        } else if (chainIdLong.equals(ChainIdLong.RINKEBY)) {
            return RINKEBY;
        } else {
            throw new EnsResolutionException(
                    "Unable to resolve ENS registry contract for network id: " + chainId);
        }
    }
}
