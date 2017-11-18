/*
 * Copyright (c) 2017. DataVolo, Inc.  All Rights Reserved.
 *
 * Proprietary and confidential.
 *
 * Unauthorized copying or use of this file or any of its contents, via any medium is strictly
 * prohibited.
 *
 * Unauthorized use of the outputs of compiling or otherwise processing this file in a
 * programmable, repeatable way is strictly prohibited.
 *
 * Authorizations, when granted, must be via a valid software license agreement with DataVolo, Inc.
 *
 * For inquires please contact legal@datavolo.com
 *
 */

package org.web3j.contracts.token;

import java.math.BigInteger;
import java.util.List;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import rx.Observable;

/**
 * Describes the Ethereum "Basic" subset of the ERC-20 token standard.
 * <p>
 *     Implementations should provide the concrete <code>TransferEventResponse</code>
 *     from their token as the generic type "T".
 * </p>
 *
 * @see <a href="https://github.com/ethereum/EIPs/issues/179">ERC: Simpler Token Standard #179</a>
 * @see <a href="https://github.com/OpenZeppelin/zeppelin-solidity/blob/master/contracts/token/ERC20Basic.sol">OpenZeppelin's zeppelin-solidity reference implementation</a>
 */
@SuppressWarnings("unused")
public interface ERC20BasicInterface<T> {

    RemoteCall<BigInteger> totalSupply();

    RemoteCall<BigInteger> balanceOf(String who);

    RemoteCall<TransactionReceipt> transfer(String to, BigInteger value);
    
    List<T> getTransferEvents(TransactionReceipt transactionReceipt);

    Observable<T> transferEventObservable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock);

}
