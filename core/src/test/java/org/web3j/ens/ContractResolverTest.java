package org.web3j.ens;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.tx.ChainId;
import org.web3j.utils.Numeric;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.web3j.ens.ContractResolver.DEFAULT_SYNC_THRESHOLD;
import static org.web3j.ens.ContractResolver.isValidEnsName;

public class ContractResolverTest {

    private Web3j web3j;
    private Web3jService web3jService;
    private ContractResolver contractResolver;

    @Before
    public void setUp() {
        web3jService = mock(Web3jService.class);
        web3j = Web3j.build(web3jService);
        contractResolver = new ContractResolver(web3j);
    }

    @Test
    public void testLookupAddress() throws Exception {
        NetVersion netVersion = new NetVersion();
        netVersion.setResult(Byte.toString(ChainId.MAINNET));

        String resolverAddress =
                "0x0000000000000000000000004c641fb9bad9b60ef180c31f56051ce826d21a9a";
        String contractAddress =
                "0x00000000000000000000000019e03255f667bdfd50a32722df860b1eeaf4d635";

        EthCall resolverAddressResponse = new EthCall();
        resolverAddressResponse.setResult(resolverAddress);

        EthCall contractAddressResponse = new EthCall();
        contractAddressResponse.setResult(contractAddress);

        when(web3jService.send(any(Request.class), eq(NetVersion.class)))
                .thenReturn(netVersion);
        when(web3jService.send(any(Request.class), eq(EthCall.class)))
                .thenReturn(resolverAddressResponse);
        when(web3jService.send(any(Request.class), eq(EthCall.class)))
                .thenReturn(contractAddressResponse);

        assertThat(contractResolver.lookupAddress("web3j.eth"),
                is("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
    }

    @Test
    public void testIsSyncedSyncing() throws Exception {
        configureSyncing(true);

        assertFalse(contractResolver.isSynced());
    }

    @Test
    public void testIsSyncedFullySynced() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000);  // block timestamp is in seconds

        assertTrue(contractResolver.isSynced());
    }

    @Test
    public void testIsSyncedBelowThreshold() throws Exception {
        configureSyncing(false);
        configureLatestBlock((System.currentTimeMillis() / 1000) - DEFAULT_SYNC_THRESHOLD);

        assertFalse(contractResolver.isSynced());
    }

    private void configureSyncing(boolean isSynced) throws IOException {
        EthSyncing ethSyncing = new EthSyncing();
        EthSyncing.Result result = new EthSyncing.Result();
        result.setSyncing(isSynced);
        ethSyncing.setResult(result);

        when(web3jService.send(any(Request.class), eq(EthSyncing.class)))
                .thenReturn(ethSyncing);
    }

    private void configureLatestBlock(long timestamp) throws IOException {
        EthBlock.Block block = new EthBlock.Block();
        block.setTimestamp(Numeric.encodeQuantity(BigInteger.valueOf(timestamp)));
        EthBlock ethBlock = new EthBlock();
        ethBlock.setResult(block);

        when(web3jService.send(any(Request.class), eq(EthBlock.class)))
                .thenReturn(ethBlock);
    }

    @Test
    public void testIsEnsName() {
        assertTrue(isValidEnsName("eth"));
        assertTrue(isValidEnsName("web3.eth"));
        assertTrue(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635.eth"));

        assertFalse(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
        assertFalse(isValidEnsName("19e03255f667bdfd50a32722df860b1eeaf4d635"));

        assertTrue(isValidEnsName(""));
    }
}
