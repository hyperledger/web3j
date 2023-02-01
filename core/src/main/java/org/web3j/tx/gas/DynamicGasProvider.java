/*
 * Copyright 2023 Web3 Labs Ltd.
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

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.tx.exceptions.ContractCallException;

public class DynamicGasProvider extends StaticGasProvider {

    private BigInteger extraGas;
    private BigInteger gasLimitCap;
    private Web3j web3j;

    public DynamicGasProvider(
            BigInteger fallbackGasPrice,
            BigInteger fallbackGasLimit,
            BigInteger extraGas,
            BigInteger gasLimitCap,
            Web3j web3j) {
        super(fallbackGasPrice, fallbackGasLimit);
        this.extraGas = extraGas;
        this.gasLimitCap = gasLimitCap;
        this.web3j = web3j;
    }

    public DynamicGasProvider(
            BigInteger fallbackGasPrice, BigInteger fallbackGasLimit, Web3j web3j) {
        this(fallbackGasPrice, fallbackGasLimit, BigInteger.ZERO, BigInteger.ZERO, web3j);
    }

    @Override
    public BigInteger getGasLimit(
            String fromAddress,
            String contractAddress,
            String data,
            BigInteger weiValue,
            String contractFunc) {
        Transaction tx =
                Transaction.createEthCallTransaction(fromAddress, contractAddress, data, weiValue);
        try {
            EthEstimateGas estimateGasResponse = web3j.ethEstimateGas(tx).send();
            if (estimateGasResponse.hasError()) {
                throw new ContractCallException(estimateGasResponse.getError().getMessage());
            } else {
                BigInteger gasUsed = estimateGasResponse.getAmountUsed().add(extraGas);
                return gasLimitCap.compareTo(BigInteger.ZERO) > 0
                        ? gasUsed.min(gasLimitCap)
                        : gasUsed;
            }
        } catch (IOException e) {
            return getGasLimit(contractFunc);
        }
    }

    @Override
    public BigInteger getGasPrice(
            String fromAddress,
            String contractAddress,
            String data,
            BigInteger weiValue,
            String contractFunc) {
        try {
            return web3j.ethGasPrice().send().getGasPrice();
        } catch (Exception e) {
            return getGasPrice(contractFunc);
        }
    }
}
