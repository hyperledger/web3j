package org.web3j.protocol.parity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import org.web3j.protocol.parity.methods.response.ParityAllAccountsInfo;

public class EqualsVerifierParityResponseTest {

    @Test
    public void testAccountsInfo() {
        EqualsVerifier.forClass(ParityAllAccountsInfo.AccountsInfo.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }
}
