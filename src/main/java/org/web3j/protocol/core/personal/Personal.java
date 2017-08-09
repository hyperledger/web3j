package org.web3j.protocol.core.personal;

import java.math.BigInteger;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.personal.methods.response.EcRecovered;
import org.web3j.protocol.core.personal.methods.response.ImportedRawKey;
import org.web3j.protocol.core.personal.methods.response.LockedAccount;
import org.web3j.protocol.core.personal.methods.response.NewAccount;
import org.web3j.protocol.core.personal.methods.response.Signed;
import org.web3j.protocol.core.personal.methods.response.Transaction;
import org.web3j.protocol.core.personal.methods.response.UnlockedAccount;
import org.web3j.protocol.parity.methods.response.PersonalListAccounts;

public interface Personal{

	static JsonRpc2_ExtendedWeb3j build(Web3jService web3jService) {
		return new JsonRpc2_ExtendedWeb3j(web3jService);
	}

	// PERSONAL
	public Request<?, ImportedRawKey> personalImportRawKey(String hexStringNoPrefix, String passphrase);
	public Request<?, NewAccount> personalNewAccount(String password);	
	public Request<?, PersonalListAccounts> personalListAccounts();
	public Request<?, LockedAccount> personalLockAccount(String address);
	public Request<?, UnlockedAccount> personalUnlockAccount(String address, String passphrase, BigInteger duration);
	public Request<?, Transaction> personalSendTransaction(String txJSON, String passphrase);
	public Request<?, Signed> personalSign(String message, String account);	
	public Request<?, EcRecovered> personalEcRecover(String message, String signature);
	
}

