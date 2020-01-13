package org.web3j.protocol.scenarios;


import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.generated.ComplexStorage;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;


@EVMTest
public class ComplexStorageIT {

    @Test
    public void greeterDeploys(
            Web3j web3j,
            TransactionManager transactionManager,
            ContractGasProvider gasProvider
    ) throws Exception {

        DynamicStruct foo = new DynamicStruct(new Utf8String("some_id"), new Utf8String("some_name"));
        DynamicStruct bar = new DynamicStruct(new Utf8String("other"), new Uint(BigInteger.TEN));


        ComplexStorage storage = ComplexStorage.deploy(web3j, transactionManager, gasProvider).send();
        storage.setFoo(foo).send();

        DynamicStruct result = storage.getFoo().send();
        System.out.println(result);
//        Assertions.assertEquals("Hello EVM", greeting);
    }

}
