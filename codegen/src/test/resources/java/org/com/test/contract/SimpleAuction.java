package org.com.test.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.11.
 */
@SuppressWarnings("rawtypes")
public class SimpleAuction extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b5060405160408061054d83398101604052805160209091015160008054600160a060020a03909216600160a060020a031990921691909117905542016001556104ef8061005e6000396000f3006080604052600436106100775763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631998aeef811461007c5780632a24f46c1461008657806338af3eed1461009b5780633ccfd60b146100d957806391f9015714610102578063d57bde7914610117575b600080fd5b61008461013e565b005b34801561009257600080fd5b50610084610290565b3480156100a757600080fd5b506100b061041b565b6040805173ffffffffffffffffffffffffffffffffffffffff9092168252519081900360200190f35b3480156100e557600080fd5b506100ee610437565b604080519115158252519081900360200190f35b34801561010e57600080fd5b506100b06104a1565b34801561012357600080fd5b5061012c6104bd565b60408051918252519081900360200190f35b600154421115610198576040805160e560020a62461bcd02815260206004820152601660248201527f41756374696f6e20616c726561647920656e6465642e00000000000000000000604482015290519081900360640190fd5b60035434116101f1576040805160e560020a62461bcd02815260206004820152601e60248201527f546865726520616c7265616479206973206120686967686572206269642e0000604482015290519081900360640190fd5b6003541561022b5760035460025473ffffffffffffffffffffffffffffffffffffffff166000908152600460205260409020805490910190555b6002805473ffffffffffffffffffffffffffffffffffffffff19163390811790915534600381905560408051928352602083019190915280517ff4757a49b326036464bec6fe419a4ae38c8a02ce3e68bf0809674f6aab8ad3009281900390910190a1565b6001544210156102ea576040805160e560020a62461bcd02815260206004820152601660248201527f41756374696f6e206e6f742079657420656e6465642e00000000000000000000604482015290519081900360640190fd5b60055460ff161561036b576040805160e560020a62461bcd02815260206004820152602360248201527f61756374696f6e456e642068617320616c7265616479206265656e2063616c6c60448201527f65642e0000000000000000000000000000000000000000000000000000000000606482015290519081900360840190fd5b6005805460ff191660011790556002546003546040805173ffffffffffffffffffffffffffffffffffffffff9093168352602083019190915280517fdaec4582d5d9595688c8c98545fdd1c696d41c6aeaeb636737e84ed2f5c00eda9281900390910190a16000805460035460405173ffffffffffffffffffffffffffffffffffffffff9092169281156108fc029290818181858888f19350505050158015610418573d6000803e3d6000fd5b50565b60005473ffffffffffffffffffffffffffffffffffffffff1681565b336000908152600460205260408120548181111561049857336000818152600460205260408082208290555183156108fc0291849190818181858888f19350505050151561049857336000908152600460205260408120829055915061049d565b600191505b5090565b60025473ffffffffffffffffffffffffffffffffffffffff1681565b600354815600a165627a7a7230582034644db040bf7744af37a959bf2a4311a0bfa919afdef1a1a3107bc46bb985ec0029";

    public static final String FUNC_BID = "bid";

    public static final String FUNC_AUCTIONEND = "auctionEnd";

    public static final String FUNC_BENEFICIARY = "beneficiary";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_HIGHESTBIDDER = "highestBidder";

    public static final String FUNC_HIGHESTBID = "highestBid";

    public static final Event HIGHESTBIDINCREASED_EVENT = new Event("HighestBidIncreased", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event AUCTIONENDED_EVENT = new Event("AuctionEnded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected SimpleAuction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SimpleAuction(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SimpleAuction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SimpleAuction(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> bid(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_BID, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> auctionEnd() {
        final Function function = new Function(
                FUNC_AUCTIONEND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> beneficiary() {
        final Function function = new Function(FUNC_BENEFICIARY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> withdraw() {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> highestBidder() {
        final Function function = new Function(FUNC_HIGHESTBIDDER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> highestBid() {
        final Function function = new Function(FUNC_HIGHESTBID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<HighestBidIncreasedEventResponse> getHighestBidIncreasedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(HIGHESTBIDINCREASED_EVENT, transactionReceipt);
        ArrayList<HighestBidIncreasedEventResponse> responses = new ArrayList<HighestBidIncreasedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            HighestBidIncreasedEventResponse typedResponse = new HighestBidIncreasedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.bidder = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<HighestBidIncreasedEventResponse> highestBidIncreasedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, HighestBidIncreasedEventResponse>() {
            @Override
            public HighestBidIncreasedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(HIGHESTBIDINCREASED_EVENT, log);
                HighestBidIncreasedEventResponse typedResponse = new HighestBidIncreasedEventResponse();
                typedResponse.log = log;
                typedResponse.bidder = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<HighestBidIncreasedEventResponse> highestBidIncreasedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(HIGHESTBIDINCREASED_EVENT));
        return highestBidIncreasedEventFlowable(filter);
    }

    public List<AuctionEndedEventResponse> getAuctionEndedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(AUCTIONENDED_EVENT, transactionReceipt);
        ArrayList<AuctionEndedEventResponse> responses = new ArrayList<AuctionEndedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AuctionEndedEventResponse typedResponse = new AuctionEndedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AuctionEndedEventResponse> auctionEndedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AuctionEndedEventResponse>() {
            @Override
            public AuctionEndedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(AUCTIONENDED_EVENT, log);
                AuctionEndedEventResponse typedResponse = new AuctionEndedEventResponse();
                typedResponse.log = log;
                typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AuctionEndedEventResponse> auctionEndedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(AUCTIONENDED_EVENT));
        return auctionEndedEventFlowable(filter);
    }

    @Deprecated
    public static SimpleAuction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleAuction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SimpleAuction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleAuction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SimpleAuction load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SimpleAuction(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SimpleAuction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SimpleAuction(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SimpleAuction> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _biddingTime, String _beneficiary) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_biddingTime), 
                new org.web3j.abi.datatypes.Address(160, _beneficiary)));
        return deployRemoteCall(SimpleAuction.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<SimpleAuction> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _biddingTime, String _beneficiary) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_biddingTime), 
                new org.web3j.abi.datatypes.Address(160, _beneficiary)));
        return deployRemoteCall(SimpleAuction.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<SimpleAuction> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _biddingTime, String _beneficiary) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_biddingTime), 
                new org.web3j.abi.datatypes.Address(160, _beneficiary)));
        return deployRemoteCall(SimpleAuction.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<SimpleAuction> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _biddingTime, String _beneficiary) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_biddingTime), 
                new org.web3j.abi.datatypes.Address(160, _beneficiary)));
        return deployRemoteCall(SimpleAuction.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class HighestBidIncreasedEventResponse extends BaseEventResponse {
        public String bidder;

        public BigInteger amount;
    }

    public static class AuctionEndedEventResponse extends BaseEventResponse {
        public String winner;

        public BigInteger amount;
    }
}
