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
package org.web3j.contracts.token;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.Flowable;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * Describes the Ethereum "Basic" subset of the ERC-20 token standard.
 *
 * <p>Implementations should provide the concrete <code>TransferEventResponse</code> from their
 * token as the generic type "T".
 *
 * @see <a href="https://github.com/ethereum/EIPs/issues/179">ERC: Simpler Token Standard #179</a>
 * @see <a
 *     href="https://github.com/OpenZeppelin/zeppelin-solidity/blob/master/contracts/token/ERC20Basic.sol">OpenZeppelin's
 *     zeppelin-solidity reference implementation</a>
 */
@SuppressWarnings("unused")
public interface ERC20BasicInterface<T> {

    RemoteCall<BigInteger> totalSupply();

    RemoteCall<BigInteger> balanceOf(String who);

    RemoteCall<TransactionReceipt> transfer(String to, BigInteger value);

    List<T> getTransferEvents(TransactionReceipt transactionReceipt);

    Flowable<T> transferEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock);
}
