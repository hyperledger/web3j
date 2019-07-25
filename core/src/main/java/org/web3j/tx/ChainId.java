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
package org.web3j.tx;

/**
 * Ethereum chain ids as per <a
 * href="https://github.com/ethereum/EIPs/blob/master/EIPS/eip-155.md">EIP-155</a>.
 */
@Deprecated
public class ChainId {
    public static final byte NONE = -1;
    public static final byte MAINNET = 1;
    public static final byte EXPANSE_MAINNET = 2;
    public static final byte ROPSTEN = 3;
    public static final byte RINKEBY = 4;
    public static final byte ROOTSTOCK_MAINNET = 30;
    public static final byte ROOTSTOCK_TESTNET = 31;
    public static final byte KOVAN = 42;
    public static final byte ETHEREUM_CLASSIC_MAINNET = 61;
    public static final byte ETHEREUM_CLASSIC_TESTNET = 62;
}
