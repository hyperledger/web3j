package org.web3j.tx.response;

import java.io.IOException;
import java.util.Optional;

import org.web3j.protocol.eea.Eea;
import org.web3j.protocol.eea.response.EeaGetTransactionReceipt;
import org.web3j.protocol.eea.response.PrivateTransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

public abstract class PrivateTransactionReceiptProcessor extends TransactionReceiptProcessor {
    private Eea eea;
    private String enclavePublicKey;

    public PrivateTransactionReceiptProcessor(Eea eea, String enclavePublicKey) {
        super(eea);
        this.eea = eea;
        this.enclavePublicKey = enclavePublicKey;
    }

    @Override
    Optional<PrivateTransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws IOException, TransactionException {
        EeaGetTransactionReceipt transactionReceipt =
                eea.eeaGetTransactionReceipt(transactionHash, enclavePublicKey).send();
        if (transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: "
                    + transactionReceipt.getError().getMessage());
        }

        return transactionReceipt.getTransactionReceipt();
    }
}
