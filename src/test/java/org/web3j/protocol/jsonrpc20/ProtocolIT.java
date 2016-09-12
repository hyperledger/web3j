package org.web3j.protocol.jsonrpc20;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import org.web3j.Web3jService;
import org.web3j.methods.response.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * JSON-RPC 2.0 Integration Tests.
 */
public class ProtocolIT {

    private Web3jService web3jService;

    @Before
    public void setUp() {
        this.web3jService = new Web3jService();
    }

    @Test
    public void testWeb3ClientVersion() throws IOException {
        Web3ClientVersion web3ClientVersion = web3jService.web3ClientVersion();
        assertThat(web3ClientVersion.getJsonrpc(), is("Geth/v1.4.10-stable-5f55d95a/darwin/go1.6.2"));
    }

    @Test
    public void testWeb3Sha3() throws IOException {
        Web3Sha3 web3Sha3 = web3jService.web3Sha3("0x68656c6c6f20776f726c64");
        assertThat(web3Sha3.getResult(),
                is("0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad"));
    }

    @Test
    public void testNetVersion() throws IOException {
        NetVersion netVersion = web3jService.netVersion();
        assertThat(netVersion.getJsonrpc(), is("8192"));
    }

    @Test
    public void testNetListening() throws IOException {
        NetListening netListening = web3jService.netListening();
        assertThat(netListening.isListening(), is(true));
    }

    @Test
    public void testNetPeerCount() throws IOException {
        NetPeerCount netPeerCount = web3jService.netPeerCount();
        assertThat(netPeerCount.getQuantity(), is(0));
    }

    @Test
    public void testEthProtocolVersion() throws IOException {
        EthProtocolVersion ethProtocolVersion = web3jService.ethProtocolVersion();
        assertThat(ethProtocolVersion.getJsonrpc(), is("0x3f"));
    }

    @Test
    public void testEthSyncing() throws IOException {
        EthSyncing ethSyncing = web3jService.ethSyncing();
        assertThat(ethSyncing.getResult(), is(false));
    }

    @Test
    public void testEthCoinbase() throws IOException {
        EthCoinbase ethCoinbase = web3jService.ethCoinbase();
        assertThat(ethCoinbase.getAddress(), is("0x4a8ca6a9548a0a629652365a313e39a18a74d737"));
    }
}
