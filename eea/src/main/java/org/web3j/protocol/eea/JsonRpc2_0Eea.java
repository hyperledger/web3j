package org.web3j.protocol.eea;

import java.util.Arrays;
import java.util.Collections;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.eea.response.EeaClientCapabilities;
import org.web3j.protocol.eea.response.EeaGetTransactionReceipt;

public class JsonRpc2_0Eea extends JsonRpc2_0Web3j implements Eea {
    public JsonRpc2_0Eea(Web3jService web3jService) {
        super(web3jService);
    }

    @Override
    public Request<?, EthSendTransaction> eeaSendRawTransaction(String signedTransactionData) {
        return new Request<>(
                "eea_sendRawTransaction",
                Collections.singletonList(signedTransactionData),
                web3jService,
                EthSendTransaction.class);
    }

    @Override
    public Request<?, EeaGetTransactionReceipt> eeaGetTransactionReceipt(String transactionHash) {
        return new Request<>(
                "eea_getTransactionReceipt",
                Collections.singletonList(transactionHash),
                web3jService,
                EeaGetTransactionReceipt.class);
    }

    @Override
    public Request<?, EeaClientCapabilities> eeaClientCapabilities() {
        return new Request<>(
                "eea_clientCapabilities",
                Collections.emptyList(),
                web3jService,
                EeaClientCapabilities.class);
    }
}
