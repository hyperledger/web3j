package org.web3j.protocol.scenarios;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.TestParameters;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.fail;
import static org.junit.Assume.assumeFalse;
import static org.web3j.protocol.core.TestParameters.isInfuraTestRinkebyUrl;

/**
 * Common methods & settings used accross scenarios.
 */
public class Scenario {

    static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

    // testnet
    private static final String WALLET_PASSWORD = "";

    /*
    If you want to use regular Ethereum wallet addresses, provide a WALLET address variable
    "0x..." // 20 bytes (40 hex characters) & replace instances of ALICE.getAddress() with this
    WALLET address variable you've defined.
    */
    static final Credentials ALICE = Credentials.create(
            TestParameters.ALICE_PRIVKEY, // 32 byte hex value
            TestParameters.ALICE_PUBKEY // 64 byte hex value
    );

    static final Credentials BOB = Credentials.create(
            TestParameters.BOB_PRIVKEY, // 32 byte hex value
            TestParameters.BOB_PUBKEY // 64 byte hex value
    );

    private static final BigInteger ACCOUNT_UNLOCK_DURATION = BigInteger.valueOf(30);

    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;

    Admin web3j;

    public Scenario() { }

    @Before
    public void setUp() {
        this.web3j = Admin.build(new HttpService(TestParameters.TEST_RINKEBY_URL));
    }

    boolean unlockAccount() throws Exception {
        assumeFalse("Infura does NOT support personal_unlockAccount - "
                + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());

        PersonalUnlockAccount personalUnlockAccount =
                web3j.personalUnlockAccount(
                        ALICE.getAddress(), WALLET_PASSWORD, ACCOUNT_UNLOCK_DURATION)
                        .sendAsync().get();
        return personalUnlockAccount.accountUnlocked();
    }

    TransactionReceipt waitForTransactionReceipt(
            String transactionHash) throws Exception {

        Optional<TransactionReceipt> transactionReceiptOptional =
                getTransactionReceipt(transactionHash, SLEEP_DURATION, ATTEMPTS);

        if (!transactionReceiptOptional.isPresent()) {
            fail("Transaction receipt not generated after " + ATTEMPTS + " attempts");
        }

        return transactionReceiptOptional.get();
    }

    private Optional<TransactionReceipt> getTransactionReceipt(
            String transactionHash,
            @SuppressWarnings("SameParameterValue") int sleepDuration,
            @SuppressWarnings("SameParameterValue") int attempts) throws Exception {

        Optional<TransactionReceipt> receiptOptional =
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

    private Optional<TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws Exception {
        EthGetTransactionReceipt transactionReceipt =
                web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();

        return transactionReceipt.getTransactionReceipt();
    }

    BigInteger getNonce(String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                address, DefaultBlockParameterName.LATEST).sendAsync().get();

        return ethGetTransactionCount.getTransactionCount();
    }

    Function createFibonacciFunction() {
        return new Function(
                "fibonacciNotify",
                Collections.singletonList(new Uint(BigInteger.valueOf(7))),
                Collections.singletonList(new TypeReference<Uint>() {}));
    }


    static String load(String filePath) throws IOException {
        InputStream fileStream = Scenario.class.getResourceAsStream(filePath);
        byte[] allBytes = new byte[fileStream.available()];
        @SuppressWarnings("unused") int read = fileStream.read(allBytes);
        return new String(allBytes);
    }

    static String getFibonacciSolidityBinary() throws Exception {
        return load("/solidity/fibonacci/build/Fibonacci.bin");
    }

    @SuppressWarnings("WeakerAccess")
    public static List<Object[]> transferTestParameters() {
        return asList(
            new Object[] { "From ALICE to BOB", ALICE, BOB },
            new Object[] { "From BOB to ALICE", BOB, ALICE }
        );
    }

}
