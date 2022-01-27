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
package org.web3j.ens;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainIdLong;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.web3j.ens.EnsResolver.DEFAULT_SYNC_THRESHOLD;
import static org.web3j.ens.EnsResolver.isValidEnsName;

public class EnsResolverTest {

    private Web3j web3j;
    private Web3jService web3jService;
    private EnsResolver ensResolver;

    @BeforeEach
    public void setUp() {
        web3jService = mock(Web3jService.class);
        web3j = Web3j.build(web3jService);
        ensResolver = new EnsResolver(web3j);
    }

    @Test
    public void testResolve() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000); // block timestamp is in seconds

        NetVersion netVersion = new NetVersion();
        netVersion.setResult(Long.toString(ChainIdLong.MAINNET));

        String resolverAddress =
                "0x0000000000000000000000004c641fb9bad9b60ef180c31f56051ce826d21a9a";
        String contractAddress =
                "0x00000000000000000000000019e03255f667bdfd50a32722df860b1eeaf4d635";

        EthCall resolverAddressResponse = new EthCall();
        resolverAddressResponse.setResult(resolverAddress);

        EthCall contractAddressResponse = new EthCall();
        contractAddressResponse.setResult(contractAddress);

        when(web3jService.send(any(Request.class), eq(NetVersion.class))).thenReturn(netVersion);
        when(web3jService.send(any(Request.class), eq(EthCall.class)))
                .thenReturn(resolverAddressResponse);
        when(web3jService.send(any(Request.class), eq(EthCall.class)))
                .thenReturn(contractAddressResponse);

        assertEquals(
                ensResolver.resolve("web3j.eth"), ("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
    }

    @Test
    public void testResolveEnsNameEmptyOrDot() throws Exception {
        assertNull(ensResolver.resolve(" "));
        assertNull(ensResolver.resolve(""));
        assertNull(ensResolver.resolve("."));
        assertNull(ensResolver.resolve(" . "));
    }

    @Test
    public void testReverseResolve() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000); // block timestamp is in seconds

        NetVersion netVersion = new NetVersion();
        netVersion.setResult(Long.toString(ChainIdLong.MAINNET));

        String resolverAddress =
                "0x0000000000000000000000004c641fb9bad9b60ef180c31f56051ce826d21a9a";
        String contractName =
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                        + TypeEncoder.encode(new Utf8String("web3j.eth"));
        System.err.println(contractName);

        EthCall resolverAddressResponse = new EthCall();
        resolverAddressResponse.setResult(resolverAddress);

        EthCall contractNameResponse = new EthCall();
        contractNameResponse.setResult(contractName);

        when(web3jService.send(any(Request.class), eq(NetVersion.class))).thenReturn(netVersion);
        when(web3jService.send(any(Request.class), eq(EthCall.class)))
                .thenReturn(resolverAddressResponse);
        when(web3jService.send(any(Request.class), eq(EthCall.class)))
                .thenReturn(contractNameResponse);

        assertEquals(
                ensResolver.reverseResolve("0x19e03255f667bdfd50a32722df860b1eeaf4d635"),
                ("web3j.eth"));
    }

    @Test
    public void testIsSyncedSyncing() throws Exception {
        configureSyncing(true);

        assertFalse(ensResolver.isSynced());
    }

    @Test
    public void testIsSyncedFullySynced() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000); // block timestamp is in seconds

        assertTrue(ensResolver.isSynced());
    }

    @Test
    public void testIsSyncedBelowThreshold() throws Exception {
        configureSyncing(false);
        configureLatestBlock((System.currentTimeMillis() / 1000) - DEFAULT_SYNC_THRESHOLD);

        assertFalse(ensResolver.isSynced());
    }

    private void configureSyncing(boolean isSyncing) throws IOException {
        EthSyncing ethSyncing = new EthSyncing();
        EthSyncing.Result result = new EthSyncing.Result();
        result.setSyncing(isSyncing);
        ethSyncing.setResult(result);

        when(web3jService.send(any(Request.class), eq(EthSyncing.class))).thenReturn(ethSyncing);
    }

    private void configureLatestBlock(long timestamp) throws IOException {
        EthBlock.Block block = new EthBlock.Block();
        block.setTimestamp(Numeric.encodeQuantity(BigInteger.valueOf(timestamp)));
        EthBlock ethBlock = new EthBlock();
        ethBlock.setResult(block);

        when(web3jService.send(any(Request.class), eq(EthBlock.class))).thenReturn(ethBlock);
    }

    @Test
    public void testIsEnsName() {
        assertTrue(isValidEnsName("eth"));
        assertTrue(isValidEnsName("web3.eth"));
        assertTrue(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635.eth"));

        assertFalse(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
        assertFalse(isValidEnsName("19e03255f667bdfd50a32722df860b1eeaf4d635"));

        assertTrue(isValidEnsName(""));
        assertTrue(isValidEnsName("."));
    }
}
