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
package org.web3j.protocol.pantheon;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import org.web3j.crypto.Credentials;
import org.web3j.generated.HumanStandardToken;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.LegacyPrivateTransactionManager;
import org.web3j.tx.PantheonPrivateTransactionManager;
import org.web3j.tx.PrivateTransactionManager;
import org.web3j.tx.gas.PantheonPrivacyGasProvider;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test designed to run with pantheon-quickstart https://github.com/PegaSysEng/pantheon-quickstart
 * Using orion 1.3.2
 */
@Ignore
public class PantheonQuickstartIntegrationTest {
    private static final String CLIENT_VERSION =
            "pantheon/v1.2.1-dev-ad2d34cd/linux-x86_64/oracle_openjdk-java-11";

    private static final Credentials ALICE =
            Credentials.create("8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63");
    private static final Credentials BOB =
            Credentials.create("c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3");
    private static final Credentials CHARLIE =
            Credentials.create("ae6ae8e5ccbfb04590405997ee2d52d2b330726137b875053c36d94e974d162f");

    private static final String ENCLAVE_KEY_ALICE = "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=";
    private static final String ENCLAVE_KEY_BOB = "Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=";
    private static final String ENCLAVE_KEY_CHARLIE =
            "k2zXEin4Ip/qBGlRkJejnGWdP9cjkK+DAvKNW31L2C8=";

    private static final PantheonPrivacyGasProvider ZERO_GAS_PROVIDER =
            new PantheonPrivacyGasProvider(BigInteger.valueOf(0));

    private static Pantheon nodeAlice;
    private static Pantheon nodeBob;
    private static Pantheon nodeCharlie;

    @BeforeClass
    public static void setUpOnce() {
        nodeAlice = Pantheon.build(new HttpService("http://localhost:20000"));
        nodeBob = Pantheon.build(new HttpService("http://localhost:20002"));
        nodeCharlie = Pantheon.build(new HttpService("http://localhost:20004"));
    }

    @Test
    public void testConnection() throws IOException {
        assertThat(nodeAlice.web3ClientVersion().send().getWeb3ClientVersion(), is(CLIENT_VERSION));
        assertThat(nodeBob.web3ClientVersion().send().getWeb3ClientVersion(), is(CLIENT_VERSION));
        assertThat(
                nodeCharlie.web3ClientVersion().send().getWeb3ClientVersion(), is(CLIENT_VERSION));
    }

    @Test
    public void legacyContract() throws Exception {
        final PrivateTransactionManager tmAlice =
                new LegacyPrivateTransactionManager(
                        nodeAlice, ALICE, 2018, ENCLAVE_KEY_ALICE, Arrays.asList(ENCLAVE_KEY_BOB));
        final PrivateTransactionManager tmBob =
                new LegacyPrivateTransactionManager(
                        nodeBob, BOB, 2018, ENCLAVE_KEY_BOB, Arrays.asList(ENCLAVE_KEY_ALICE));

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
        final String aliceBobGroup =
                nodeAlice
                        .privCreatePrivacyGroup(
                                Arrays.asList(ENCLAVE_KEY_ALICE, ENCLAVE_KEY_BOB),
                                "AliceBob",
                                "AliceBob group")
                        .send()
                        .getPrivacyGroupId();

        // Find the privacy group that was built by Alice from Bob's node
        final String aliceBobGroupFromBobNode =
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

        final PrivateTransactionManager tmAlice =
                new PantheonPrivateTransactionManager(
                        nodeAlice, ALICE, 2018, ENCLAVE_KEY_ALICE, aliceBobGroup);
        final PrivateTransactionManager tmBob =
                new PantheonPrivateTransactionManager(
                        nodeBob, BOB, 2018, ENCLAVE_KEY_BOB, aliceBobGroupFromBobNode);

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

        assertThat(aliceAlice, is(aliceBalance));
        assertThat(aliceBob, is(bobBalance));
        assertThat(bobAlice, is(aliceBalance));
        assertThat(bobBob, is(bobBalance));
    }
}
