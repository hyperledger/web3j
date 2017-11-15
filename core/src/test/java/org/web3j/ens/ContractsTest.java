package org.web3j.ens;

import org.junit.Test;

import org.web3j.tx.ChainId;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.web3j.ens.Contracts.MAINNET;
import static org.web3j.ens.Contracts.RINKEBY;
import static org.web3j.ens.Contracts.ROPSTEN;
import static org.web3j.ens.Contracts.resolveRegistryContract;

public class ContractsTest {

    @Test
    public void testResolveRegistryContract() {
        assertThat(resolveRegistryContract(ChainId.MAINNET + ""), is(MAINNET));
        assertThat(resolveRegistryContract(ChainId.ROPSTEN + ""), is(ROPSTEN));
        assertThat(resolveRegistryContract(ChainId.RINKEBY + ""), is(RINKEBY));
    }

    @Test(expected = EnsResolutionException.class)
    public void testResolveRegistryContractInvalid() {
        resolveRegistryContract(ChainId.NONE + "");
    }
}
