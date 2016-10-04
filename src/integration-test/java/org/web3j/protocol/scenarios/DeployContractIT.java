package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthCall;
import org.web3j.protocol.core.methods.request.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;
import org.web3j.protocol.parity.methods.response.PersonalUnlockAccount;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
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

        String transactionHash = sendTransaction();
//        String transactionHash = "0xb4820c8369a4061187d18a2114919ca2357996923c6d59ffa024a71cc9ce3ad8";
        assertFalse(transactionHash.isEmpty());

        EthGetTransactionReceipt.TransactionReceipt transactionReceipt =
                waitForTransactionReceipt(transactionHash);

        assertThat(transactionReceipt.getTransactionHash(), is(transactionHash));
        Optional<String> contractAddressOptional = transactionReceipt.getContractAddress();

        assertTrue(contractAddressOptional.isPresent());
        String contractAddress = contractAddressOptional.get();

        Function function = createFibonacciFunction();

        String responseValue = callSmartContractFunction(function, contractAddress);
        assertFalse(responseValue.isEmpty());

        List<Uint> uint = FunctionReturnDecoder.decode(responseValue, function.getOutputParameters());
        assertThat(uint.size(), is(1));
        assertThat(uint.get(0).getValue(), equalTo(BigInteger.valueOf(55)));
    }

    private String sendTransaction() throws Exception {
        EthSendTransaction ethSendTransaction = new EthSendTransaction(
                ADDRESS,
                BigInteger.valueOf(500000),  // See contents of fibonacci.bin
                "6060604052610152806100126000396000f360606040526000357c0100000000000000000000000000000000000000000000000000000000900480633c7fdc701461004757806361047ff41461007857610042565b610002565b346100025761006260048080359060200190919050506100a9565b6040518082815260200191505060405180910390f35b346100025761009360048080359060200190919050506100fd565b6040518082815260200191505060405180910390f35b60006100b4826100fd565b905080507f71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed8282604051808381526020018281526020019250505060405180910390a15b919050565b60006000821415610115576000905061014d5661014c565b600182141561012b576001905061014d5661014b565b610137600283036100fd565b610143600184036100fd565b01905061014d565b5b5b91905056"
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
