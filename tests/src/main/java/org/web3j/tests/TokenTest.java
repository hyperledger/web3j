package org.web3j.tests;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.account.Account;
import org.web3j.protocol.account.CompiledContract;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.math.BigInteger;
import java.util.Random;

public class TokenTest {

    private static final String privateKey = "352416e1c910e413768c51390dfd791b414212b7b4fe6b1a18f58007fa894214";
    private static final String fromAddress = "0x0dbd369a741319fa5107733e2c9db9929093e3c7";
    private static final String toAddress = "0x546226ed566d0abb215c9db075fc36476888b310";
    private static final String solPath = "tests/src/main/resources/Token.sol";
    private static final int version = 0;
    private static final int chainId = 1;

    private static Random random;
    private static BigInteger quota;
    private final static Web3j service = Web3j.build(new HttpService("http://127.0.0.1:1337"));

    private Account account;
    private CompiledContract tokenContract;
    private String contractAddress;

    static {
        random = new Random(System.currentTimeMillis());
        quota = BigInteger.valueOf(1000000);
    }

    private static BigInteger randomNonce() {
        return BigInteger.valueOf(Math.abs(random.nextLong()));
    }

    private static TransactionReceipt waitToGetReceipt(String hash) throws Exception {
        Thread.sleep(10_000);
        return service.ethGetTransactionReceipt(hash).send().getTransactionReceipt().get();
    }

    public TokenTest() throws Exception {
        account = new Account(privateKey, service);
        tokenContract = new CompiledContract(new File(solPath));

    }

    public void deployContract(String path) throws Exception {
        EthSendTransaction ethSendTransaction = account.deploy(new File(path), randomNonce(), quota, version, chainId);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("deploy contract failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
        System.out.println("deploy contract success and contract address is " + receipt.getContractAddress());
        contractAddress = receipt.getContractAddress();
    }

    public void transfer(String toAddress, BigInteger amount) throws Exception {
        AbiDefinition transfer = tokenContract.getFunctionAbi("transfer", 2);
        EthSendTransaction ethSendTransaction = (EthSendTransaction) account.callContract(contractAddress, transfer, randomNonce(), quota, version, chainId, toAddress, amount);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("call transfer method failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
        System.out.println("call transfer method success and receipt is " + receipt.getTransactionHash());
    }

    public void getBalance(String address) throws Exception {
        AbiDefinition getBalance = tokenContract.getFunctionAbi("getBalance", 1);
        Object object = account.callContract(contractAddress, getBalance, randomNonce(), quota, version, chainId, address);
        System.out.println(address + " has " + object.toString() + " tokens");
    }

    public void transferRemote(String toAddress, BigInteger amount) throws Exception {
        EthSendTransaction ethSendTransaction = (EthSendTransaction) account.callContract(contractAddress, "transfer", randomNonce(), quota, version, chainId, toAddress, amount);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("call transfer method failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
        System.out.println("call transfer method success and receipt is " + receipt.getTransactionHash());
    }

    public void getBalanceRemote(String address) throws Exception {
        Object object = account.callContract(contractAddress, "getBalance", randomNonce(), quota, version, chainId, address);
        System.out.println(address + " has " + object.toString() + " tokens");
    }

    public void storeAbiToBlockchain() throws Exception {
        EthSendTransaction ethSendTransaction = (EthSendTransaction) account.uploadAbi(contractAddress, tokenContract.getAbi(), randomNonce(), quota, version, chainId);
        TransactionReceipt receipt = waitToGetReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage() != null) {
            System.out.println("call upload abi method failed because of " + receipt.getErrorMessage());
            System.exit(1);
        }
        System.out.println("call upload abi method success and receipt is " + receipt.getTransactionHash());
    }

    public void getAbi() throws Exception {
        String abi = account.getAbi(contractAddress);
        System.out.println("abi: " + abi);
    }

    public static void main(String[] args) throws Exception {
        // 本地编译solidity文件，然后部署合约以及调用合约方法
        deployContractAndCallMethodFromSolidity();

        // 根据已经部署过的合约地址从链上获取abi，然后调用合约方法
//        callContractMethodFromRemoteAbi();

        System.exit(0);
    }


    private static void deployContractAndCallMethodFromSolidity() throws Exception {
        TokenTest tokenTest = new TokenTest();
        tokenTest.deployContract(solPath);
        tokenTest.getBalance(fromAddress);
        tokenTest.getBalance(toAddress);
        tokenTest.transfer(toAddress, BigInteger.valueOf(1200));
        tokenTest.getBalance(fromAddress);
        tokenTest.getBalance(toAddress);
        tokenTest.storeAbiToBlockchain();
        tokenTest.getAbi();
    }

    private static void callContractMethodFromRemoteAbi() throws Exception {
        TokenTest tokenTest = new TokenTest();

        tokenTest.contractAddress = "0xf889c843bab04701424369c94d5acaeed3648938";
        tokenTest.transferRemote(toAddress, BigInteger.valueOf(500));
        tokenTest.getBalanceRemote(fromAddress);
        tokenTest.getBalanceRemote(toAddress);
    }
}
