package org.web3j.tx;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TransferTest extends ManagedTransactionTester {

    protected TransactionReceipt transactionReceipt;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        transactionReceipt = prepareTransfer();
    }

    @Test
    public void testSendFunds() throws Exception {
        assertThat(
                sendFunds(SampleKeys.CREDENTIALS, ADDRESS, BigDecimal.TEN, Convert.Unit.ETHER),
                is(transactionReceipt));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTransferInvalidValue() throws Exception {
        sendFunds(SampleKeys.CREDENTIALS, ADDRESS, new BigDecimal(0.1), Convert.Unit.WEI);
    }

    protected TransactionReceipt sendFunds(
            Credentials credentials, String toAddress, BigDecimal value, Convert.Unit unit)
            throws Exception {
        return new Transfer(web3j, getVerifiedTransactionManager(credentials))
                .sendFunds(toAddress, value, unit)
                .send();
    }
}
