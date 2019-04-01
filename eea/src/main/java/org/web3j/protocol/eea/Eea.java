package org.web3j.protocol.eea;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.eea.response.EeaClientCapabilities;
import org.web3j.protocol.eea.response.EeaGetTransactionReceipt;

public interface Eea extends Web3j {
    static Eea build(Web3jService web3jService) {
        return new JsonRpc2_0Eea(web3jService);
    }

    Request<?, EthSendTransaction> eeaSendRawTransaction(String signedTransactionData);

    Request<?, EeaGetTransactionReceipt> eeaGetTransactionReceipt(String transactionHash);

    Request<?, EeaClientCapabilities> eeaClientCapabilities();
}
