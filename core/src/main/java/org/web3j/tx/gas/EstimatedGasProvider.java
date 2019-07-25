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
package org.web3j.tx.gas;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;

import java.io.IOException;
import java.math.BigInteger;

public class EstimatedGasProvider implements ContractGasProvider {

    private final Web3j web3j;

    public EstimatedGasProvider(Web3j web3j) {
        this.web3j = web3j;
    }

    @Override
    public BigInteger getGasPrice(String contractFunc) throws IOException{
        return getGasPrice();
    }

    @Override
    public BigInteger getGasPrice() throws IOException {
        return web3j.ethGasPrice().send().getGasPrice();
    }

    @Override
    public BigInteger getGasLimit(
            String fromAddress,
            BigInteger gasPrice,
            String contractAddress,
            BigInteger weiValue,
            String data
    ) throws IOException {
        Transaction estimatedTransaction = new Transaction(
                fromAddress,
                BigInteger.ZERO,
                gasPrice,
                BigInteger.ZERO,
                contractAddress,
                weiValue,
                data);
        return web3j.ethEstimateGas(estimatedTransaction).send().getAmountUsed();
    }

    @Override
    public BigInteger getGasLimit(String contractFunc) {
        return null;
    }

    @Override
    public BigInteger getGasLimit() {
        return null;
    }
}
