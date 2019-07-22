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
package org.web3j.protocol.core;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.request.Transaction;

/** Common values used by integration tests. */
public interface IntegrationTestConfig {

    String validBlockHash();

    BigInteger validBlock();

    BigInteger validBlockTransactionCount();

    BigInteger validBlockUncleCount();

    String validAccount();

    String validContractAddress();

    String validContractAddressPositionZero();

    String validContractCode();

    Transaction buildTransaction();

    String validTransactionHash();

    String validUncleBlockHash();

    BigInteger validUncleBlock();

    String encodedEvent();
}
