package org.web3j.ens;

import org.junit.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EnsIT {

    @Test
    public void testEns() throws Exception {

        Web3j web3j = Web3jFactory.build(new HttpService());
        EnsResolver ensResolver = new EnsResolver(web3j);

        assertThat(ensResolver.resolve("web3j.test"),
                is("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
    }
}
