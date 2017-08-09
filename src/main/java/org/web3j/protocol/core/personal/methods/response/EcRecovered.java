package org.web3j.protocol.core.personal.methods.response;

import org.web3j.protocol.core.Response;

public class EcRecovered extends Response<String> {

	public String getEC() {
		return getResult();
	}
}
