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
package org.web3j.tx.response;

import java.io.IOException;
import java.util.Optional;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

/** With each provided transaction hash, poll until we obtain a transaction receipt. */
public class PollingTransactionReceiptProcessor extends TransactionReceiptProcessor {

    protected final long sleepDuration;
    protected final int attempts;

    public PollingTransactionReceiptProcessor(Web3j web3j, long sleepDuration, int attempts) {
        super(web3j);
        this.sleepDuration = sleepDuration;
        this.attempts = attempts;
    }

    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash)
            throws IOException, TransactionException {

        return getTransactionReceipt(transactionHash, sleepDuration, attempts);
    }

    private TransactionReceipt getTransactionReceipt(
            String transactionHash, long sleepDuration, int attempts)
            throws IOException, TransactionException {

        for (int i = 0; i < attempts; i++) {
            Optional<? extends TransactionReceipt> receiptOptional =
                    sendTransactionReceiptRequest(transactionHash);

            if (receiptOptional.isPresent()) {
                return receiptOptional.get();
            }

            // Sleep unless it is the last attempt.
            if (i < attempts - 1) {
                try {
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    throw new TransactionException(e);
                }
            }
        }

        throw new TransactionException(
                "Transaction receipt was not generated after "
                        + ((sleepDuration * attempts) / 1000
                                + " seconds for transaction: "
                                + transactionHash),
                transactionHash);
    }
}
