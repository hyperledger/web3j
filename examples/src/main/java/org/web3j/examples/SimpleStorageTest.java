package org.web3j.examples;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.account.Account;
import org.web3j.protocol.account.Contract;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class SimpleStorageTest {
    public static void main(String[] args) throws Exception {
        String privateKey = "352416e1c910e413768c51390dfd791b414212b7b4fe6b1a18f58007fa894214";
        Credentials credentials = Credentials.create(privateKey);
        String address = credentials.getAddress();
        System.out.println("account address: " + address);
        List<String> addrs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            addrs.add(address);
        }

        Web3j service = Web3j.build(new HttpService("http://127.0.0.1:1337"));
        Account account = new Account(privateKey, service);
        String path = "/home/lbqds/WorkSpace/tasks/web3j/examples/src/main/resources/SimpleStorage.sol";
        EthSendTransaction result = account.deploy(new File(path), BigInteger.valueOf(1), BigInteger.valueOf(1000000));
        String hash = result.getSendTransactionResult().getHash();
        System.out.println("deploy contract tx hash: " + hash);

        Thread.sleep(10_000);
        TransactionReceipt receipt = service.ethGetTransactionReceipt(hash).send().getResult();
        String contractAddress = receipt.getContractAddress();
        System.out.println("contract address: " + contractAddress);

        Contract contract = new Contract(new File(path));
        EthSendTransaction setStorage = (EthSendTransaction)account.callContract(contractAddress, contract.getFunctionAbi("set", 2),
                "set", BigInteger.valueOf(2), BigInteger.valueOf(1000000), BigInteger.valueOf(12), addrs);
        System.out.println("set storage tx hash: " + setStorage.getSendTransactionResult().getHash());

        Thread.sleep(15_000);
        receipt = service.ethGetTransactionReceipt(setStorage.getSendTransactionResult().getHash()).send().getResult();
        System.out.println("error message is: " + receipt.getErrorMessage());

        Object storage = account.callContract(contractAddress, contract.getFunctionAbi("get", 0),
                "get", null, null);
        if (List.class.isAssignableFrom(storage.getClass())) {
            List<Object> values = (List<Object>) storage;
            BigInteger data = (BigInteger) values.get(0);
            List<String> addr = (List<String>) values.get(1);
            System.out.println("storage is: " + data.longValue());
            System.out.println("addr is: " + addr);
        }
        System.exit(0);
    }
}
