package org.web3j.tx.gas;

import java.math.BigInteger;

public interface ContractGasProvider {
    @Deprecated
    BigInteger getGasPrice(String contractFunc);

    BigInteger getGasPrice(String contractFunc, String data);

    @Deprecated
    BigInteger getGasPrice();

    @Deprecated
    BigInteger getGasLimit(String contractFunc);

    BigInteger getGasLimit(String contractFunc, String data);

    @Deprecated
    BigInteger getGasLimit();
}
