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
package org.web3j.tx.gas;

import java.math.BigInteger;

public class StaticEIP1559GasProvider implements ContractEIP1559GasProvider {
    private long chainId;
    private BigInteger maxFeePerGas;
    private BigInteger maxPriorityFeePerGas;
    private BigInteger gasLimit;

    public StaticEIP1559GasProvider(
            long chainId,
            BigInteger maxFeePerGas,
            BigInteger maxPriorityFeePerGas,
            BigInteger gasLimit) {
        this.chainId = chainId;
        this.maxFeePerGas = maxFeePerGas;
        this.maxPriorityFeePerGas = maxPriorityFeePerGas;
        this.gasLimit = gasLimit;
    }

    @Override
    public BigInteger getGasPrice(String contractFunc) {
        return maxFeePerGas;
    }

    @Override
    public BigInteger getGasPrice() {
        return maxFeePerGas;
    }

    @Override
    public BigInteger getGasLimit(String contractFunc) {
        return gasLimit;
    }

    @Override
    public BigInteger getGasLimit() {
        return gasLimit;
    }

    @Override
    public boolean isEIP1559Enabled() {
        return true;
    }

    @Override
    public long getChainId() {
        return chainId;
    }

    @Override
    public BigInteger getMaxFeePerGas(String contractFunc) {
        return maxFeePerGas;
    }

    @Override
    public BigInteger getMaxPriorityFeePerGas(String contractFunc) {
        return maxPriorityFeePerGas;
    }
}
