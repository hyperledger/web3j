package org.web3j.protocol.infura;


import org.apache.http.message.BasicHeader;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.web3j.protocol.infura.InfuraHttpService.buildHeader;

public class InfuraHttpServiceTest {

    @Test
    public void testBuildHeader() {
        assertFalse(buildHeader("", false).isPresent());
        assertFalse(buildHeader(null, false).isPresent());

        assertThat(buildHeader("geth 1.4.19", true).get().toString(),
                is(new BasicHeader(
                        "Infura-Ethereum-Preferred-Client",
                        "geth 1.4.19").toString()));

        assertThat(buildHeader("geth 1.4.19", false).get().toString(),
                is(new BasicHeader(
                        "Infura-Ethereum-Preferred-Client",
                        "geth 1.4.19; required=false").toString()));
    }
}
