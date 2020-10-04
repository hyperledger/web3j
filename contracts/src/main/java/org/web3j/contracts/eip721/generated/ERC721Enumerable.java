package org.web3j.contracts.eip721.generated;

import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
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
public class ERC721Enumerable extends Contract {
    private static final String BINARY = "Bin file was not provided";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TOKENOFOWNERBYINDEX = "tokenOfOwnerByIndex";

    public static final String FUNC_TOKENBYINDEX = "tokenByIndex";

    protected ERC721Enumerable(final String contractAddress, final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider, final TransactionReceipt transactionReceipt) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider, transactionReceipt);
    }

    protected ERC721Enumerable(final String contractAddress, final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider, final TransactionReceipt transactionReceipt) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider, transactionReceipt);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.asList(), 
                Arrays.asList(new TypeReference<Uint256>() {}));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }

    public RemoteCall<BigInteger> tokenOfOwnerByIndex(final String _owner, final BigInteger _index) {
        final Function function = new Function(FUNC_TOKENOFOWNERBYINDEX, 
                Arrays.asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.asList(new TypeReference<Uint256>() {}));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }

    public RemoteCall<BigInteger> tokenByIndex(final BigInteger _index) {
        final Function function = new Function(FUNC_TOKENBYINDEX, 
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(_index)), 
                Arrays.asList(new TypeReference<Uint256>() {}));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }

    public static ERC721Enumerable load(final String contractAddress, final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider) {
        return new ERC721Enumerable(contractAddress, web3j, credentials, contractGasProvider, null);
    }

    public static ERC721Enumerable load(final String contractAddress, final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider) {
        return new ERC721Enumerable(contractAddress, web3j, transactionManager, contractGasProvider, null);
    }
}
