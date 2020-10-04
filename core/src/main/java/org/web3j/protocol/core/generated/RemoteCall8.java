package org.web3j.protocol.core.generated;

import java.util.List;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.AbstractRemoteCall;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.TransactionManager;

public class RemoteCall8<T1, T2, T3, T4, T5, T6, T7, T8>
        extends AbstractRemoteCall<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> {

    public RemoteCall8(
            final Function function,
            final String contractAddress,
            final boolean useNativeJavaTypes,
            final TransactionManager transactionManager,
            final DefaultBlockParameter defaultBlockParameter) {
        super(function, contractAddress, useNativeJavaTypes, transactionManager, defaultBlockParameter);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> convert(final List<Type<?>> values) {
        return new Tuple8<>(
                ((T1) (useNativeJavaTypes() ? values.get(0).getValue() : values.get(0))),
                ((T2) (useNativeJavaTypes() ? values.get(1).getValue() : values.get(1))),
                ((T3) (useNativeJavaTypes() ? values.get(2).getValue() : values.get(2))),
                ((T4) (useNativeJavaTypes() ? values.get(3).getValue() : values.get(3))),
                ((T5) (useNativeJavaTypes() ? values.get(4).getValue() : values.get(4))),
                ((T6) (useNativeJavaTypes() ? values.get(5).getValue() : values.get(5))),
                ((T7) (useNativeJavaTypes() ? values.get(6).getValue() : values.get(6))),
                ((T8) (useNativeJavaTypes() ? values.get(7).getValue() : values.get(7)))
        );
    }
}
