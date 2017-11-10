package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Int;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Int32 extends Int {
    public static final Int32 DEFAULT = new Int32(BigInteger.ZERO);

    public Int32(BigInteger value) {
        super(32, value);
    }

    public Int32(long value) {
        this(BigInteger.valueOf(value));
    }
}
