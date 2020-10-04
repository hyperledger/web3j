package org.web3j.contracts.eip721.generated;

import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
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
public class ERC721Metadata extends Contract {
    private static final String BINARY = "Bin file was not provided";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOKENURI = "tokenURI";

    protected ERC721Metadata(final String contractAddress, final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider, final TransactionReceipt transactionReceipt) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider, transactionReceipt);
    }

    protected ERC721Metadata(final String contractAddress, final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider, final TransactionReceipt transactionReceipt) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider, transactionReceipt);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.asList(), 
                Arrays.asList(new TypeReference<Utf8String>() {}));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.asList(), 
                Arrays.asList(new TypeReference<Utf8String>() {}));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }

    public RemoteCall<String> tokenURI(final BigInteger _tokenId) {
        final Function function = new Function(FUNC_TOKENURI, 
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Arrays.asList(new TypeReference<Utf8String>() {}));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }
    
    public static ERC721Metadata load(final String contractAddress, final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider) {
        return new ERC721Metadata(contractAddress, web3j, credentials, contractGasProvider, null);
    }

    public static ERC721Metadata load(final String contractAddress, final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider) {
        return new ERC721Metadata(contractAddress, web3j, transactionManager, contractGasProvider, null);
    }
}
