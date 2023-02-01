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
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.tx.exceptions.ContractCallException;

public class DynamicEIP1559GasProvider extends StaticEIP1559GasProvider {

    private BigInteger extraGas;
    private BigInteger maxFeePerGasCap;
    private BigInteger gasLimitCap;
    private Web3j web3j;

    public DynamicEIP1559GasProvider(
            long chainId,
            BigInteger fallbackMaxFeePerGas,
            BigInteger fallbackMaxPriorityFeePerGas,
            BigInteger fallbackGasLimit,
            BigInteger extraGas,
            BigInteger maxFeePerGasCap,
            BigInteger gasLimitCap,
            Web3j web3j) {
        super(chainId, fallbackMaxFeePerGas, fallbackMaxPriorityFeePerGas, fallbackGasLimit);
        this.extraGas = extraGas;
        this.maxFeePerGasCap = maxFeePerGasCap;
        this.gasLimitCap = gasLimitCap;
        this.web3j = web3j;
    }

    public DynamicEIP1559GasProvider(
            long chainId,
            BigInteger fallbackMaxFeePerGas,
            BigInteger fallbackMaxPriorityFeePerGas,
            BigInteger fallbackGasLimit,
            Web3j web3j) {
        this(
                chainId,
                fallbackMaxFeePerGas,
                fallbackMaxPriorityFeePerGas,
                fallbackGasLimit,
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                web3j);
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
    public EIP1559GasFeeData getGasFeeData(
            String fromAddress,
            String contractAddress,
            String data,
            BigInteger weiValue,
            String contractFunc) {
        BigInteger baseFee = null;
        try {
            baseFee =
                    web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                            .send()
                            .getBlock()
                            .getBaseFeePerGas();
        } catch (Exception e) {
        }
        BigInteger maxPriorityFeePerGas = null;
        try {
            maxPriorityFeePerGas = web3j.ethMaxPriorityFeePerGas().send().getMaxPriorityFeePerGas();
        } catch (Exception e) {
        }
        if (maxPriorityFeePerGas == null)
            maxPriorityFeePerGas = getMaxPriorityFeePerGas(contractFunc);
        BigInteger maxFeePerGas;
        if (baseFee != null) {
            maxFeePerGas = baseFee.multiply(BigInteger.valueOf(2)).add(maxPriorityFeePerGas);
            if (maxFeePerGasCap.compareTo(BigInteger.ZERO) > 0)
                maxFeePerGas = maxFeePerGas.min(maxFeePerGasCap);
        } else {
            maxFeePerGas = getMaxFeePerGas(contractFunc);
        }
        return new EIP1559GasFeeData(maxFeePerGas, maxPriorityFeePerGas);
    }
}
