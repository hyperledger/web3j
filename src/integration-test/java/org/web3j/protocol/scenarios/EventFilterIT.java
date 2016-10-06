package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Filter scenario integration tests.
 */
public class EventFilterIT extends Scenario {

    // Deployed Fibonacci contract instance in testnet
    private final String CONTRACT_ADDRESS = "0x3c05b2564139fb55820b18b72e94b2178eaace7d";

    @Test
    public void testEventFilter() throws Exception {
        unlockAccount();

        String transactionHash = sendTransaction();

        EthGetTransactionReceipt.TransactionReceipt transactionReceipt =
                waitForTransactionReceipt(transactionHash);

        List<Log> logs = transactionReceipt.getLogs();
        assertFalse(logs.isEmpty());

        Log log = logs.get(0);

        List<String> topics = log.getTopics();
        assertThat(topics.size(), is(1));

        Event event = new Event<>("Notify",
                Collections.emptyList(),
                Arrays.asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));

        // check function signature - we only have a single topic our event signature,
        // there are no indexed parameters in this example
        String encodedEventSignature = EventEncoder.encode(event);
        assertThat(topics.get(0),
                is(encodedEventSignature));

        // verify our two event parameters
        List<Type> results = FunctionReturnDecoder.decode(
                log.getData(), event.getNonIndexedParameters());
        assertThat(results, equalTo(Arrays.asList(
                new Uint256(BigInteger.valueOf(7)), new Uint256(BigInteger.valueOf(13)))));

        // finally check it shows up in the event filter
        List<EthLog.LogResult> filterLogs = createFilterForEvent(encodedEventSignature);
        assertFalse(filterLogs.isEmpty());
    }

    private String sendTransaction() throws Exception {
        Function function = createFibonacciFunction();

        String encodedFunction = FunctionEncoder.encode(function);

        EthEstimateGas ethEstimateGas = parity.ethEstimateGas(new EthCall(null, encodedFunction))
                .sendAsync().get();
        // this was coming back as 50,000,000 which is > the block gas limit of 4,712,388 - see eth.getBlock("latest")
        BigInteger requiredGas = ethEstimateGas.getAmountUsed().divide(BigInteger.valueOf(100));

        EthSendTransaction ethSendTransaction = new EthSendTransaction(
                ADDRESS, CONTRACT_ADDRESS, requiredGas, encodedFunction);

        org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse =
                parity.ethSendTransaction(ethSendTransaction).sendAsync().get();

        assertFalse(transactionResponse.hasError());

        return transactionResponse.getTransactionHash();
    }

    private List<EthLog.LogResult> createFilterForEvent(String encodedEventSignature) throws Exception {
        EthFilter ethFilter = new EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                CONTRACT_ADDRESS
        );

        ethFilter.addSingleTopic(encodedEventSignature);

        EthLog ethLog = parity.ethGetLogs(ethFilter).send();
        return ethLog.getLogs();
    }
}
