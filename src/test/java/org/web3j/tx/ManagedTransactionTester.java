package org.web3j.tx;

import java.util.concurrent.Callable;

import org.junit.Before;

import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Async;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public abstract class ManagedTransactionTester {

    static final String ADDRESS = "0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a";
    static final String TRANSACTION_HASH = "0xHASH";
    protected Web3j web3j;

    @Before
    public void setUp() {
        web3j = mock(Web3j.class);
    }

    void prepareTransaction(TransactionReceipt transactionReceipt) {
        prepareNonceRequest();
        prepareTransactionRequest();
        prepareTransactionReceipt(transactionReceipt);
    }

    void prepareNonceRequest() {
        final EthGetTransactionCount ethGetTransactionCount = new EthGetTransactionCount();
        ethGetTransactionCount.setResult("0x1");

        Request transactionCountRequest = mock(Request.class);
        when(transactionCountRequest.sendAsync())
                .thenReturn(Async.run(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return ethGetTransactionCount;
                    }
                }));
        when(web3j.ethGetTransactionCount(SampleKeys.ADDRESS, DefaultBlockParameterName.LATEST))
                .thenReturn(transactionCountRequest);
    }

    void prepareTransactionRequest() {
        final EthSendTransaction ethSendTransaction = new EthSendTransaction();
        ethSendTransaction.setResult(TRANSACTION_HASH);

        Request rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.sendAsync()).thenReturn(Async.run(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return ethSendTransaction;
            }
        }));
        when(web3j.ethSendRawTransaction(any(String.class)))
                .thenReturn(rawTransactionRequest);
    }

    void prepareTransactionReceipt(TransactionReceipt transactionReceipt) {
        final EthGetTransactionReceipt ethGetTransactionReceipt = new EthGetTransactionReceipt();
        ethGetTransactionReceipt.setResult(transactionReceipt);

        Request getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.sendAsync())
                .thenReturn(Async.run(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return ethGetTransactionReceipt;
                    }
                }));
        when(web3j.ethGetTransactionReceipt(TRANSACTION_HASH))
                .thenReturn(getTransactionReceiptRequest);
    }
}
