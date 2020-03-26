package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Int;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Int80 extends Int {
    public static final Int80 DEFAULT = new Int80(BigInteger.ZERO);

    public Int80(final BigInteger value) {
        super(80, value);
    }

    public Int80(final long value) {
        this(BigInteger.valueOf(value));
    }
}
