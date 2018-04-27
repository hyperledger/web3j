package org.web3j.examples;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Call;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.Random;

public class SendTransactionDemo {
    private final static int VERSION = 0;

    private static Web3j service;
    private static Random random;
    private static int chainId;

    static {
        service = Web3j.build(new HttpService("http://127.0.0.1:1337"));
        random = new Random(System.currentTimeMillis());
        chainId = 1;
    }

    static long currentBlockNumber() throws Exception {
        return service.ethBlockNumber().send().getBlockNumber().longValue();
    }

    // Deploy contract, return transaction hash
    static String deployContract() throws Exception {
        // contract.bin
        String contractCode = "606060405234156100105760006000fd5b610015565b60e0806100236000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806360fe47b114604b5780636d4ce63c14606c576045565b60006000fd5b341560565760006000fd5b606a60048080359060200190919050506093565b005b341560775760006000fd5b607d60a3565b6040518082815260200191505060405180910390f35b8060006000508190909055505b50565b6000600060005054905060b1565b905600a165627a7a72305820942223976c6dd48a3aa1d4749f45ad270915cfacd9c0bf3583c018d4c86f9da20029";
        String privateKey = "352416e1c910e413768c51390dfd791b414212b7b4fe6b1a18f58007fa894214";
        long currentHeight = currentBlockNumber();
        // validUntilBlock should between currentHeight and currentHeight+100
        long validUntilBlock = currentHeight + 80;
        BigInteger nonce = BigInteger.valueOf(Math.abs(random.nextLong()));
        long quota = 1000000;

        Transaction tx = Transaction.createContractTransaction(nonce, quota, validUntilBlock, VERSION, chainId, contractCode);
        String rawTx = tx.sign(privateKey);

        return service.ethSendRawTransaction(rawTx).send().getSendTransactionResult().getHash();
    }

    // Contract function call
    static String contractFunctionCall(String contractAddress) throws Exception {
        String functionCallData = "60fe47b10000000000000000000000000000000000000000000000000000000000000001";
        String privateKey = "352416e1c910e413768c51390dfd791b414212b7b4fe6b1a18f58007fa894214";
        long currentHeight = currentBlockNumber();
        // validUntilBlock should between currentHeight and currentHeight+100
        long validUntilBlock = currentHeight + 80;
        BigInteger nonce = BigInteger.valueOf(Math.abs(random.nextLong()));
        long quota = 1000000;


        Transaction tx = Transaction.createFunctionCallTransaction(contractAddress, nonce, quota, validUntilBlock, VERSION, functionCallData, chainId);
        String rawTx = tx.sign(privateKey);

        return service.ethSendRawTransaction(rawTx).send().getSendTransactionResult().getHash();
    }

    // eth_call
    static String call(String from, String contractAddress, String callData) throws Exception {
        Call call = new Call(from, contractAddress, callData);
        return service.ethCall(call, DefaultBlockParameter.valueOf("latest")).send().getValue();
    }

    // Get transaction receipt
    static TransactionReceipt getTransactionReceipt(String txHash) throws Exception {
        return service.ethGetTransactionReceipt(txHash).send().getTransactionReceipt().get();
    }

    public static void main(String[] args) throws Exception {
        // deploy contract
        String deployContractTxHash = SendTransactionDemo.deployContract();
        System.out.println("wait to deploy contract");
        Thread.sleep(10000);

        // get contract address from receipt
        TransactionReceipt txReceipt = SendTransactionDemo.getTransactionReceipt(deployContractTxHash);
        String contractAddress = txReceipt.getContractAddress();

        // call contract function
        SendTransactionDemo.contractFunctionCall(contractAddress);
        System.out.println("Contract address: " + contractAddress + ", wait to call contract function");
        Thread.sleep(10000);

        String from = "0dbd369a741319fa5107733e2c9db9929093e3c7";
        String ethCallResult = call(from, contractAddress, "0x6d4ce63c");
        System.out.println("eth_call result: " + ethCallResult);
        System.out.println("complete");
    }
}