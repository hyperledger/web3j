package org.web3j.protocol.core.personal.methods.response;

import org.web3j.protocol.core.Response;

public class Transaction extends Response<String> {

	public String getTransactionId() {
		return getResult();
	}
}
