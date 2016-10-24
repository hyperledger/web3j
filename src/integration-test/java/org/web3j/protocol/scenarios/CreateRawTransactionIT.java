package org.web3j.protocol.scenarios;

import java.math.BigInteger;

import org.junit.Test;

import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Create, sign and send a raw transaction.
 */
public class CreateRawTransactionIT extends Scenario {

    // testnet
    private static final String PRIVATE_KEY = "";  // 32 byte hex value
    private static final String PUBLIC_KEY = "0x";  // 64 byte hex value

    private static final String TO_ADDRESS = "0x";  // 20 byte hex value

    private static final ECKeyPair KEY_PAIR = new ECKeyPair(
            Numeric.toBigInt(PRIVATE_KEY), Numeric.toBigInt(PUBLIC_KEY));

    @Test
    public void testTransferEther() throws Exception {
        BigInteger nonce = getNonce(WALLET_ADDRESS);
        RawTransaction rawTransaction = createEtherTransaction(
                nonce, TO_ADDRESS);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, KEY_PAIR);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction =
                parity.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();

        assertFalse(transactionHash.isEmpty());

        EthGetTransactionReceipt.TransactionReceipt transactionReceipt =
                waitForTransactionReceipt(transactionHash);

        assertThat(transactionReceipt.getTransactionHash(), is(transactionHash));
    }

    @Test
    public void testDeploySmartContract() throws Exception {
        BigInteger nonce = getNonce(WALLET_ADDRESS);
        RawTransaction rawTransaction = createSmartContractTransaction(nonce);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, KEY_PAIR);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction =
                parity.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();

        assertFalse(transactionHash.isEmpty());

        EthGetTransactionReceipt.TransactionReceipt transactionReceipt =
                waitForTransactionReceipt(transactionHash);

        assertThat(transactionReceipt.getTransactionHash(), is(transactionHash));

        assertFalse("Contract execution ran out of gas",
                rawTransaction.getGasLimit().equals(transactionReceipt.getGasUsed()));
    }

    private static RawTransaction createEtherTransaction(BigInteger nonce, String toAddress) {
        BigInteger value = Convert.toWei("0.5", Convert.Unit.ETHER).toBigInteger();

        return RawTransaction.createEtherTransaction(
                nonce, GAS_PRICE, GAS_LIMIT, toAddress, value);
    }

    private static RawTransaction createSmartContractTransaction(BigInteger nonce) {
        return RawTransaction.createContractTransaction(
                nonce, GAS_PRICE, GAS_LIMIT, BigInteger.ZERO, getFibonacciSolidityBinary());
    }

    private BigInteger getNonce(String address) throws Exception {
        EthGetTransactionCount ethGetTransactionCount = parity.ethGetTransactionCount(
                address, DefaultBlockParameterName.LATEST).sendAsync().get();

        return ethGetTransactionCount.getTransactionCount();
    }
}
