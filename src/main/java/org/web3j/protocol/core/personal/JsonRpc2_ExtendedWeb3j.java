package org.web3j.protocol.core.personal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.personal.methods.response.EcRecovered;
import org.web3j.protocol.core.personal.methods.response.ImportedRawKey;
import org.web3j.protocol.core.personal.methods.response.LockedAccount;
import org.web3j.protocol.core.personal.methods.response.NewAccount;
import org.web3j.protocol.core.personal.methods.response.Signed;
import org.web3j.protocol.core.personal.methods.response.Transaction;
import org.web3j.protocol.core.personal.methods.response.UnlockedAccount;
import org.web3j.protocol.parity.methods.response.PersonalListAccounts;

public class JsonRpc2_ExtendedWeb3j extends JsonRpc2_0Web3j
implements Personal {

	public JsonRpc2_ExtendedWeb3j(Web3jService web3jService) {
		super(web3jService);
	}

	@Override
	public Request<?, ImportedRawKey> personalImportRawKey(String hexStringNoPrefix, String passphrase){
		return new Request<>(	
				"personal_importRawKey",
				Arrays.asList(hexStringNoPrefix, passphrase),
				ID,
				web3jService,
				ImportedRawKey.class);
	}

	@Override
	public Request<?, PersonalListAccounts> personalListAccounts() {
		return new Request<>(
				"personal_listAccounts",
				Collections.emptyList(),
				ID,
				web3jService,
				PersonalListAccounts.class);
	}

	@Override
	public Request<?, EcRecovered> personalEcRecover(String message, String signature) {
		return new Request<>(	
				"personal_ecRecover",
				Arrays.asList(message, signature),
				ID,
				web3jService,
				EcRecovered.class);
	}

	@Override
	public Request<?, LockedAccount> personalLockAccount(String address) {
		return new Request<>(	
				"personal_lockAccount",
				Arrays.asList(address),
				ID,
				web3jService,
				LockedAccount.class);
	}

	@Override
	public Request<?, UnlockedAccount> personalUnlockAccount(String address, String passphrase, BigInteger duration) {
		List<Object> attributes = new ArrayList<>(3);
        attributes.add(address);
        attributes.add(passphrase);

        if (duration != null) {
            attributes.add(duration.longValue());
        } else {
            attributes.add(null);
        }

		return new Request<>(	
				"personal_unlockAccount",
				attributes,
				ID,
				web3jService,
				UnlockedAccount.class);
	}

	@Override
	public Request<?, NewAccount> personalNewAccount(String password) {
		return new Request<>(	
				"personal_newAccount",
				Arrays.asList(password),
				ID,
				web3jService,
				NewAccount.class);
	}

	@Override
	public Request<?, Transaction> personalSendTransaction(String txJSON, String passphrase) {
		return new Request<>(	
				"personal_sendTransaction",
				Arrays.asList(txJSON, passphrase),
				ID,
				web3jService,
				Transaction.class);
	}

	@Override
	public Request<?, Signed> personalSign(String message, String account) {
		return new Request<>(	
				"personal_sign",
				Arrays.asList(message, account),
				ID,
				web3jService,
				Signed.class);
	}


}