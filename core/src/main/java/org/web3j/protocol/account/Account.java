package org.web3j.protocol.account;

import org.web3j.abi.*;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Call;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
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
    private CitaTransactionManager transactionManager;
    private Web3j service;

    public Account(String privateKey, Web3j service) {
        Credentials credentials = Credentials.create(privateKey);
        this.transactionManager = new CitaTransactionManager(service, credentials);
        this.service = service;
    }

    public EthSendTransaction deploy(File contractFile, BigInteger nonce, BigInteger quota)
            throws IOException, InterruptedException, CompiledContract.ContractCompileError {
        CompiledContract contract = new CompiledContract(contractFile);
        String contractBin = contract.getBin();
        return this.transactionManager.
                sendTransaction("", contractBin, quota, nonce, getValidUntilBlock());
    }

    public CompletableFuture<EthSendTransaction> deployAsync(File contractFile, BigInteger nonce, BigInteger quota)
            throws IOException, InterruptedException, CompiledContract.ContractCompileError {
        CompiledContract contract = new CompiledContract(contractFile);
        String contractBin = contract.getBin();
        return this.transactionManager.
                sendTransactionAsync("", contractBin, quota, nonce, getValidUntilBlock());
    }

    // eth_call: nonce and quota is null
    // sendTransaction: nonce and quota is necessary
    public Object callContract(String contractName, String funcName, BigInteger nonce, BigInteger quota, Object... args)
            throws Exception {
        CompiledContract contract = loadContract(contractName);
        String contractAddress = getContractAddress(contract);
        AbiDefinition functionAbi = contract.getFunctionAbi(funcName, args.length);
        return callContract(contractAddress, functionAbi, nonce, quota, args);
    }

    public Object callContract(String contractAddress, AbiDefinition functionAbi, BigInteger nonce, BigInteger quota, Object... args)
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
            // send_transaction, no ret
            func = new Function(functionAbi.getName(), params, Collections.emptyList());
            return sendTransaction(contractAddress, func, nonce, quota.longValue());
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

    public Object sendTransaction(String contractAddress, Function func, BigInteger nonce, long quota)
            throws IOException {
        String data = FunctionEncoder.encode(func);
        return this.transactionManager.sendTransaction(contractAddress, data, BigInteger.valueOf(quota), nonce, getValidUntilBlock());
    }

    public Observable<Object> eventObservable(String contractName, String eventName, DefaultBlockParameter start, DefaultBlockParameter end)
            throws Exception {
        CompiledContract contract = loadContract(contractName);
        String contractAddress = getContractAddress(contract);
        AbiDefinition eventAbi = contract.getEventAbi(eventName);
        return eventObservable(contractAddress, eventAbi, start, end);
    }

    public Observable<Object> eventObservable(String contractAddress, AbiDefinition eventAbi,
                                              DefaultBlockParameter start, DefaultBlockParameter end)
            throws Exception {
        List<TypeReference<?>> indexedParameters = new ArrayList<>();
        List<TypeReference<?>> nonindexedParameters = new ArrayList<>();
        List<TypedAbi.ArgRetType> results = new ArrayList<>();
        List<AbiDefinition.NamedType> namedTypes = eventAbi.getInputs();
        for (AbiDefinition.NamedType namedType: namedTypes) {
            TypedAbi.ArgRetType argRetType = TypedAbi.getArgRetType(namedType.getType());
            results.add(argRetType);
            if (namedType.isIndexed()) {
                indexedParameters.add(argRetType.getTypeReference());
            } else {
                nonindexedParameters.add(argRetType.getTypeReference());
            }
        }

        Event event = new Event(eventAbi.getName(), indexedParameters, nonindexedParameters);
        EthFilter filter = new EthFilter(start, end, contractAddress);
        /// FIXME: https://github.com/web3j/web3j/issues/209, patch to this after web3j fixed
        filter.addSingleTopic(EventEncoder.encode(event));
        return this.service.ethLogObservable(filter).map(log -> {
            EventValues eventValues = staticExtractEventParameters(event, log);
            List<Object> values = new ArrayList<>();
            List<Type> indexedValues = eventValues.getIndexedValues();
            int indexedSize = indexedValues.size();
            for (int i = 0; i < indexedSize; i++) {
                values.add(results.get(i).abiToJava(indexedValues.get(i)));
            }

            List<Type> nonindexedValues = eventValues.getNonIndexedValues();
            for (int i = 0; i < nonindexedValues.size(); i++) {
                values.add(results.get(i + indexedSize).abiToJava(nonindexedValues.get(i)));
            }
            return values;
        });
    }

    public static EventValues staticExtractEventParameters(
            Event event, Log log) {

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

    private String getContractAddress(CompiledContract contract) {
        return "";
    }

    private long blockHeight() throws IOException {
        return this.service.ethBlockNumber().send().getBlockNumber().longValue();
    }

    private BigInteger getValidUntilBlock() throws IOException {
        return BigInteger.valueOf(blockHeight() + 80);
    }
}
