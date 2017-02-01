package org.web3j.tx;


import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Async;
import org.web3j.utils.Convert;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferTest extends ManagedTransactionTester {

    private TransactionReceipt transactionReceipt;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        transactionReceipt = prepareTransfer();
    }

    @Test
    public void testSendFunds() throws Exception {
        assertThat(Transfer.sendFunds(web3j, SampleKeys.CREDENTIALS, ADDRESS,
                BigDecimal.TEN, Convert.Unit.ETHER),
                is(transactionReceipt));
    }

    @Test
    public void testSendFundsAsync() throws  Exception {
        assertThat(Transfer.sendFundsAsync(web3j, SampleKeys.CREDENTIALS, ADDRESS,
                BigDecimal.TEN, Convert.Unit.ETHER).get(),
                is(transactionReceipt));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTransferInvalidValue() throws Exception {
        Transfer.sendFunds(web3j, SampleKeys.CREDENTIALS, ADDRESS,
                new BigDecimal(0.1), Convert.Unit.WEI);
    }

    private TransactionReceipt prepareTransfer() {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        prepareTransaction(transactionReceipt);

        EthGasPrice ethGasPrice = new EthGasPrice();
        ethGasPrice.setResult("0x1");

        Request gasPriceRequest = mock(Request.class);
        when(gasPriceRequest.sendAsync()).thenReturn(Async.run(() -> ethGasPrice));
        when(web3j.ethGasPrice()).thenReturn(gasPriceRequest);

        return transactionReceipt;
    }
}
