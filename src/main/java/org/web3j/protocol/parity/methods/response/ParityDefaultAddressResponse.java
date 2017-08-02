package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 *
 * @author ivaylo
 */
public class ParityDefaultAddressResponse extends Response<String>{
    public String getAddress(){
        return getResult();
    }
}
