package org.web3j.ens;

import org.web3j.tx.ChainIdLong;

/**
 * ENS registry contract addresses.
 */
public class Contracts {

    public static final String MAINNET = "0x314159265dd8dbb310642f98f50c066173c1259b";
    public static final String ROPSTEN = "0x112234455c3a32fd11230c42e7bccd4a84e02010";
    public static final String RINKEBY = "0xe7410170f87102df0055eb195163a03b7f2bff4a";

    public static String resolveRegistryContract(String chainId) {
        final Long chainIdLong = Long.parseLong(chainId);
        if (chainIdLong.equals(ChainIdLong.MAINNET)) {
            return MAINNET;
        } else if (chainIdLong.equals(ChainIdLong.ROPSTEN)) {
            return ROPSTEN;
        } else if (chainIdLong.equals(ChainIdLong.RINKEBY)) {
            return RINKEBY;
        } else {
            throw new EnsResolutionException(
                "Unable to resolve ENS registry contract for network id: " + chainId);
        }
    }
}
