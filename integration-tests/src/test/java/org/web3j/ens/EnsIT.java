package org.web3j.ens;

import org.junit.Test;

//import org.web3j.protocol.RequestTester;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@org.junit.Ignore
public class EnsIT { // extends RequestTester {

    private Web3j web3j;

//    @Override
//    protected void initWeb3Client(HttpService httpService) {
//        web3j = Web3jFactory.build(httpService);
//    }

    @Test
    public void testEns() throws Exception {

        ContractResolver contractResolver = new ContractResolver(web3j);

        assertThat(contractResolver.resolve("web3j.test"),
                is("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
    }
}
