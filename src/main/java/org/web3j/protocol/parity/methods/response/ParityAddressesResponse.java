package org.web3j.protocol.parity.methods.response;

import java.util.ArrayList;

import org.web3j.protocol.core.Response;

/**
 * parity_getDappAddresses
 * parity_getNewDappsAddresses
 * parity_importGethAccounts
 * parity_listGethAccounts.
 */
public class ParityAddressesResponse extends Response<ArrayList<String>>{
    public ArrayList<String> getAddresses(){
        return getResult();
    }
}
