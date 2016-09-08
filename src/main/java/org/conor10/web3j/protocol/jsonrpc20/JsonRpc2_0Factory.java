package org.conor10.web3j.protocol.jsonrpc20;


import java.util.Arrays;
import java.util.Collections;

import org.conor10.web3j.protocol.RequestFactory;
import org.conor10.web3j.protocol.Utils;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0Factory implements RequestFactory {

    @Override
    public Request web3ClientVersion() {
        return new Request(
                "web3_clientVersion",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request web3Sha3(String data) {
        return new Request(
                "web3_sha3",
                Arrays.asList(data),
                1
        );
    }

    @Override
    public Request netVersion() {
        return new Request(
                "net_version",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request netListening() {
        return new Request(
                "net_listening",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request netPeerCount() {
        return new Request(
                "net_peerCount",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request ethProtocolVersion() {
        return new Request(
                "eth_protocolVersion",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request ethCoinbase() {
        return new Request(
                "eth_coinbase",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request ethSyncing() {
        return new Request(
                "eth_syncing",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request ethMining() {
        return new Request(
                "eth_mining",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request ethHashrate() {
        return new Request(
                "eth_syncing",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request ethGasPrice() {
        return new Request(
                "eth_gasPrice",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request ethAccounts() {
        return new Request(
                "eth_accounts",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request ethBlockNumber() {
        return new Request(
                "eth_blockNumber",
                Collections.EMPTY_LIST,
                1
        );
    }

    @Override
    public Request ethGetBalance(String address, DefaultBlockParamTag defaultBlockParamTag) {
        return new Request(
                "eth_syncing",
                Arrays.asList(address, defaultBlockParamTag.getValue()),
                1
        );
    }

    @Override
    public Request ethGetBalance(String address, int blockNumber) {
        return new Request(
                "eth_syncing",
                Arrays.asList(address, Utils.encodeQuantity(blockNumber)),
                1
        );
    }
}
