package org.com.test.contract;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.4.0.
 */
public class BlindAuction extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060405160608061075d83398101604090815281516020830151919092015160008054600160a060020a03909216600160a060020a0319909216919091179055429091016001819055016002556106f18061006c6000396000f3006080604052600436106100ae5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166301495c1c81146100b357806312fa6feb146100f05780632a24f46c1461011957806338af3eed146101305780633ccfd60b14610161578063423b217f14610176578063900f080a1461019d57806391f9015714610264578063957bb1e014610279578063a6e6647714610284578063d57bde7914610299575b600080fd5b3480156100bf57600080fd5b506100d7600160a060020a03600435166024356102ae565b6040805192835260208301919091528051918290030190f35b3480156100fc57600080fd5b506101056102e9565b604080519115158252519081900360200190f35b34801561012557600080fd5b5061012e6102f2565b005b34801561013c57600080fd5b506101456103a8565b60408051600160a060020a039092168252519081900360200190f35b34801561016d57600080fd5b5061012e6103b7565b34801561018257600080fd5b5061018b61040d565b60408051918252519081900360200190f35b3480156101a957600080fd5b506040805160206004803580820135838102808601850190965280855261012e95369593946024949385019291829185019084908082843750506040805187358901803560208181028481018201909552818452989b9a998901989297509082019550935083925085019084908082843750506040805187358901803560208181028481018201909552818452989b9a9989019892975090820195509350839250850190849080828437509497506104139650505050505050565b34801561027057600080fd5b506101456105d3565b61012e6004356105e2565b34801561029057600080fd5b5061018b610638565b3480156102a557600080fd5b5061018b61063e565b6004602052816000526040600020818154811015156102c957fe5b600091825260209091206002909102018054600190910154909250905082565b60035460ff1681565b60025442811061030157600080fd5b60035460ff161561031157600080fd5b60055460065460408051600160a060020a039093168352602083019190915280517fdaec4582d5d9595688c8c98545fdd1c696d41c6aeaeb636737e84ed2f5c00eda9281900390910190a16003805460ff1916600117905560008054600654604051600160a060020a039092169281156108fc029290818181858888f193505050501580156103a4573d6000803e3d6000fd5b5050565b600054600160a060020a031681565b336000908152600760205260408120549081111561040a57336000818152600760205260408082208290555183156108fc0291849190818181858888f193505050501580156103a4573d6000803e3d6000fd5b50565b60015481565b6000806000806000806000600154804211151561042f57600080fd5b60025442811161043e57600080fd5b336000908152600460205260409020548c51909950891461045e57600080fd5b8a51891461046b57600080fd5b8951891461047857600080fd5b600096505b88871015610597573360009081526004602052604090208054889081106104a057fe5b906000526020600020906002020195508b878151811015156104be57fe5b906020019060200201518b888151811015156104d657fe5b906020019060200201518b898151811015156104ee57fe5b6020908102909101810151604080518581527f0100000000000000000000000000000000000000000000000000000000000000851515029381019390935260218301829052519182900360410190912089549398509196509450146105525761058c565b8560010154880197508315801561056d575084866001015410155b156105875761057c3386610644565b156105875784880397505b600086555b60019096019561047d565b604051339089156108fc02908a906000818181858888f193505050501580156105c4573d6000803e3d6000fd5b50505050505050505050505050565b600554600160a060020a031681565b6001544281116105f157600080fd5b503360009081526004602090815260408083208151808301909252938152348183019081528454600181810187559585529290932090516002909202019081559051910155565b60025481565b60065481565b6006546000908211610658575060006106bf565b600554600160a060020a03161561068e57600654600554600160a060020a03166000908152600760205260409020805490910190555b5060068190556005805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03841617905560015b929150505600a165627a7a7230582031589b5c38a90750d0758245d4af5ab46f8c92b43d26932fc9021bbfcaea2cc80029";

    public static final String FUNC_BIDS = "bids";

    public static final String FUNC_ENDED = "ended";

    public static final String FUNC_AUCTIONEND = "auctionEnd";

    public static final String FUNC_BENEFICIARY = "beneficiary";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_BIDDINGEND = "biddingEnd";

    public static final String FUNC_REVEAL = "reveal";

    public static final String FUNC_HIGHESTBIDDER = "highestBidder";

    public static final String FUNC_BID = "bid";

    public static final String FUNC_REVEALEND = "revealEnd";

    public static final String FUNC_HIGHESTBID = "highestBid";

    public static final Event AUCTIONENDED_EVENT = new Event("AuctionEnded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected BlindAuction(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BlindAuction(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected BlindAuction(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected BlindAuction(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<Tuple2<byte[], BigInteger>> bids(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_BIDS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0),
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple2<byte[], BigInteger>>(
                new Callable<Tuple2<byte[], BigInteger>>() {
                    @Override
                    public Tuple2<byte[], BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<byte[], BigInteger>(
                                (byte[]) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<Boolean> ended() {
        final Function function = new Function(FUNC_ENDED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> auctionEnd() {
        final Function function = new Function(
                FUNC_AUCTIONEND, 
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> beneficiary() {
        final Function function = new Function(FUNC_BENEFICIARY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> withdraw() {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> biddingEnd() {
        final Function function = new Function(FUNC_BIDDINGEND,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> reveal(List<BigInteger> _values, List<Boolean> _fake, List<byte[]> _secret) {
        final Function function = new Function(
                FUNC_REVEAL, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(_values, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Bool>(
                        org.web3j.abi.datatypes.Bool.class,
                        org.web3j.abi.Utils.typeMap(_fake, org.web3j.abi.datatypes.Bool.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.datatypes.generated.Bytes32.class,
                        org.web3j.abi.Utils.typeMap(_secret, org.web3j.abi.datatypes.generated.Bytes32.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> highestBidder() {
        final Function function = new Function(FUNC_HIGHESTBIDDER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> bid(byte[] _blindedBid, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_BID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_blindedBid)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<BigInteger> revealEnd() {
        final Function function = new Function(FUNC_REVEALEND,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> highestBid() {
        final Function function = new Function(FUNC_HIGHESTBID,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<AuctionEndedEventResponse> getAuctionEndedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(AUCTIONENDED_EVENT, transactionReceipt);
        ArrayList<AuctionEndedEventResponse> responses = new ArrayList<AuctionEndedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AuctionEndedEventResponse typedResponse = new AuctionEndedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.highestBid = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
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
                typedResponse.highestBid = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
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
    public static BlindAuction load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BlindAuction(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static BlindAuction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BlindAuction(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static BlindAuction load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new BlindAuction(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static BlindAuction load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new BlindAuction(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<BlindAuction> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _biddingTime, BigInteger _revealTime, String _beneficiary) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_biddingTime),
                new org.web3j.abi.datatypes.generated.Uint256(_revealTime), 
                new org.web3j.abi.datatypes.Address(_beneficiary)));
        return deployRemoteCall(BlindAuction.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<BlindAuction> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _biddingTime, BigInteger _revealTime, String _beneficiary) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_biddingTime),
                new org.web3j.abi.datatypes.generated.Uint256(_revealTime), 
                new org.web3j.abi.datatypes.Address(_beneficiary)));
        return deployRemoteCall(BlindAuction.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<BlindAuction> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _biddingTime, BigInteger _revealTime, String _beneficiary) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_biddingTime),
                new org.web3j.abi.datatypes.generated.Uint256(_revealTime), 
                new org.web3j.abi.datatypes.Address(_beneficiary)));
        return deployRemoteCall(BlindAuction.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<BlindAuction> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _biddingTime, BigInteger _revealTime, String _beneficiary) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_biddingTime),
                new org.web3j.abi.datatypes.generated.Uint256(_revealTime), 
                new org.web3j.abi.datatypes.Address(_beneficiary)));
        return deployRemoteCall(BlindAuction.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class AuctionEndedEventResponse {
        public Log log;

        public String winner;

        public BigInteger highestBid;
    }
}
