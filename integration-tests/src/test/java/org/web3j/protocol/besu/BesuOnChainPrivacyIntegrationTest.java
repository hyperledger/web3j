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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.web3j.crypto.Credentials;
import org.web3j.generated.HumanStandardToken;
import org.web3j.protocol.besu.response.privacy.PrivacyGroup;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.BesuPrivateTransactionManager;
import org.web3j.tx.PrivateTransactionManager;
import org.web3j.tx.gas.BesuPrivacyGasProvider;
import org.web3j.tx.response.PollingPrivateTransactionReceiptProcessor;
import org.web3j.utils.Base64String;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class BesuOnChainPrivacyIntegrationTest {

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

    private static Besu nodeAlice;
    private static Besu nodeBob;
    private static Besu nodeCharlie;
    private static PollingPrivateTransactionReceiptProcessor processor;

    @BeforeAll
    public static void setUpOnce() {
        nodeAlice = Besu.build(new HttpService("http://localhost:20000"));
        nodeBob = Besu.build(new HttpService("http://localhost:20002"));
        nodeCharlie = Besu.build(new HttpService("http://localhost:20004"));
        processor = new PollingPrivateTransactionReceiptProcessor(nodeAlice, 1000, 15);
    }

    static byte[] generateRandomBytes(int size) {
        byte[] bytes = new byte[size];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    private static final BesuPrivacyGasProvider ZERO_GAS_PROVIDER =
            new BesuPrivacyGasProvider(BigInteger.valueOf(0));

    @Test
    public void testCreateAndFindOnChainPrivacyGroup() throws Exception {
        Base64String privacyGroupId = Base64String.wrap(generateRandomBytes(32));
        final String txHash =
                nodeAlice
                        .privOnChainCreatePrivacyGroup(
                                privacyGroupId,
                                ALICE,
                                ENCLAVE_KEY_ALICE,
                                Collections.singletonList(ENCLAVE_KEY_BOB))
                        .send()
                        .getTransactionHash();

        TransactionReceipt receipt = processor.waitForTransactionReceipt(txHash);
        assertTrue(receipt.isStatusOK());

        List<PrivacyGroup> groups =
                nodeAlice
                        .privOnChainFindPrivacyGroup(
                                Arrays.asList(ENCLAVE_KEY_ALICE, ENCLAVE_KEY_BOB))
                        .send()
                        .getGroups();

        assertTrue(groups.stream().anyMatch(x -> x.getPrivacyGroupId().equals(privacyGroupId)));
    }

    @Test
    public void testCreateAddRemoveOnChainPrivacyGroup() throws Exception {
        Base64String privacyGroupId = Base64String.wrap(generateRandomBytes(32));
        final String createTxHash =
                nodeAlice
                        .privOnChainCreatePrivacyGroup(
                                privacyGroupId,
                                ALICE,
                                ENCLAVE_KEY_ALICE,
                                Collections.singletonList(ENCLAVE_KEY_BOB))
                        .send()
                        .getTransactionHash();

        TransactionReceipt createReceipt = processor.waitForTransactionReceipt(createTxHash);
        assertTrue(createReceipt.isStatusOK());

        final String addTxHash =
                nodeAlice
                        .privOnChainAddToPrivacyGroup(
                                privacyGroupId,
                                ALICE,
                                ENCLAVE_KEY_ALICE,
                                Collections.singletonList(ENCLAVE_KEY_CHARLIE))
                        .send()
                        .getTransactionHash();

        TransactionReceipt addReceipt = processor.waitForTransactionReceipt(addTxHash);
        assertTrue(addReceipt.isStatusOK());

        List<PrivacyGroup> groups =
                nodeAlice
                        .privOnChainFindPrivacyGroup(
                                Arrays.asList(
                                        ENCLAVE_KEY_ALICE, ENCLAVE_KEY_BOB, ENCLAVE_KEY_CHARLIE))
                        .send()
                        .getGroups();
        assertTrue(
                groups.stream()
                        .anyMatch(
                                g ->
                                        g.getPrivacyGroupId().equals(privacyGroupId)
                                                && g.getMembers().size() == 3));

        final String removeTxHash =
                nodeAlice
                        .privOnChainRemoveFromPrivacyGroup(
                                privacyGroupId, ALICE, ENCLAVE_KEY_ALICE, ENCLAVE_KEY_CHARLIE)
                        .send()
                        .getTransactionHash();

        TransactionReceipt removeReceipt = processor.waitForTransactionReceipt(removeTxHash);
        assertTrue(removeReceipt.isStatusOK());

        List<PrivacyGroup> removedGroups =
                nodeAlice
                        .privOnChainFindPrivacyGroup(
                                Arrays.asList(ENCLAVE_KEY_ALICE, ENCLAVE_KEY_BOB))
                        .send()
                        .getGroups();
        assertTrue(
                removedGroups.stream()
                        .anyMatch(
                                g ->
                                        g.getPrivacyGroupId().equals(privacyGroupId)
                                                && g.getMembers().size() == 2));
    }

    @Test
    public void testCannotAddToAlreadyLockedGroup() throws Exception {
        Base64String privacyGroupId = Base64String.wrap(generateRandomBytes(32));
        final String createTxHash =
                nodeAlice
                        .privOnChainCreatePrivacyGroup(
                                privacyGroupId,
                                ALICE,
                                ENCLAVE_KEY_ALICE,
                                Collections.singletonList(ENCLAVE_KEY_BOB))
                        .send()
                        .getTransactionHash();

        TransactionReceipt createReceipt = processor.waitForTransactionReceipt(createTxHash);
        assertTrue(createReceipt.isStatusOK());

        final String lockTxHash =
                nodeAlice
                        .privOnChainSetGroupLockState(
                                privacyGroupId, ALICE, ENCLAVE_KEY_ALICE, true)
                        .send()
                        .getTransactionHash();

        TransactionReceipt lockReceipt = processor.waitForTransactionReceipt(lockTxHash);
        assertTrue(lockReceipt.isStatusOK());

        assertThrows(
                TransactionException.class,
                () ->
                        nodeBob.privOnChainAddToPrivacyGroup(
                                        privacyGroupId,
                                        BOB,
                                        ENCLAVE_KEY_BOB,
                                        Collections.singletonList(ENCLAVE_KEY_CHARLIE))
                                .send());
    }

    @Test
    public void testCanExecuteSmartContractsInOnChainPrivacyGroup() throws Exception {
        Base64String aliceBobGroup = Base64String.wrap(generateRandomBytes(32));
        final String createTxHash =
                nodeAlice
                        .privOnChainCreatePrivacyGroup(
                                aliceBobGroup,
                                ALICE,
                                ENCLAVE_KEY_ALICE,
                                Collections.singletonList(ENCLAVE_KEY_BOB))
                        .send()
                        .getTransactionHash();

        TransactionReceipt createReceipt = processor.waitForTransactionReceipt(createTxHash);
        assertTrue(createReceipt.isStatusOK());

        // Find the privacy group that was built by Alice from Bob's node
        final Base64String aliceBobGroupFromBobNode =
                nodeBob
                        .privOnChainFindPrivacyGroup(
                                Arrays.asList(ENCLAVE_KEY_ALICE, ENCLAVE_KEY_BOB))
                        .send().getGroups().stream()
                        .filter(g -> g.getPrivacyGroupId().equals(aliceBobGroup))
                        .findFirst()
                        .orElseThrow(RuntimeException::new)
                        .getPrivacyGroupId();

        final PrivateTransactionManager tmAlice =
                new BesuPrivateTransactionManager(
                        nodeAlice,
                        ZERO_GAS_PROVIDER,
                        ALICE,
                        2018,
                        ENCLAVE_KEY_ALICE,
                        aliceBobGroup);
        final PrivateTransactionManager tmBob =
                new BesuPrivateTransactionManager(
                        nodeBob,
                        ZERO_GAS_PROVIDER,
                        BOB,
                        2018,
                        ENCLAVE_KEY_BOB,
                        aliceBobGroupFromBobNode);

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
    public void testCannotAddDuplicateMemberToPrivacyGroup() throws Exception {
        Base64String privacyGroupId = Base64String.wrap(generateRandomBytes(32));
        final String txHash =
                nodeAlice
                        .privOnChainCreatePrivacyGroup(
                                privacyGroupId,
                                ALICE,
                                ENCLAVE_KEY_ALICE,
                                Collections.singletonList(ENCLAVE_KEY_BOB))
                        .send()
                        .getTransactionHash();

        TransactionReceipt receipt = processor.waitForTransactionReceipt(txHash);
        assertTrue(receipt.isStatusOK());

        final String addTxHash =
                nodeAlice
                        .privOnChainAddToPrivacyGroup(
                                privacyGroupId,
                                ALICE,
                                ENCLAVE_KEY_ALICE,
                                Collections.singletonList(ENCLAVE_KEY_CHARLIE))
                        .send()
                        .getTransactionHash();

        TransactionReceipt addReceipt = processor.waitForTransactionReceipt(addTxHash);
        assertTrue(addReceipt.isStatusOK());

        final String secondAddTxHash =
                nodeAlice
                        .privOnChainAddToPrivacyGroup(
                                privacyGroupId,
                                ALICE,
                                ENCLAVE_KEY_ALICE,
                                Collections.singletonList(ENCLAVE_KEY_CHARLIE))
                        .send()
                        .getTransactionHash();

        TransactionReceipt secondAddTxReceipt =
                processor.waitForTransactionReceipt(secondAddTxHash);
        assertFalse(secondAddTxReceipt.isStatusOK());
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
