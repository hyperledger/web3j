package org.web3j.tx;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by fernando on 2/1/17.
 */
public class HighThroughputTransactionManager extends RawTransactionManager {

	private static HashMap<String,HighThroughputTransactionManager> map = new HashMap<>();
	private String accountAddress;
	private BigInteger nonce = BigInteger.valueOf(-1);
	private HashMap<BigInteger, Object> transactions;
	private Web3j web3j;
	private byte chainId;

	public static HighThroughputTransactionManager getInstance(Web3j web3j, Credentials credentials)
	{
		if (map.containsKey(credentials.getAddress())) {
			return map.get(credentials.getAddress());
		} else {
			HighThroughputTransactionManager instance = new HighThroughputTransactionManager(web3j, credentials);
			map.put(credentials.getAddress(), instance);

			return instance;
		}
	}

	public HighThroughputTransactionManager(Web3j web3j, Credentials credentials, byte chainId) {
		super(web3j, credentials, chainId);
		accountAddress = credentials.getAddress();
		this.web3j = web3j;
		this.chainId = chainId;
	}

	public HighThroughputTransactionManager(Web3j web3j, Credentials credentials) {
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
