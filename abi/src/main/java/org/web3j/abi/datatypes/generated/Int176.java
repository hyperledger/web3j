package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Int;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/hyperledger/web3j/tree/main/codegen">codegen module</a> to update.
 */
public class Int176 extends Int {
    public static final Int176 DEFAULT = new Int176(BigInteger.ZERO);

    public Int176(BigInteger value) {
        super(176, value);
    }

    public Int176(long value) {
        this(BigInteger.valueOf(value));
    }
}
