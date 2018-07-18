package org.web3j.ens;

import org.junit.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.TestParameters;
import org.web3j.protocol.http.HttpService;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class EnsIT {

    @Test
    public void testEns() throws Exception {

        Web3j web3j = Web3j.build(new HttpService(TestParameters.TEST_URL));

        assumeThat("Skipping testEns() because we are still syncing, which means we will NOT be "
                        + "able to accurately do EnsResolver#resolve()",
                web3j.ethSyncing().send().isSyncing(),
                allOf(
                        notNullValue(),
                        is(false)
                ));

        EnsResolver ensResolver = new EnsResolver(web3j);
        assertThat(ensResolver.resolve("web3j.test"),
                is("0xe7fdbbac9325508269d772982cc68103f79d9693"));
    }
}
