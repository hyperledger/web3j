package org.web3j.protocol;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.NetVersion;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CloudIT {


    @Test
    @Disabled("this will not work unless the machine test setup includes the CLI and has a logged-in user")
    public void testWeb3jCloudIsFunctional() throws Exception {
        Web3j web3j = Web3j.build();
        String netVersion =
                web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf("latest"), false).send().getBlock().getHash();
        System.out.println(netVersion);
    }

}
