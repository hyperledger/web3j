package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Optional;

import org.junit.Before;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;
import org.web3j.protocol.parity.methods.response.PersonalUnlockAccount;

import static junit.framework.TestCase.fail;

/**
 * Common methods & settings used accross scenarios.
 */
public class Scenario {
    static final String ADDRESS = "";
    private static final String PASSWORD = "";

    private static final BigInteger ACCOUNT_UNLOCK_DURATION = BigInteger.valueOf(30);

    private static final int SLEEP_DURATION = 10000;
    private static final int ATTEMPTS = 20;

    Parity parity;

    public Scenario() {
        System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
    }

    @Before
    public void setUp() {
        this.parity = Parity.build(new HttpService());
    }

    boolean unlockAccount() throws Exception {
        PersonalUnlockAccount personalUnlockAccount =
                parity.personalUnlockAccount(ADDRESS, PASSWORD, ACCOUNT_UNLOCK_DURATION)
                        .sendAsync().get();
        return personalUnlockAccount.accountUnlocked();
    }

    EthGetTransactionReceipt.TransactionReceipt waitForTransactionReceipt(
            String transactionHash) throws Exception {

        Optional<EthGetTransactionReceipt.TransactionReceipt> transactionReceiptOptional =
                getTransactionReceipt(transactionHash, SLEEP_DURATION, ATTEMPTS);

        if (!transactionReceiptOptional.isPresent()) {
            fail("Transaction reciept not generated after " + ATTEMPTS + " attempts");
        }

        return transactionReceiptOptional.get();
    }

    private Optional<EthGetTransactionReceipt.TransactionReceipt> getTransactionReceipt(
            String transactionHash, int sleepDuration, int attempts) throws Exception {

        Optional<EthGetTransactionReceipt.TransactionReceipt> receiptOptional =
                sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!receiptOptional.isPresent()) {
                Thread.sleep(sleepDuration);
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                break;
            }
        }

        return receiptOptional;
    }

    private Optional<EthGetTransactionReceipt.TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws Exception {
        EthGetTransactionReceipt transactionReceipt =
                parity.ethGetTransactionReceipt(transactionHash).sendAsync().get();

        return transactionReceipt.getTransactionReceipt();
    }

    Function createFibonacciFunction() {
        return new Function<>(
                "fibonacciNotify",
                Collections.singletonList(new Uint(BigInteger.valueOf(7))),
                Collections.singletonList(new TypeReference<Uint>() {}));
    }

    static String getFibonacciSolidityBinary() {  // See contents of fibonacci.bin
        return "6060604052610152806100126000396000f360606040526000357c0100000000000000000000000000000000000000000000000000000000900480633c7fdc701461004757806361047ff41461007857610042565b610002565b346100025761006260048080359060200190919050506100a9565b6040518082815260200191505060405180910390f35b346100025761009360048080359060200190919050506100fd565b6040518082815260200191505060405180910390f35b60006100b4826100fd565b905080507f71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed8282604051808381526020018281526020019250505060405180910390a15b919050565b60006000821415610115576000905061014d5661014c565b600182141561012b576001905061014d5661014b565b610137600283036100fd565b610143600184036100fd565b01905061014d565b5b5b91905056";
    }
}
