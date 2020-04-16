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
import java.util.Arrays;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Uint;
import org.web3j.protocol.core.methods.request.Transaction;

/** Mordon Testnet Configuration. */
public class TestnetConfig implements IntegrationTestConfig {

    @Override
    public String validBlockHash() {
        https: // testnet.etherscan.io/block/1627453
        return "0xd67e59db999c3bd78bd4c2ba54689dba0c372ebcad09c8b9677970f37d64ca46";
    }

    @Override
    public BigInteger validBlock() {
        // https://testnet.etherscan.io/block/71032
        return BigInteger.valueOf(71032);
    }

    @Override
    public BigInteger validBlockTransactionCount() {
        return BigInteger.valueOf(3);
    }

    @Override
    public BigInteger validBlockUncleCount() {
        return BigInteger.ZERO;
    }

    @Override
    public String validAccount() {
        // https://testnet.etherscan.io/address/0xCB10FBad79F5e602699fFf2Bb4919Fbd87AbC8CC
        return "0xcb10fbad79f5e602699fff2bb4919fbd87abc8cc";
    }

    @Override
    public String validContractAddress() {
        // Deployed fibonacci example
        return "0x3c05b2564139fb55820b18b72e94b2178eaace7d";
    }

    @Override
    public String validContractAddressPositionZero() {
        return "0x0000000000000000000000000000000000000000000000000000000000000000";
    }

    @Override
    public String validContractCode() {
        return "0x";
    }

    @Override
    public Transaction buildTransaction() {
        return Transaction.createContractTransaction(
                validAccount(),
                BigInteger.ZERO, // nonce
                Transaction.DEFAULT_GAS,
                validContractCode());
    }

    @Override
    public String validTransactionHash() {
        return "0xf26d441775da4e01cb557dfe35e09ab8c8a69134b2687209e34348c11ae54509";
    }

    @Override
    public String validUncleBlockHash() {
        return "0x9d512dd0cad173dd3e7ec568794db03541c4a98448cc5940b695da604d118754";
    }

    @Override
    public BigInteger validUncleBlock() {
        return BigInteger.valueOf(1640092);
    }

    @Override
    public String encodedEvent() {
        Event event =
                new Event(
                        "Notify",
                        Arrays.asList(
                                new TypeReference<Uint>(true) {}, new TypeReference<Uint>() {}));

        return EventEncoder.encode(event);
    }
}
