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
package org.web3j.protocol.core;

import java.math.BigInteger;

/**
 * Wrapper for parameter that takes either a block number or block name as input
 *
 * <p>See the <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter">
 * specification</a> for further information.
 */
public interface DefaultBlockParameter {
    static DefaultBlockParameter valueOf(BigInteger blockNumber) {
        if (BigInteger.ZERO.compareTo(blockNumber) >= 0) {
            blockNumber = BigInteger.ZERO;
        }
        return new DefaultBlockParameterNumber(blockNumber);
    }

    static DefaultBlockParameter valueOf(final String blockName) {
        return DefaultBlockParameterName.fromString(blockName);
    }

    String getValue();
}
