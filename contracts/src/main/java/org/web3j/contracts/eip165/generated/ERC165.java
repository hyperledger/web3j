package org.web3j.contracts.eip165.generated;

import java.util.Arrays;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.generated.RemoteCall1;
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
 * <p>Generated with web3j version 4.1.1.
 */
public class ERC165 extends Contract {
    private static final String BINARY = "Bin file was not provided";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";
    
    protected ERC165(final String contractAddress, final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider, final TransactionReceipt transactionReceipt) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider, transactionReceipt);
    }

    protected ERC165(final String contractAddress, final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider, final TransactionReceipt transactionReceipt) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider, transactionReceipt);
    }

    public RemoteCall<Boolean> supportsInterface(final byte[] interfaceID) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceID)), 
                Arrays.asList(new TypeReference<Bool>() {}));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }

    public static ERC165 load(final String contractAddress, final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider) {
        return new ERC165(contractAddress, web3j, credentials, contractGasProvider, null);
    }

    public static ERC165 load(final String contractAddress, final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider) {
        return new ERC165(contractAddress, web3j, transactionManager, contractGasProvider, null);
    }
}
