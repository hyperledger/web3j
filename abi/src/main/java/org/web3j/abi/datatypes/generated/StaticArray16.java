package org.web3j.abi.datatypes.generated;

import java.util.List;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/hyperledger/web3j/tree/main/codegen">codegen module</a> to update.
 */
public class StaticArray16<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray16(List<T> values) {
        super(16, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray16(T... values) {
        super(16, values);
    }

    public StaticArray16(Class<T> type, List<T> values) {
        super(type, 16, values);
    }

    @SafeVarargs
    public StaticArray16(Class<T> type, T... values) {
        super(type, 16, values);
    }
}
