package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Int;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/hyperledger/web3j/tree/main/codegen">codegen module</a> to update.
 */
public class Int160 extends Int {
    public static final Int160 DEFAULT = new Int160(BigInteger.ZERO);

    public Int160(BigInteger value) {
        super(160, value);
    }

    public Int160(long value) {
        this(BigInteger.valueOf(value));
    }
}
