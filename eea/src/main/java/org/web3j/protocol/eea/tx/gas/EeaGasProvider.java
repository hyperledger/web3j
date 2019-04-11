package org.web3j.protocol.eea.tx.gas;

import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class EeaGasProvider extends StaticGasProvider {
    public EeaGasProvider(BigInteger gasPrice) {
        super(gasPrice, BigInteger.valueOf(3000000));
    }

    public EeaGasProvider(BigInteger gasPrice, BigInteger gasLimit) {
        super(gasPrice, gasLimit);
    }
}
