package org.web3j.protocol.core.personal.methods.response;

import org.web3j.protocol.core.Response;

public class ImportedRawKey extends Response<String> {

	public String getAccountId() {
		return getResult();
	}
	
}
