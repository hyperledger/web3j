package org.web3j.protocol.parity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import org.web3j.protocol.parity.methods.response.PersonalAccountsInfo;
import org.web3j.protocol.parity.methods.response.PersonalRequestsToConfirm;

public class EqualsVerifierParityResponseTest {

    @Test
    public void testTransactionRequestType() {
        EqualsVerifier.forClass(PersonalRequestsToConfirm.TransactionRequestType.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testAccountsInfo() {
        EqualsVerifier.forClass(PersonalAccountsInfo.AccountsInfo.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testSignRequest() {
        EqualsVerifier.forClass(PersonalRequestsToConfirm.SignRequest.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testRequestsToConfirm() {
        EqualsVerifier.forClass(PersonalRequestsToConfirm.RequestsToConfirm.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testTransactionPayload() {
        EqualsVerifier.forClass(PersonalRequestsToConfirm.TransactionPayload.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testSignPayload() {
        EqualsVerifier.forClass(PersonalRequestsToConfirm.SignPayload.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }
}
