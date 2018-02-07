package org.web3j.protocol.contract;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Call;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.CitaTransactionManager;
import org.web3j.utils.TypedABI;

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

    public EthSendTransaction deploy(File contractFile, BigInteger nonce, long quota)
            throws IOException, InterruptedException, Contract.ContractCompileError {
        Contract contract = new Contract(contractFile);
        String contractBin = contract.getBin();
        return this.transactionManager.
                sendTransaction("", contractBin, BigInteger.valueOf(quota), nonce, getValidUntilBlock());
    }

    public CompletableFuture<EthSendTransaction> deployAsync(File contractFile, BigInteger nonce, long quota)
            throws IOException, InterruptedException, Contract.ContractCompileError {
        Contract contract = new Contract(contractFile);
        String contractBin = contract.getBin();
        return this.transactionManager.
                sendTransactionAsync("", contractBin, BigInteger.valueOf(quota), nonce, getValidUntilBlock());
    }

    // eth_call: nonce and quota is null
    // sendTransaction: nonce and quota is necessary
    public Object callContract(String contractName, String funcName, BigInteger nonce, Long quota, Object... args)
            throws Exception {
        Contract contract = loadContract(contractName);
        String contractAddress = getContractAddress(contract);
        AbiDefinition functionAbi = contract.getFunctionAbi(funcName, args.length);
        List<Type> params = new ArrayList<>();
        List<AbiDefinition.NamedType> inputs = functionAbi.getInputs();
        assert args.length == inputs.size();
        for (int i = 0; i < inputs.size(); i++) {
            Object arg = args[i];
            String typeName = inputs.get(i).getType();
            params.add(TypedABI.getType(typeName, arg));
        }

        Function func;
        if (functionAbi.isConstant()) {
            // eth_call
            List<TypedABI.ArgRetType> retsType = new ArrayList<>();
            List<TypeReference<? extends Type>> retsTypeRef = new ArrayList<>();
            List<AbiDefinition.NamedType> outputs = functionAbi.getOutputs();
            for (AbiDefinition.NamedType namedType: outputs) {
                retsTypeRef.add(TypeReference.create(TypedABI.getArgRetType(namedType.getType()).getType()));
            }
            func = new Function(funcName, params, retsTypeRef);
            return ethCall(contractAddress, func, retsType);
        } else {
            // send_transaction, no ret
            func = new Function(funcName, params, Collections.emptyList());
            return sendTransaction(contractAddress, func, nonce, quota);
        }
    }

    public Object ethCall(String contractAddress, Function func, List<TypedABI.ArgRetType> retsType)
            throws IOException {
        String data = FunctionEncoder.encode(func);
        EthCall call = this.service.ethCall(new Call(this.transactionManager.getFromAddress(),
                contractAddress, data), DefaultBlockParameterName.LATEST).send();
        String value = call.getValue();
        List<Type> abiValues = FunctionReturnDecoder.decode(value, func.getOutputParameters());
        assert retsType.size() == abiValues.size();
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
        return this.transactionManager.sendTransaction(BigInteger.valueOf(quota), nonce, contractAddress, data, getValidUntilBlock());
    }

    private Contract loadContract(String contractName) throws IOException {
        /// TODO: jsonrpc
        return new Contract("");
    }

    private String getContractAddress(Contract contract) {
        return "";
    }

    private long blockHeight() throws IOException {
        return this.service.ethBlockNumber().send().getBlockNumber().longValue();
    }

    private BigInteger getValidUntilBlock() throws IOException {
        return BigInteger.valueOf(blockHeight() + 80);
    }
}
