package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthCall;
import org.web3j.protocol.core.methods.request.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Integration test demonstrating the full contract deployment workflow.
 */
public class DeployContractIT extends Scenario {

    @Test
    public void testContractCreation() throws Exception {
        boolean accountUnlocked = unlockAccount();
        assertTrue(accountUnlocked);

        BigInteger gas = BigInteger.valueOf(500000);

        String transactionHash = sendTransaction(gas);
        assertFalse(transactionHash.isEmpty());

        EthGetTransactionReceipt.TransactionReceipt transactionReceipt =
                waitForTransactionReceipt(transactionHash);

        assertThat(transactionReceipt.getTransactionHash(), is(transactionHash));

        assertFalse("Contract execution ran out of gas",
                transactionReceipt.getGasUsed().equals(gas));

        Optional<String> contractAddressOptional = transactionReceipt.getContractAddress();

        assertTrue(contractAddressOptional.isPresent());
        String contractAddress = contractAddressOptional.get();

        Function function = createFibonacciFunction();

        String responseValue = callSmartContractFunction(function, contractAddress);
        assertFalse(responseValue.isEmpty());

        List<Uint> uint = FunctionReturnDecoder.decode(
                responseValue, function.getOutputParameters());
        assertThat(uint.size(), is(1));
        assertThat(uint.get(0).getValue(), equalTo(BigInteger.valueOf(13)));
    }

    private String sendTransaction(BigInteger gas) throws Exception {
        EthSendTransaction ethSendTransaction = new EthSendTransaction(
                WALLET_ADDRESS,
                gas,
                getFibonacciSolidityBinary()
        );

        org.web3j.protocol.core.methods.response.EthSendTransaction
                transactionResponse = parity.ethSendTransaction(ethSendTransaction)
                .sendAsync().get();

        return transactionResponse.getTransactionHash();
    }

    private String callSmartContractFunction(
            Function function, String contractAddress) throws Exception {

        String encodedFunction = FunctionEncoder.encode(function);

        org.web3j.protocol.core.methods.response.EthCall response = parity.ethCall(
                new EthCall(contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        return response.getValue();
    }
}
