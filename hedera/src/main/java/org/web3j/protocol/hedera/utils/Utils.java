package org.web3j.protocol.hedera.utils;

import com.google.protobuf.ByteString;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.hedera.HederaRPC;
import org.web3j.protocol.hedera.gas.HederaGasProvider;
import org.web3j.protocol.hedera.tx.HederaTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class Utils {
  public static byte[] contractByteCode(Contract contract) {
    return contract.getContractBinary().getBytes(StandardCharsets.UTF_8);
  }
  public static ByteString functionByteString(String function) {
    return ByteString.copyFrom(functionBytes(function));
  }
  public static byte[] functionBytes(String function) {
    return Hex.decode(function.replace("0x", ""));
  }

  public static Object contract(Class contractClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    HederaRPC hederaRPC = new HederaRPC();
    String dummyAddress = "0000000000000000000000000000000000000000";
    HederaTransactionManager hederaTransactionManager = new HederaTransactionManager(hederaRPC, dummyAddress);

    Class[] argumentList = new Class[4];
    argumentList[0] = String.class;
    argumentList[1] = Web3j.class;
    argumentList[2] = TransactionManager.class;
    argumentList[3] = ContractGasProvider.class;

    Constructor<?> myConstructor = Class.forName(contractClass.getName()).
            getDeclaredConstructor(argumentList);

    myConstructor.setAccessible(true);

    return myConstructor.newInstance(dummyAddress, hederaRPC, hederaTransactionManager, new HederaGasProvider());
  }
}
