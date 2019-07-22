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

import org.web3j.utils.Numeric;

/**
 * Record type interfaces supported by resolvers as per <a
 * href="https://github.com/ethereum/EIPs/blob/master/EIPS/eip-137.md#resolver-specification">EIP-137</a>
 */
public class RecordTypes {

    public static final byte[] ADDR = Numeric.hexStringToByteArray("0x3b3b57de");
    public static final byte[] NAME = Numeric.hexStringToByteArray("0x691f3431");
    public static final byte[] ABI = Numeric.hexStringToByteArray("0x2203ab56");
    public static final byte[] PUB_KEY = Numeric.hexStringToByteArray("0xc8690233");
}
