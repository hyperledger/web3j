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
 * The Ethereum ERC-20 token standard.
 *
 * <p>Implementations should provide the concrete <code>ApprovalEventResponse</code> and <code>
 * TransferEventResponse</code> from their token as the generic types "R" amd "T".
 *
 * @see <a
 *     href="https://github.com/ethereum/EIPs/blob/master/EIPS/eip-20-token-standard.md">EIPs/EIPS/eip-20-token-standard.md</a>
 * @see <a href="https://github.com/ethereum/EIPs/issues/20">ERC: Token standard #20</a>
 */
@SuppressWarnings("unused")
public interface ERC20Interface<R, T> extends ERC20BasicInterface<T> {

    RemoteCall<BigInteger> allowance(String owner, String spender);

    RemoteCall<TransactionReceipt> approve(String spender, BigInteger value);

    RemoteCall<TransactionReceipt> transferFrom(String from, String to, BigInteger value);

    List<R> getApprovalEvents(TransactionReceipt transactionReceipt);

    Flowable<R> approvalEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock);
}
