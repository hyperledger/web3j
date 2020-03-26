package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Uint;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Uint128 extends Uint {
    public static final Uint128 DEFAULT = new Uint128(BigInteger.ZERO);

    public Uint128(final BigInteger value) {
        super(128, value);
    }

    public Uint128(final long value) {
        this(BigInteger.valueOf(value));
    }
}
