package org.web3j.protocol.account;

import org.web3j.abi.*;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.UnorderedEvent;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Call;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.CitaTransactionManager;
import org.web3j.utils.TypedAbi;
import rx.Observable;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Account {
    private static final String ABI_ADDRESS = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    private CitaTransactionManager transactionManager;
    private Web3j service;
    private String abi;

    public Account(String privateKey, Web3j service) {
        Credentials credentials = Credentials.create(privateKey);
        this.transactionManager = new CitaTransactionManager(service, credentials);
        this.service = service;
    }

    public CitaTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /// TODO: get contract address from receipt after deploy, then return contract name(ENS)
    public EthSendTransaction deploy(File contractFile, BigInteger nonce, BigInteger quota, int version, int chainId)
            throws IOException, InterruptedException, CompiledContract.ContractCompileError {
        CompiledContract contract = new CompiledContract(contractFile);
        String contractBin = contract.getBin();
        return this.transactionManager.
                sendTransaction("", contractBin, quota, nonce, getValidUntilBlock(), BigInteger.valueOf(version), BigInteger.valueOf(chainId));
    }

    public CompletableFuture<EthSendTransaction> deployAsync(File contractFile, BigInteger nonce, BigInteger quota, BigInteger version, int chainId)
            throws IOException, InterruptedException, CompiledContract.ContractCompileError {
        CompiledContract contract = new CompiledContract(contractFile);
        String contractBin = contract.getBin();
        return this.transactionManager.
                sendTransactionAsync("", contractBin, quota, nonce, getValidUntilBlock(), version, chainId);
    }

    // eth_call: nonce and quota is null
    // sendTransaction: nonce and quota is necessary
    public Object callContract(String contractAddress, String funcName, BigInteger nonce, BigInteger quota, int version, int chainId, Object... args)
            throws Exception {
        if (abi == null) {
            abi = getAbi(contractAddress);
        }
        CompiledContract contract = new CompiledContract(abi);
        AbiDefinition functionAbi = contract.getFunctionAbi(funcName, args.length);
        return callContract(contractAddress, functionAbi, nonce, quota, version, chainId,args);
    }

    public Object callContract(String contractAddress, AbiDefinition functionAbi, BigInteger nonce, BigInteger quota, int version,int chainId, Object... args)
            throws Exception {
        List<Type> params = new ArrayList<>();
        List<AbiDefinition.NamedType> inputs = functionAbi.getInputs();
        for (int i = 0; i < inputs.size(); i++) {
            Object arg = args[i];
            String typeName = inputs.get(i).getType();
            params.add(TypedAbi.getType(typeName, arg));
        }

        Function func;
        if (functionAbi.isConstant()) {
            // eth_call
            List<TypedAbi.ArgRetType> retsType = new ArrayList<>();
            List<TypeReference<?>> retsTypeRef = new ArrayList<>();
            List<AbiDefinition.NamedType> outputs = functionAbi.getOutputs();
            for (AbiDefinition.NamedType namedType: outputs) {
                TypedAbi.ArgRetType retType = TypedAbi.getArgRetType(namedType.getType());
                retsType.add(retType);
                retsTypeRef.add(retType.getTypeReference());
            }
            func = new Function(functionAbi.getName(), params, retsTypeRef);
            return ethCall(contractAddress, func, retsType);
        } else {
            // send_transaction
            func = new Function(functionAbi.getName(), params, Collections.emptyList());
            return sendTransaction(contractAddress, func, nonce, quota.longValue(), version, chainId);
        }
    }

    public Object ethCall(String contractAddress, Function func, List<TypedAbi.ArgRetType> retsType)
            throws IOException {
        String data = FunctionEncoder.encode(func);
        EthCall call = this.service.ethCall(new Call(this.transactionManager.getFromAddress(),
                contractAddress, data), DefaultBlockParameterName.LATEST).send();
        String value = call.getValue();
        List<Type> abiValues = FunctionReturnDecoder.decode(value, func.getOutputParameters());
        if (retsType.size() == 1) {
            return retsType.get(0).abiToJava(abiValues.get(0));
        } else {
            List<Object> results = new ArrayList<>();
            for (int i = 0; i < retsType.size(); i++) {
                results.add(retsType.get(i).abiToJava(abiValues.get(i)));
            }
            return results;
        }
    }

    public Object sendTransaction(String contractAddress, Function func, BigInteger nonce, long quota, long version, int chainId)
            throws IOException {
        String data = FunctionEncoder.encode(func);
        return this.transactionManager.sendTransaction(contractAddress, data, BigInteger.valueOf(quota), nonce, getValidUntilBlock(), BigInteger.valueOf(version), BigInteger.valueOf(chainId));
    }

    public Object uploadAbi(String contractAddress, String abi, BigInteger nonce, BigInteger quota, long version, int chainId) throws Exception {
        String data = hex_remove_0x(contractAddress) + hex_remove_0x(bytesToHexStr(abi.getBytes()));
        return this.transactionManager.sendTransaction(ABI_ADDRESS, data, quota, nonce, getValidUntilBlock(), BigInteger.valueOf(version), BigInteger.valueOf(chainId));
    }

    public String getAbi(String contractAddress) throws IOException {
        String abi = service.ethGetAbi(contractAddress, DefaultBlockParameter.valueOf("latest")).send().getAbi();
        return new String(hexStrToBytes(hex_remove_0x(abi)));
    }

    public Observable<Object> eventObservable(String contractName, String eventName)
            throws Exception {
        CompiledContract contract = loadContract(contractName);
        String contractAddress = getContractAddress(contractName);
        AbiDefinition eventAbi = contract.getEventAbi(eventName);
        return eventObservable(contractAddress, eventAbi, DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST);
    }

    public Observable<Object> eventObservable(String contractAddress, AbiDefinition eventAbi,
                                              DefaultBlockParameter start, DefaultBlockParameter end)
            throws Exception {
        List<TypedAbi.ArgRetType> results = new ArrayList<>();
        List<AbiDefinition.NamedType> namedTypes = eventAbi.getInputs();
        UnorderedEvent event = new UnorderedEvent(eventAbi.getName());
        for (AbiDefinition.NamedType namedType: namedTypes) {
            TypedAbi.ArgRetType argRetType = TypedAbi.getArgRetType(namedType.getType());
            results.add(argRetType);
            event.add(namedType.isIndexed(), argRetType.getTypeReference());
        }

        EthFilter filter = new EthFilter(start, end, contractAddress);
        /// FIXME: https://github.com/web3j/web3j/issues/209, patch to this after web3j fixed
        filter.addSingleTopic(EventEncoder.encode(event));
        return this.service.ethLogObservable(filter).map(log -> {
            EventValues eventValues = staticExtractEventParameters(event, log);
            List<Type> indexedValues = eventValues.getIndexedValues();
            List<Type> nonIndexedValues = eventValues.getNonIndexedValues();
            int indexedSize = indexedValues.size();
            int nonIndexedSize = nonIndexedValues.size();
            int size = indexedSize + nonIndexedSize;
            List<Object> values = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                values.add(null);
            }

            List<Integer> indexedSeq = event.getIndexedParametersSeq();
            for (int i = 0; i < indexedSize; i++) {
                int indexSeqNum = indexedSeq.get(i);
                values.set(indexSeqNum, results.get(indexSeqNum).abiToJava(indexedValues.get(i)));
            }

            List<Integer> nonIndexedSeq = event.getNonIndexedParametersSeq();
            for (int i = 0; i < nonIndexedSize; i++) {
                int indexSeqNum = nonIndexedSeq.get(i);
                values.set(indexSeqNum, results.get(indexSeqNum).abiToJava(nonIndexedValues.get(i)));
            }
            return values;
        });
    }

    private static EventValues staticExtractEventParameters(
            UnorderedEvent event, Log log) {

        List<String> topics = log.getTopics();
        String encodedEventSignature = EventEncoder.encode(event);
        if (!topics.get(0).equals(encodedEventSignature)) {
            return null;
        }

        List<Type> indexedValues = new ArrayList<>();
        List<Type> nonIndexedValues = FunctionReturnDecoder.decode(
                log.getData(), event.getNonIndexedParameters());

        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(
                    topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        return new EventValues(indexedValues, nonIndexedValues);
    }

    private CompiledContract loadContract(String contractName) throws IOException {
        /// TODO: jsonrpc
        return new CompiledContract("");
    }

    /// TODO: get contract address from ENS
    private String getContractAddress(String contractName) {
        return "";
    }

    private long blockHeight() throws IOException {
        return this.service.ethBlockNumber().send().getBlockNumber().longValue();
    }

    private BigInteger getValidUntilBlock() throws IOException {
        return BigInteger.valueOf(blockHeight() + 80);
    }

    private String hex_remove_0x(String hex){
        if (hex.contains("0x")) {
            return hex.substring(2);
        }
        return hex;
    }

    private String bytesToHexStr(byte[] byteArr) {
        if (null == byteArr || byteArr.length < 1) return "";
        StringBuilder sb = new StringBuilder();
        for (byte t : byteArr) {
            if ((t & 0xF0) == 0) sb.append("0");
            sb.append(Integer.toHexString(t & 0xFF));
        }
        return sb.toString();
    }

    private byte[] hexStrToBytes(String hexStr) {
        if (null == hexStr || hexStr.length() < 1) return null;
        int byteLen = hexStr.length() / 2;
        byte[] result = new byte[byteLen];
        char[] hexChar = hexStr.toCharArray();
        for(int i=0 ;i<byteLen;i++){
            result[i] = (byte)(Character.digit(hexChar[i*2],16)<<4 | Character.digit(hexChar[i*2+1],16));
        }
        return result;
    }
}
