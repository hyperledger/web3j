package org.web3j.ens;

import org.junit.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EnsIT {

    @Test
    public void testEns() throws Exception {

        Web3j web3j = Web3j.build(new HttpService());
        ContractResolver contractResolver = new ContractResolver(web3j);

        assertThat(contractResolver.resolve("web3j.test"),
                is("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
    }
}
