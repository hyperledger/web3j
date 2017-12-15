package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

import java.io.IOException;

/**
 * eth_sendTransaction.
 */
public class EthSendTransaction extends Response<EthSendTransaction.SendTransactionResult> {

    public SendTransactionResult getSendTransactionResult() {
        return getResult();
    }

    public boolean isEmpty() {
        return getResult() == null;
    }

    public static class SendTransactionResult {
        private String hash;
        private String status;

        public SendTransactionResult() {
        }

        public SendTransactionResult(String hash, String status) {
            this.hash = hash;
            this.status = status;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) { this.hash = hash; }

        public String getStatus() { return status; }

        public void setStatus(String status) { this.status = status; }
    }
}
