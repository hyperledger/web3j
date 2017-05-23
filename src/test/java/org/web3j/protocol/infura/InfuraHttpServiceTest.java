package org.web3j.protocol.infura;

import cz.msebera.android.httpclient.message.BasicHeader;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.web3j.protocol.infura.InfuraHttpService.buildHeader;

public class InfuraHttpServiceTest {

    @Test
    public void testBuildHeader() {
        assertNull(buildHeader("", false));
        assertNull(buildHeader(null, false));

        assertThat(buildHeader("geth 1.4.19", true).toString(),
                is(new BasicHeader(
                        "Infura-Ethereum-Preferred-Client",
                        "geth 1.4.19").toString()));

        assertThat(buildHeader("geth 1.4.19", false).toString(),
                is(new BasicHeader(
                        "Infura-Ethereum-Preferred-Client",
                        "geth 1.4.19; required=false").toString()));
    }
}
