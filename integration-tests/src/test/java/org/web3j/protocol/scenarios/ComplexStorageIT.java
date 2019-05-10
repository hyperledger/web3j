package org.web3j.protocol.scenarios;

import org.junit.Before;
import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStructType;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.generated.ComplexStorage;
import org.web3j.generated.SimpleStorage;
import org.web3j.generated.SimpleStruct3;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ComplexStorageIT {

    static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    static final StaticGasProvider STATIC_GAS_PROVIDER =
            new StaticGasProvider(GAS_PRICE, GAS_LIMIT);

    // testnet
    private static final String WALLET_PASSWORD = "";

    /*
    If you want to use regular Ethereum wallet addresses, provide a WALLET address variable
    "0x..." // 20 bytes (40 hex characters) & replace instances of ALICE.getAddress() with this
    WALLET address variable you've defined.
    */
    private Credentials credentials;

    private static final BigInteger ACCOUNT_UNLOCK_DURATION = BigInteger.valueOf(30);

    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;

    Admin web3j;

    @Before
    public void setUp() throws Exception {
        this.web3j = Admin.build(new HttpService("https://rinkeby.infura.io/v3/a1504fcad53249f5a7b8df6bc2e3c3fb"));

        this.credentials = WalletUtils.loadCredentials(
                "hello",
                "/Users/sam/development/work/blk_io/wallets/test/UTC--2019-04-03T09-08-31.959000000Z--fa8d1f97740206b9b785193af60d6c8138b0cadb.json");
    }

    @Test
    public void testDeploySimpleContract() throws Exception {
        final BigInteger value = BigInteger.valueOf(1000L);
        final SimpleStorage simpleStorage = SimpleStorage.deploy(web3j, this.credentials, GAS_PRICE, GAS_LIMIT).send();
        assertNotNull(simpleStorage.set(value).send());
        assertThat(simpleStorage.get().send(), is(value));
    }

    @Test
    public void testDeploySimpleStruct3Contract() throws Exception {
        final SimpleStruct3.TupleClass1 ivaylo = new SimpleStruct3.TupleClass1(BigInteger.valueOf(33), "Ivaylo");
        final SimpleStruct3 simpleStruct3 = SimpleStruct3.deploy(web3j, credentials, new DefaultGasProvider(), ivaylo).send();

        final SimpleStruct3.TupleClass1 ivayloResult = simpleStruct3.getBar().send();
        System.out.println(ivayloResult);

        final SimpleStruct3.TupleClass1 sam = new SimpleStruct3.TupleClass1(BigInteger.valueOf(22), "Sam");
        assertNotNull(simpleStruct3.setBar(sam).send());

        final SimpleStruct3.TupleClass1 result1 = simpleStruct3.getBar().send();
        System.out.println(result1);

        final SimpleStruct3.TupleClass1 tony = new SimpleStruct3.TupleClass1(BigInteger.valueOf(77), "Tony");
        assertNotNull(simpleStruct3.setBar(tony).send());

        final SimpleStruct3.TupleClass1 result2 = simpleStruct3.getBar().send();
        System.out.println(result2);

//        assertNotNull(complexStorage.setBar(bar).send()); // TODO: re-enable once we are done with tuple1
    }

    @Test
    public void testDeployContract() throws Exception {
        final ComplexStorage.TupleClass1 foo = new ComplexStorage.TupleClass1("Sam", "Na");
        final ComplexStorage.TupleClass2 bar = new ComplexStorage.TupleClass2("First", BigInteger.valueOf(100L));

        final ComplexStorage complexStorage = ComplexStorage.deploy(web3j, credentials, new DefaultGasProvider(), foo, bar).send();

        final ComplexStorage.TupleClass1 foo2 = new ComplexStorage.TupleClass1("Ivaylo", "K");
        assertNotNull(complexStorage.setFoo(foo2).send());

        // Now test the decoder.
        complexStorage.getFooBar().send();

//        assertNotNull(complexStorage.setBar(bar).send()); // TODO: re-enable once we are done with tuple1
    }

    boolean unlockAccount() throws Exception {
        PersonalUnlockAccount personalUnlockAccount =
                web3j.personalUnlockAccount(
                        credentials.getAddress(), WALLET_PASSWORD, ACCOUNT_UNLOCK_DURATION)
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
            String transactionHash, int sleepDuration, int attempts) throws Exception {

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
                Collections.singletonList(new TypeReference<Uint>() {
                }));
    }

    static String load(String filePath) throws URISyntaxException, IOException {
        URL url = Scenario.class.getClass().getResource(filePath);
        byte[] bytes = Files.readAllBytes(Paths.get(url.toURI()));
        return new String(bytes);
    }

    static String getFibonacciSolidityBinary() throws Exception {
        return load("/solidity/fibonacci/build/Fibonacci.bin");
    }
}
