package org.web3j.protocol.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.utils.CallCmd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Contract {
    public static class ContractCompileError extends Exception {
        private String errorMessage;
        public ContractCompileError(String error) {
            errorMessage = error;
        }

        @Override
        public String toString() {
            return "complie contract failed because of " + errorMessage;
        }
    }

    public static class ContractFuncNotFound extends Exception {
        private String funcName;
        private int numOfArgs;
        public ContractFuncNotFound(String func, int num) {
            this.funcName = func;
            this.numOfArgs = num;
        }

        @Override
        public String toString() {
            return "contract method " + funcName + " with " + numOfArgs + " args " + " not found";
        }
    }

    private String bin;
    private String abi;
    private List<AbiDefinition> typedABI;

    /// NOTE: the file name must be same with contract name
    public Contract(File contractFile) throws IOException, InterruptedException, ContractCompileError {
        String fileName = contractFile.getName();
        if (fileName.indexOf(".") > 0) {
            fileName = fileName.substring(0, fileName.indexOf("."));
        }
        generateAbiAndBin(contractFile);

        String binPath = "/tmp/" + fileName + ".bin";
        String abiPath = "/tmp/" + fileName + ".abi";
        this.bin = new String(Files.readAllBytes(Paths.get(binPath)));
        this.abi = new String(Files.readAllBytes(Paths.get(abiPath)));
        this.typedABI = generateTypedABI();
    }

    public Contract(String abi) throws IOException {
        this.abi = abi;
        this.typedABI = generateTypedABI();
    }

    /// TODO: support windows OS
    private void generateAbiAndBin(File contractFile) throws IOException, InterruptedException, ContractCompileError {
        String callSolcCmd = String.format("solc %s --abi --bin --optimize --overwrite -o /tmp/", contractFile.getAbsolutePath());
        CallCmd.ExecutedResult result = CallCmd.callCmd(callSolcCmd);
        if (result.exitCode != 0) {
            throw new ContractCompileError(result.output);
        }
    }

    private List<AbiDefinition> generateTypedABI() throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AbiDefinition[] typedABI = objectMapper.readValue(this.abi, AbiDefinition[].class);
        return Arrays.asList(typedABI);
    }

    public List<AbiDefinition> getTypedABI() {
        return this.typedABI;
    }

    public String getBin() {
        return this.bin;
    }

    /// TODO: how to distinguish overload function which the num of args are same???
    public AbiDefinition getFunctionAbi(String funcName, int numOfArgs) throws ContractFuncNotFound {
        Object[] abiDefinitions =  this.typedABI
                .stream()
                .filter(abiDefinition ->
                        abiDefinition.getType().equals("function") &&
                        abiDefinition.getName().equals(funcName) &&
                        abiDefinition.getInputs().size() == numOfArgs)
                .toArray();

        AbiDefinition functionAbi;
        if (abiDefinitions.length == 0) {
            throw new ContractFuncNotFound(funcName, numOfArgs);
        } else {
            return (AbiDefinition)abiDefinitions[0];
        }
    }
}
