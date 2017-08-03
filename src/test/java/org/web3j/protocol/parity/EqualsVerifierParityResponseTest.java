package org.web3j.protocol.parity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import org.web3j.protocol.parity.methods.response.ParityAllAccountsInfo;
import org.web3j.protocol.parity.methods.response.SignerRequestsToConfirm;

public class EqualsVerifierParityResponseTest {

    @Test
    public void testTransactionRequestType() {
        EqualsVerifier.forClass(SignerRequestsToConfirm.TransactionRequestType.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testAccountsInfo() {
        EqualsVerifier.forClass(ParityAllAccountsInfo.AccountsInfo.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testSignRequest() {
        EqualsVerifier.forClass(SignerRequestsToConfirm.SignRequest.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testRequestsToConfirm() {
        EqualsVerifier.forClass(SignerRequestsToConfirm.RequestsToConfirm.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testTransactionPayload() {
        EqualsVerifier.forClass(SignerRequestsToConfirm.TransactionPayload.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testSignPayload() {
        EqualsVerifier.forClass(SignerRequestsToConfirm.SignPayload.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }
}
