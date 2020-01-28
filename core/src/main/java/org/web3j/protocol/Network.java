package org.web3j.protocol;

public enum Network {
    MAINNET("mainnet"),
    ROPSTEN("ropsten"),
    KOVAN("kovan"),
    GORLI("gorli"),
    RINKEBY("rinkeby");

    public String getNetworkName() {
        return network;
    }

    String network;

    Network(final String network) {
        this.network = network;
    }
}
