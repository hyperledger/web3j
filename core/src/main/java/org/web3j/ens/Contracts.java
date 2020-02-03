package org.web3j.ens;

import org.web3j.tx.ChainId;

/**
 * ENS registry contract addresses.
 */
public class Contracts {

    public static final String MAINNET = "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e";
    public static final String ROPSTEN = "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e";
    public static final String RINKEBY = "0x00000000000C2E074eC69A0dFb2997BA6C7d2e1e";

    public static String resolveRegistryContract(String chainId) {
        switch (Byte.valueOf(chainId)) {
            case ChainId.MAINNET:
                return MAINNET;
            case ChainId.ROPSTEN:
                return ROPSTEN;
            case ChainId.RINKEBY:
                return RINKEBY;
            default:
                throw new EnsResolutionException(
                        "Unable to resolve ENS registry contract for network id: " + chainId);
        }
    }
}
