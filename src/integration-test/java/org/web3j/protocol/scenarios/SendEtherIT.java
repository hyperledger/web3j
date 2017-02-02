package org.web3j.protocol.scenarios;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Simple integration test to demonstrate sending of Ether between parties.
 */
public class SendEtherIT extends Scenario {

    @Test
    public void testTransferEther() throws Exception {
        unlockAccount();

        BigInteger nonce = getNonce(ALICE.getAddress());
        BigInteger value = Convert.toWei("0.5", Convert.Unit.ETHER).toBigInteger();

        Transaction transaction = Transaction.createEtherTransaction(
                ALICE.getAddress(), nonce, GAS_PRICE, GAS_LIMIT, BOB.getAddress(), value);

        EthSendTransaction ethSendTransaction = parity.ethSendTransaction(transaction).sendAsync().get();

        String transactionHash = ethSendTransaction.getTransactionHash();

        assertFalse(transactionHash.isEmpty());

        TransactionReceipt transactionReceipt =
                waitForTransactionReceipt(transactionHash);

        assertThat(transactionReceipt.getTransactionHash(), is(transactionHash));
    }

    /*
    Valid transaction receipt:
    "{"jsonrpc":"2.0",
        "id":1,
        "result":{
           "blockHash":"0x35a865cf2ba4efc3642b17a651f9e896dfebcdea39bfd0741b6f629e1be31a27",
           "blockNumber":"0x1c155f",
           "contractAddress":null,
           "cumulativeGasUsed":"0x5208",
           "from":"0x19e03255f667bdfd50a32722df860b1eeaf4d635",
           "gasUsed":"0x5208",
           "logs":[

           ],
           "root":"327e1e81c85cb710fe81cb8c0f824e9e49c3bf200e5e1149f589140145df10e3",
           "to":"0x9c98e381edc5fe1ac514935f3cc3edaa764cf004",
           "transactionHash":"0x16e41aa9d97d1c3374a4cb9599febdb24d4d5648b607c99e01a8e79e3eab2c34",
           "transactionIndex":"0x0"
        }
     */
    @Test
    public void testTransfer() throws Exception {
        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                parity, ALICE, BOB.getAddress(), BigDecimal.valueOf(0.2), Convert.Unit.ETHER);
        assertFalse(transactionReceipt.getBlockHash().isEmpty());
    }
}
