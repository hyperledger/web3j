package org.web3j.protocol.parity;

import org.junit.Before;
import org.junit.Test;

import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.methods.response.NewAccountIdentifier;
import org.web3j.protocol.parity.methods.response.PersonalListAccounts;
import org.web3j.protocol.parity.methods.response.PersonalUnlockAccount;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * JSON-RPC 2.0 Integration Tests.
 */
public class ParityIT {

    private static String PASSWORD = "1n5ecur3P@55w0rd";
    private Parity parity;

    public ParityIT() {
        System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
    }

    @Before
    public void setUp() {
        this.parity = ParityFactory.build(new HttpService());
    }

    @Test
    public void testPersonalListAccounts() throws Exception {
        PersonalListAccounts personalListAccounts = parity.personalListAccounts().send();
        assertNotNull(personalListAccounts.getAccountIds());
    }

    @Test
    public void testPersonalNewAccount() throws Exception {
        NewAccountIdentifier newAccountIdentifier = createAccount();
        assertFalse(newAccountIdentifier.getAccountId().isEmpty());
    }

    @Test
    public void testPersonalUnlockAccount() throws Exception {
        NewAccountIdentifier newAccountIdentifier = createAccount();
        PersonalUnlockAccount personalUnlockAccount = parity.personalUnlockAccount(
                newAccountIdentifier.getAccountId(), PASSWORD).send();
        assertTrue(personalUnlockAccount.accountUnlocked());
    }

    private NewAccountIdentifier createAccount() throws Exception {
        return parity.personalNewAccount(PASSWORD).send();
    }
}
