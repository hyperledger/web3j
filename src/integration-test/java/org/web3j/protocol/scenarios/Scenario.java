package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Optional;

import org.junit.Before;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;
import org.web3j.protocol.parity.methods.response.PersonalUnlockAccount;

import static junit.framework.TestCase.fail;

/**
 * Common methods & settings used accross scenarios.
 */
public class Scenario {

    static final BigInteger GAS_PRICE = BigInteger.valueOf(50_000_000_000L);
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(2_000_000);

    static final String WALLET_ADDRESS = "0x";  // 20 byte hex address - must have 0x prefix
    private static final String WALLET_PASSWORD = "";

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
                parity.personalUnlockAccount(WALLET_ADDRESS, WALLET_PASSWORD, ACCOUNT_UNLOCK_DURATION)
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

    Function createSimpleStringFunction() {
        return new Function<>(
                "simple",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Utf8String>() {}));
    }

    static String getStringSolidityBinary() {  // See contents of string.bin
        return "6060604052610326806100126000396000f360606040526000357c010000000000000000000000000000000000000000000000000000000090048063df201a4614610052578063e39d4588146100d2578063f2a75fe4146101525761004d565b610002565b346100025761006460048050506101d2565b60405180806020018281038252838181518152602001915080519060200190808383829060006004602084601f0104600302600f01f150905090810190601f1680156100c45780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34610002576100e4600480505061022a565b60405180806020018281038252838181518152602001915080519060200190808383829060006004602084601f0104600302600f01f150905090810190601f1680156101445780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b346100025761016460048050506102f4565b60405180806020018281038252838181518152602001915080519060200190808383829060006004602084601f0104600302600f01f150905090810190601f1680156101c45780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6020604051908101604052806000815260200150604060405190810160405280600d81526020017f6f6e65206d6f72652074696d65000000000000000000000000000000000000008152602001509050610227565b90565b602060405190810160405280600081526020015060a060405190810160405280607b81526020017f4c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e7381526020017f656374657475722061646970697363696e6720656c69742c2073656420646f2081526020017f656975736d6f642074656d706f7220696e6369646964756e74207574206c616281526020017f6f726520657420646f6c6f7265206d61676e6120616c697175612e000000000081526020015090506102f1565b90565b602060405190810160405280600081526020015060206040519081016040528060008152602001509050610323565b9056";
    }
}
