package org.web3j.tx;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import rx.Subscription;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * High Throughput transaction manager.
 */

public class HighThroughputTransactionManager extends RawTransactionManager {

	//this static hashmap stores web3j objects and their accounts
	// ---> web3j ---> account ---> HQtransactionManager
	private static HashMap<Web3j, HashMap<String, HighThroughputTransactionManager>> map = new HashMap<>();

	private String accountAddress;
	private BigInteger nonce = BigInteger.valueOf(-1);
	private Web3j web3j;
	private byte chainId;

	//this private hashmap stores receipts for each block
	// ---> block number ---> txindex ---> TransactionReceipt
	private HashMap<BigInteger, HashMap<Long, TransactionReceipt>> transactions;

	public static HighThroughputTransactionManager getInstance(Web3j web3j, Credentials credentials)
	{
		return getInstance(web3j, credentials, (byte) -1);
	}

	public static HighThroughputTransactionManager getInstance(Web3j web3j, Credentials credentials, byte chainId)
	{
		//local variable for the entry in the map
		HashMap<String, HighThroughputTransactionManager> entryWeb3j = null;

		//check if we have an entry for this Credentials and this web3j connection
		for (Web3j itWeb3j: map.keySet()) {
			if (web3j == itWeb3j) {
				entryWeb3j = map.get(itWeb3j);
			}
		}

		if (entryWeb3j == null) {
			entryWeb3j = new HashMap<>();
			map.put(web3j, entryWeb3j);
		}

		if (entryWeb3j.containsKey(credentials.getAddress())) {
			return entryWeb3j.get(credentials.getAddress());
		} else {
			HighThroughputTransactionManager instance;
			if (chainId == (byte) -1) {
				instance = new HighThroughputTransactionManager(web3j, credentials);
			} else {
				instance = new HighThroughputTransactionManager(web3j, credentials, chainId);
			}
			entryWeb3j.put(credentials.getAddress(), instance);

			return instance;
		}
	}

	public HighThroughputTransactionManager(Web3j web3j, Credentials credentials, byte chainId) {
		super(web3j, credentials, chainId);
		accountAddress = credentials.getAddress();
		this.web3j = web3j;
		this.chainId = chainId;
	}

	private HighThroughputTransactionManager(Web3j web3j, Credentials credentials) {
		super(web3j, credentials);
		this.web3j = web3j;
		accountAddress = credentials.getAddress();
	}

	@Override
	synchronized BigInteger getNonce() throws InterruptedException, ExecutionException
	{
		if (nonce.signum() == -1) {
			nonce = super.getNonce();
		} else {
			nonce = nonce.add(BigInteger.ONE);
		}
		return nonce;
	}

	public BigInteger getCurrentNonce() {
		return nonce;
	}

	public synchronized void resetNonce(String address) throws InterruptedException, ExecutionException {
		nonce = super.getNonce();
	}

	public synchronized void setNonce(BigInteger value) {
		nonce = value;
	}

	@Override
	public EthSendTransaction executeTransaction(
		BigInteger gasPrice, BigInteger gasLimit, String to,
		String data, BigInteger value) throws ExecutionException, InterruptedException {

		BigInteger nonce = getNonce(); //should execute getNonce from HT

		RawTransaction rawTransaction = RawTransaction.createTransaction(
			nonce,
			gasPrice,
			gasLimit,
			to,
			value,
			data);

		return signAndSend(rawTransaction);
	}

}
