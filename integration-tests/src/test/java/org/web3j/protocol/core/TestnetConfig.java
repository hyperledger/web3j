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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.test.contract.Fibonacci;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Convert;

/** Web3j-Unit Embedded Testnet Configuration. */
public class TestnetConfig implements IntegrationTestConfig {

    final String blockHash = "0xeba1ae1115682dfa88f7c8c82bbdcb8ce5b599c05d688ce5c715383637199b15";
    final BigInteger validBlock = BigInteger.valueOf(0);
    final BigInteger validBlockTransactionCount = BigInteger.valueOf(0);
    final BigInteger validBlockUncleCount = BigInteger.valueOf(0);
    final String validAccount = "0xFE3B557E8Fb62b89F4916B721be55cEb828dBd73";
    final String validPrivateKey =
            "0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";
    private final String validTransactionHash;
    private final String validContractAddress;
    private final String validBlockHash;
    private final BigInteger validBlockNumber;
    private final BigInteger transactionIndex;
    private final Fibonacci validDeployedContract;

    TestnetConfig(
            Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider)
            throws Exception {
        this.validDeployedContract =
                Fibonacci.deploy(web3j, transactionManager, gasProvider).send();
        TransactionReceipt transactionReceipt = validDeployedContract.getTransactionReceipt().get();
        this.validContractAddress = validDeployedContract.getContractAddress();
        this.validTransactionHash = transactionReceipt.getTransactionHash();
        this.validBlockHash = transactionReceipt.getBlockHash();
        this.validBlockNumber = transactionReceipt.getBlockNumber();
        this.transactionIndex = transactionReceipt.getTransactionIndex();
    }

    @Override
    public Fibonacci getValidDeployedContract() {
        return this.validDeployedContract;
    }

    @Override
    public String validBlockHash() {
        return validBlockHash;
    }

    @Override
    public BigInteger validBlock() {
        return BigInteger.valueOf(71032);
    }

    @Override
    public BigInteger validBlockNumber() {
        return this.validBlockNumber;
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
        return this.validAccount;
    }

    @Override
    public String validPrivateKey() {
        return this.validPrivateKey;
    }

    @Override
    public BigInteger validTransactionIndex() {
        return this.transactionIndex;
    }

    @Override
    public String validContractAddress() {
        // Deployed fibonacci example
        return validContractAddress;
    }

    @Override
    public String validContractAddressPositionZero() {
        return "0x0000000000000000000000000000000000000000000000000000000000000000";
    }

    @Override
    public String validContractCode() {
        return "608060405234801561001057600080fd5b5061014f806100206000396000f30060806040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633c7fdc70811461005057806361047ff41461007a575b600080fd5b34801561005c57600080fd5b50610068600435610092565b60408051918252519081900360200190f35b34801561008657600080fd5b506100686004356100e0565b600061009d826100e0565b604080518481526020810183905281519293507f71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed929081900390910190a1919050565b60008115156100f15750600061011e565b81600114156101025750600161011e565b61010e600283036100e0565b61011a600184036100e0565b0190505b9190505600a165627a7a723058201b9d0941154b95636fb5e4225fefd5c2c460060efa5f5e40c9826dce08814af80029";
    }

    @Override
    public Transaction buildTransaction(Web3j web3j, ContractGasProvider gasProvider)
            throws IOException {
        return Transaction.createContractTransaction(
                validAccount(),
                web3j.ethGetTransactionCount(validAccount, DefaultBlockParameterName.LATEST)
                        .send()
                        .getTransactionCount(), // nonce
                gasProvider.getGasPrice(),
                validContractCode());
    }

    @Override
    public TransactionReceipt transferEth(Web3j web3j) throws Exception {

        return Transfer.sendFunds(
                        web3j,
                        Credentials.create(validPrivateKey()),
                        "0x000000000000000000000000000000000000dEaD",
                        BigDecimal.valueOf(1.0),
                        Convert.Unit.ETHER)
                .send();
    }

    @Override
    public String validTransactionHash() {
        return validTransactionHash;
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
