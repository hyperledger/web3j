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
package org.web3j.protocol.besu;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.besu.response.privacy.PrivateTransactionReceipt;
import org.web3j.protocol.besu.response.privacy.PrivateTransactionWithPrivacyGroup;
import org.web3j.protocol.eea.crypto.PrivateTransactionEncoder;
import org.web3j.protocol.eea.crypto.RawPrivateTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.test.contract.HumanStandardToken;
import org.web3j.tx.ChainIdLong;
import org.web3j.tx.PrivateTransactionManager;
import org.web3j.tx.gas.BesuPrivacyGasProvider;
import org.web3j.tx.response.PollingPrivateTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Base64String;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_FREQUENCY;
import static org.web3j.utils.Restriction.RESTRICTED;

/**
 * Test designed to run with besu-quickstart https://github.com/PegaSysEng/besu-quickstart Using
 * orion 1.3.2
 */
@Disabled
public class BesuPrivacyQuickstartIntegrationTest {
    private static final String CLIENT_VERSION = "besu/v1.3.0/linux-x86_64/oracle_openjdk-java-11";

    // FIXME: This should be made public in the contract wrapper
    private static final String HUMAN_STANDARD_TOKEN_BINARY =
            "60c0604052600460808190527f48302e310000000000000000000000000000000000000000000000000000000060a090815261003e91600691906100d0565b5034801561004b57600080fd5b506040516109ab3803806109ab8339810160409081528151602080840151838501516060860151336000908152600185529586208590559484905590850180519395909491939101916100a3916003918601906100d0565b506004805460ff191660ff841617905580516100c69060059060208401906100d0565b505050505061016b565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061011157805160ff191683800117855561013e565b8280016001018555821561013e579182015b8281111561013e578251825591602001919060010190610123565b5061014a92915061014e565b5090565b61016891905b8082111561014a5760008155600101610154565b90565b6108318061017a6000396000f3006080604052600436106100955763ffffffff60e060020a60003504166306fdde0381146100a7578063095ea7b31461013157806318160ddd1461016957806323b872dd14610190578063313ce567146101ba57806354fd4d50146101e557806370a08231146101fa57806395d89b411461021b578063a9059cbb14610230578063cae9ca5114610254578063dd62ed3e146102bd575b3480156100a157600080fd5b50600080fd5b3480156100b357600080fd5b506100bc6102e4565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100f65781810151838201526020016100de565b50505050905090810190601f1680156101235780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561013d57600080fd5b50610155600160a060020a0360043516602435610372565b604080519115158252519081900360200190f35b34801561017557600080fd5b5061017e6103d9565b60408051918252519081900360200190f35b34801561019c57600080fd5b50610155600160a060020a03600435811690602435166044356103df565b3480156101c657600080fd5b506101cf6104cc565b6040805160ff9092168252519081900360200190f35b3480156101f157600080fd5b506100bc6104d5565b34801561020657600080fd5b5061017e600160a060020a0360043516610530565b34801561022757600080fd5b506100bc61054b565b34801561023c57600080fd5b50610155600160a060020a03600435166024356105a6565b34801561026057600080fd5b50604080516020600460443581810135601f8101849004840285018401909552848452610155948235600160a060020a031694602480359536959460649492019190819084018382808284375094975061063f9650505050505050565b3480156102c957600080fd5b5061017e600160a060020a03600435811690602435166107da565b6003805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561036a5780601f1061033f5761010080835404028352916020019161036a565b820191906000526020600020905b81548152906001019060200180831161034d57829003601f168201915b505050505081565b336000818152600260209081526040808320600160a060020a038716808552908352818420869055815186815291519394909390927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925928290030190a35060015b92915050565b60005481565b600160a060020a038316600090815260016020526040812054821180159061042a5750600160a060020a03841660009081526002602090815260408083203384529091529020548211155b80156104365750600082115b156104c157600160a060020a03808416600081815260016020908152604080832080548801905593881680835284832080548890039055600282528483203384528252918490208054879003905583518681529351929391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a35060016104c5565b5060005b9392505050565b60045460ff1681565b6006805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561036a5780601f1061033f5761010080835404028352916020019161036a565b600160a060020a031660009081526001602052604090205490565b6005805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561036a5780601f1061033f5761010080835404028352916020019161036a565b3360009081526001602052604081205482118015906105c55750600082115b156106375733600081815260016020908152604080832080548790039055600160a060020a03871680845292819020805487019055805186815290519293927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a35060016103d3565b5060006103d3565b336000818152600260209081526040808320600160a060020a038816808552908352818420879055815187815291519394909390927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925928290030190a383600160a060020a031660405180807f72656365697665417070726f76616c28616464726573732c75696e743235362c81526020017f616464726573732c627974657329000000000000000000000000000000000000815250602e019050604051809103902060e060020a9004338530866040518563ffffffff1660e060020a0281526004018085600160a060020a0316600160a060020a0316815260200184815260200183600160a060020a0316600160a060020a03168152602001828051906020019080838360005b8381101561077f578181015183820152602001610767565b50505050905090810190601f1680156107ac5780820380516001836020036101000a031916815260200191505b509450505050506000604051808303816000875af19250505015156107d057600080fd5b5060019392505050565b600160a060020a039182166000908152600260209081526040808320939094168252919091522054905600a165627a7a723058203f2de808df5359509254dc2a0d616b226de2b64f0bf28bae7323aeba4487199b0029";

    private static final Credentials ALICE =
            Credentials.create("8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63");
    private static final Credentials BOB =
            Credentials.create("c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3");
    private static final Credentials CHARLIE =
            Credentials.create("ae6ae8e5ccbfb04590405997ee2d52d2b330726137b875053c36d94e974d162f");

    private static final Base64String ENCLAVE_KEY_ALICE =
            Base64String.wrap("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=");
    private static final Base64String ENCLAVE_KEY_BOB =
            Base64String.wrap("Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=");
    private static final Base64String ENCLAVE_KEY_CHARLIE =
            Base64String.wrap("k2zXEin4Ip/qBGlRkJejnGWdP9cjkK+DAvKNW31L2C8=");

    private static final BesuPrivacyGasProvider ZERO_GAS_PROVIDER =
            new BesuPrivacyGasProvider(BigInteger.valueOf(0));

    private static Besu nodeAlice;
    private static Besu nodeBob;
    private static Besu nodeCharlie;

    @BeforeAll
    public static void setUpOnce() {
        nodeAlice = Besu.build(new HttpService("http://localhost:20000"));
        nodeBob = Besu.build(new HttpService("http://localhost:20002"));
        nodeCharlie = Besu.build(new HttpService("http://localhost:20004"));
    }

    @Test
    public void testConnection() throws IOException {
        assertEquals(nodeAlice.web3ClientVersion().send().getWeb3ClientVersion(), (CLIENT_VERSION));
        assertEquals(nodeBob.web3ClientVersion().send().getWeb3ClientVersion(), (CLIENT_VERSION));
        assertEquals(
                nodeCharlie.web3ClientVersion().send().getWeb3ClientVersion(), (CLIENT_VERSION));
    }

    @Test
    public void simplePrivateTransactions() throws Exception {

        // Build new privacy group using the create API
        final Base64String privacyGroupId =
                nodeBob.privCreatePrivacyGroup(
                                Arrays.asList(
                                        ENCLAVE_KEY_ALICE, ENCLAVE_KEY_BOB, ENCLAVE_KEY_CHARLIE),
                                "AliceBobCharlie",
                                "AliceBobCharlie group")
                        .send()
                        .getPrivacyGroupId();

        final BigInteger nonce =
                nodeCharlie
                        .privGetTransactionCount(ALICE.getAddress(), privacyGroupId)
                        .send()
                        .getTransactionCount();
        final RawPrivateTransaction rawPrivateTransaction =
                RawPrivateTransaction.createContractTransaction(
                        nonce,
                        ZERO_GAS_PROVIDER.getGasPrice(),
                        ZERO_GAS_PROVIDER.getGasLimit(),
                        HUMAN_STANDARD_TOKEN_BINARY,
                        ENCLAVE_KEY_ALICE,
                        privacyGroupId,
                        RESTRICTED);

        final String signedTransactionData =
                Numeric.toHexString(
                        PrivateTransactionEncoder.signMessage(rawPrivateTransaction, 2018, ALICE));

        final String transactionHash =
                nodeAlice.eeaSendRawTransaction(signedTransactionData).send().getTransactionHash();

        final PollingPrivateTransactionReceiptProcessor receiptProcessor =
                new PollingPrivateTransactionReceiptProcessor(nodeAlice, 1 * 1000, 120);
        final PrivateTransactionReceipt receipt =
                receiptProcessor.waitForTransactionReceipt(transactionHash);

        assertEquals(receipt.getFrom(), (ALICE.getAddress()));
        assertEquals(receipt.getLogs().size(), (0));
        assertNull(receipt.getTo());
        assertNotNull(receipt.getContractAddress());

        assertNotNull(receipt.getStatus());

        assertNull(receipt.getRevertReason());

        final PrivateTransactionWithPrivacyGroup privateTransaction =
                (PrivateTransactionWithPrivacyGroup)
                        nodeAlice
                                .privGetPrivateTransaction(transactionHash)
                                .send()
                                .getPrivateTransaction()
                                .get();

        assertEquals(privateTransaction.getFrom(), (ALICE.getAddress()));
        assertEquals(privateTransaction.getGas(), (ZERO_GAS_PROVIDER.getGasLimit()));
        assertEquals(privateTransaction.getGasPrice(), (ZERO_GAS_PROVIDER.getGasPrice()));
        assertEquals(privateTransaction.getNonce(), (nonce));

        final byte[] encodedTransaction =
                PrivateTransactionEncoder.encode(rawPrivateTransaction, 2018);
        final Sign.SignatureData signatureData =
                Sign.signMessage(encodedTransaction, ALICE.getEcKeyPair());
        final Sign.SignatureData eip155SignatureData =
                TransactionEncoder.createEip155SignatureData(signatureData, 2018);
        assertEquals(
                Numeric.toBytesPadded(BigInteger.valueOf(privateTransaction.getV()), 2),
                (eip155SignatureData.getV()));
        assertEquals(
                Numeric.hexStringToByteArray(privateTransaction.getR()),
                (eip155SignatureData.getR()));
        assertEquals(
                Numeric.hexStringToByteArray(privateTransaction.getS()),
                (eip155SignatureData.getS()));

        assertEquals(privateTransaction.getPrivateFrom(), (ENCLAVE_KEY_ALICE));
        assertEquals(privateTransaction.getPrivacyGroupId(), (privacyGroupId));
        assertEquals(privateTransaction.getRestriction(), (RESTRICTED));
        assertNull(privateTransaction.getTo());
    }

    @Test
    public void legacyContract() throws Exception {
        TransactionReceiptProcessor transactionReceiptProcessor =
                new PollingPrivateTransactionReceiptProcessor(
                        nodeAlice, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);

        PrivateTransactionManager tmAlice =
                new PrivateTransactionManager(
                        nodeAlice,
                        ALICE,
                        transactionReceiptProcessor,
                        ChainIdLong.NONE,
                        ENCLAVE_KEY_ALICE,
                        Collections.singletonList(ENCLAVE_KEY_BOB),
                        RESTRICTED);

        PrivateTransactionManager tmBob =
                new PrivateTransactionManager(
                        nodeBob,
                        BOB,
                        transactionReceiptProcessor,
                        ChainIdLong.NONE,
                        ENCLAVE_KEY_BOB,
                        Collections.singletonList(ENCLAVE_KEY_ALICE),
                        RESTRICTED);

        final HumanStandardToken tokenAlice =
                HumanStandardToken.deploy(
                                nodeAlice,
                                tmAlice,
                                ZERO_GAS_PROVIDER,
                                BigInteger.TEN,
                                "eea_token",
                                BigInteger.TEN,
                                "EEATKN")
                        .send();

        final HumanStandardToken tokenBob =
                HumanStandardToken.load(
                        tokenAlice.getContractAddress(), nodeBob, tmBob, ZERO_GAS_PROVIDER);

        tokenAlice.transfer(BOB.getAddress(), BigInteger.TEN).send();
        testBalances(tokenAlice, tokenBob, BigInteger.ZERO, BigInteger.TEN);
    }

    @Test
    public void privacyGroupContract() throws Exception {
        // Build new privacy group using the create API
        final Base64String aliceBobGroup =
                nodeAlice
                        .privCreatePrivacyGroup(
                                Arrays.asList(ENCLAVE_KEY_ALICE, ENCLAVE_KEY_BOB),
                                "AliceBob",
                                "AliceBob group")
                        .send()
                        .getPrivacyGroupId();

        // Find the privacy group that was built by Alice from Bob's node
        final Base64String aliceBobGroupFromBobNode =
                nodeBob.privFindPrivacyGroup(Arrays.asList(ENCLAVE_KEY_ALICE, ENCLAVE_KEY_BOB))
                        .send().getGroups().stream()
                        .filter(
                                g ->
                                        g.getName().equals("AliceBob")
                                                && g.getDescription().equals("AliceBob group")
                                                && g.getPrivacyGroupId().equals(aliceBobGroup))
                        .findFirst()
                        .orElseThrow(RuntimeException::new)
                        .getPrivacyGroupId();

        TransactionReceiptProcessor transactionReceiptProcessor =
                new PollingPrivateTransactionReceiptProcessor(
                        nodeAlice, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);

        PrivateTransactionManager tmBob =
                new PrivateTransactionManager(
                        nodeBob,
                        BOB,
                        transactionReceiptProcessor,
                        ChainIdLong.NONE,
                        ENCLAVE_KEY_BOB,
                        aliceBobGroupFromBobNode,
                        RESTRICTED);

        PrivateTransactionManager tmAlice =
                new PrivateTransactionManager(
                        nodeAlice,
                        ALICE,
                        transactionReceiptProcessor,
                        ChainIdLong.NONE,
                        ENCLAVE_KEY_ALICE,
                        aliceBobGroupFromBobNode,
                        RESTRICTED);

        final HumanStandardToken tokenAlice =
                HumanStandardToken.deploy(
                                nodeAlice,
                                tmAlice,
                                ZERO_GAS_PROVIDER,
                                BigInteger.TEN,
                                "eea_token",
                                BigInteger.TEN,
                                "EEATKN")
                        .send();

        final HumanStandardToken tokenBob =
                HumanStandardToken.load(
                        tokenAlice.getContractAddress(), nodeBob, tmBob, ZERO_GAS_PROVIDER);

        tokenAlice.transfer(BOB.getAddress(), BigInteger.TEN).send();
        testBalances(tokenAlice, tokenBob, BigInteger.ZERO, BigInteger.TEN);
    }

    private void testBalances(
            final HumanStandardToken tokenAlice,
            final HumanStandardToken tokenBob,
            final BigInteger aliceBalance,
            final BigInteger bobBalance)
            throws Exception {
        final BigInteger aliceAlice = tokenAlice.balanceOf(ALICE.getAddress()).send();
        final BigInteger aliceBob = tokenAlice.balanceOf(BOB.getAddress()).send();
        final BigInteger bobAlice = tokenBob.balanceOf(ALICE.getAddress()).send();
        final BigInteger bobBob = tokenBob.balanceOf(BOB.getAddress()).send();

        assertEquals(aliceAlice, (aliceBalance));
        assertEquals(aliceBob, (bobBalance));
        assertEquals(bobAlice, (aliceBalance));
        assertEquals(bobBob, (bobBalance));
    }
}
