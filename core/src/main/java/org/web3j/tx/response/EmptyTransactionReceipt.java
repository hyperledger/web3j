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

import java.math.BigInteger;
import java.util.List;

import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * An empty transaction receipt object containing only the transaction hash. This is to support the
 * {@link QueuingTransactionReceiptProcessor} and {@link NoOpProcessor}.
 */
public class EmptyTransactionReceipt extends TransactionReceipt {

    public EmptyTransactionReceipt(String transactionHash) {
        super();
        this.setTransactionHash(transactionHash);
    }

    @Override
    public String getTransactionHash() {
        return super.getTransactionHash();
    }

    @Override
    public void setTransactionHash(String transactionHash) {
        super.setTransactionHash(transactionHash);
    }

    private UnsupportedOperationException unsupportedOperation() {
        return new UnsupportedOperationException(
                "Empty transaction receipt, only transaction hash is available");
    }

    @Override
    public BigInteger getTransactionIndex() {
        throw unsupportedOperation();
    }

    @Override
    public String getTransactionIndexRaw() {
        throw unsupportedOperation();
    }

    @Override
    public void setTransactionIndex(String transactionIndex) {
        throw unsupportedOperation();
    }

    @Override
    public String getBlockHash() {
        throw unsupportedOperation();
    }

    @Override
    public void setBlockHash(String blockHash) {
        throw unsupportedOperation();
    }

    @Override
    public BigInteger getBlockNumber() {
        throw unsupportedOperation();
    }

    @Override
    public String getBlockNumberRaw() {
        throw unsupportedOperation();
    }

    @Override
    public void setBlockNumber(String blockNumber) {
        throw unsupportedOperation();
    }

    @Override
    public BigInteger getCumulativeGasUsed() {
        throw unsupportedOperation();
    }

    @Override
    public String getCumulativeGasUsedRaw() {
        throw unsupportedOperation();
    }

    @Override
    public void setCumulativeGasUsed(String cumulativeGasUsed) {
        throw unsupportedOperation();
    }

    @Override
    public BigInteger getGasUsed() {
        throw unsupportedOperation();
    }

    @Override
    public String getGasUsedRaw() {
        throw unsupportedOperation();
    }

    @Override
    public void setGasUsed(String gasUsed) {
        throw unsupportedOperation();
    }

    @Override
    public String getContractAddress() {
        throw unsupportedOperation();
    }

    @Override
    public void setContractAddress(String contractAddress) {
        throw unsupportedOperation();
    }

    @Override
    public String getRoot() {
        throw unsupportedOperation();
    }

    @Override
    public void setRoot(String root) {
        throw unsupportedOperation();
    }

    @Override
    public String getStatus() {
        throw unsupportedOperation();
    }

    @Override
    public void setStatus(String status) {
        throw unsupportedOperation();
    }

    @Override
    public String getFrom() {
        throw unsupportedOperation();
    }

    @Override
    public void setFrom(String from) {
        throw unsupportedOperation();
    }

    @Override
    public String getTo() {
        throw unsupportedOperation();
    }

    @Override
    public void setTo(String to) {
        throw unsupportedOperation();
    }

    @Override
    public List<Log> getLogs() {
        throw unsupportedOperation();
    }

    @Override
    public void setLogs(List<Log> logs) {
        throw unsupportedOperation();
    }

    @Override
    public String getLogsBloom() {
        throw unsupportedOperation();
    }

    @Override
    public void setLogsBloom(String logsBloom) {
        throw unsupportedOperation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionReceipt)) {
            return false;
        }

        TransactionReceipt that = (TransactionReceipt) o;

        return getTransactionHash() != null
                ? getTransactionHash().equals(that.getTransactionHash())
                : that.getTransactionHash() == null;
    }

    @Override
    public int hashCode() {
        return getTransactionHash() != null ? getTransactionHash().hashCode() : 0;
    }
}
