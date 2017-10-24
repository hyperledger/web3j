package org.web3j.generated;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.0.0-alpha4.
 */
public final class ShipIt extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6101db8061001e6000396000f300606060405263ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663d51cd4ac811461003c57600080fd5b341561004757600080fd5b61006873ffffffffffffffffffffffffffffffffffffffff60043516610150565b60405173ffffffffffffffffffffffffffffffffffffffff808a168252881660208201526040810187905260608101869052608081018560008111156100aa57fe5b60ff168152602081018590526060810183905260808282038101604083019081528554600260001960018316156101000201909116049183018290529160a001908590801561013a5780601f1061010f5761010080835404028352916020019161013a565b820191906000526020600020905b81548152906001019060200180831161011d57829003601f168201915b5050995050505050505050505060405180910390f35b6000602081905290815260409020805460018201546002830154600384015460048501546005860154600787015473ffffffffffffffffffffffffffffffffffffffff9687169796909516959394929360ff90921692909160060190885600a165627a7a72305820a724ff6f47df5f7b0a835a873d40089b7ace6a56076e71c1f6941f892fedf1780029";

    private ShipIt(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private ShipIt(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<Tuple8<String, String, BigInteger, BigInteger, BigInteger, BigInteger, String, byte[]>> shipments(String param0) {
        final Function function = new Function("shipments", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}));
        return new RemoteCall<Tuple8<String, String, BigInteger, BigInteger, BigInteger, BigInteger, String, byte[]>>(
                new Callable<Tuple8<String, String, BigInteger, BigInteger, BigInteger, BigInteger, String, byte[]>>() {
                    @Override
                    public Tuple8<String, String, BigInteger, BigInteger, BigInteger, BigInteger, String, byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);;
                        return new Tuple8<String, String, BigInteger, BigInteger, BigInteger, BigInteger, String, byte[]>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (String) results.get(6).getValue(), 
                                (byte[]) results.get(7).getValue());
                    }
                });
    }

    public static RemoteCall<ShipIt> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ShipIt.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ShipIt> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ShipIt.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static ShipIt load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ShipIt(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ShipIt load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ShipIt(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
