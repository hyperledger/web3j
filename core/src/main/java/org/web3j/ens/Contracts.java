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
package org.web3j.ens;

import org.web3j.tx.ChainIdLong;

/** ENS registry contract addresses. */
public class Contracts {

    public static final String MAINNET = "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e";
    public static final String ROPSTEN = "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e";
    public static final String RINKEBY = "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e";
    public static final String GOERLI = "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e";
    public static final String SEPOLIA = "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e";
    public static final String LINEA = "0x50130b669B28C339991d8676FA73CF122a121267";
    public static final String LINEA_SEPOLIA = "0x5B2636F0f2137B4aE722C01dd5122D7d3e9541f7";

    public static String resolveRegistryContract(String chainId) {
        final Long chainIdLong = Long.parseLong(chainId);
        if (chainIdLong.equals(ChainIdLong.MAINNET)) {
            return MAINNET;
        } else if (chainIdLong.equals(ChainIdLong.ROPSTEN)) {
            return ROPSTEN;
        } else if (chainIdLong.equals(ChainIdLong.RINKEBY)) {
            return RINKEBY;
        } else if (chainIdLong.equals(ChainIdLong.GOERLI)) {
            return GOERLI;
        } else if (chainIdLong.equals(ChainIdLong.SEPOLIA)) {
            return SEPOLIA;
        } else if (chainIdLong.equals(ChainIdLong.LINEA)) {
            return LINEA;
        } else if (chainIdLong.equals(ChainIdLong.LINEA_SEPOLIA)) {
            return LINEA_SEPOLIA;
        } else {
            throw new EnsResolutionException(
                    "Unable to resolve ENS registry contract for network id: " + chainId);
        }
    }
}
