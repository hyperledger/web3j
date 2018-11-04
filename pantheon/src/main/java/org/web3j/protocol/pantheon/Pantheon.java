package org.web3j.protocol.pantheon;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.MinerStartResponse;
import org.web3j.protocol.pantheon.response.PantheonEthAccountsMapResponse;
import org.web3j.protocol.pantheon.response.PantheonFullDebugTraceResponse;

import java.util.Map;

public interface Pantheon extends Web3j {
    static Pantheon build(Web3jService web3jService) {
        return new JsonRpc2_0Pantheon(web3jService);
    }

    Request<?, MinerStartResponse> minerStart();

    Request<?, BooleanResponse> minerStop();

    Request<?, BooleanResponse> clicqueDiscard(String address);

    Request<?, EthAccounts> clicqueGetSigners(DefaultBlockParameter defaultBlockParameter);

    Request<?, EthAccounts> clicqueGetSignersAtHash(String blockHash);

    Request<?, BooleanResponse> cliquePropose(String address, Boolean signerAddition);

    Request<?, PantheonEthAccountsMapResponse> cliqueProposals();

    Request<?, PantheonFullDebugTraceResponse> debugTraceTransaction(String transactionHash, Map<String, Boolean> options);
}
