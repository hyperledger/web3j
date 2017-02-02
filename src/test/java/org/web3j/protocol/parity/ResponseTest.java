package org.web3j.protocol.parity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.core.methods.response.VoidResponse;
import org.web3j.protocol.parity.methods.response.NewAccountIdentifier;
import org.web3j.protocol.parity.methods.response.PersonalAccountsInfo;
import org.web3j.protocol.parity.methods.response.PersonalListAccounts;
import org.web3j.protocol.parity.methods.response.PersonalRejectRequest;
import org.web3j.protocol.parity.methods.response.PersonalRequestsToConfirm;
import org.web3j.protocol.parity.methods.response.PersonalSignerEnabled;
import org.web3j.protocol.parity.methods.response.PersonalUnlockAccount;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Parity Protocol Response tests.
 */
public class ResponseTest extends ResponseTester {

    @Test
    public void testPersonalSignerEnabled() {
        buildResponse("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 1,\n" +
                "    \"result\": true\n" +
                "}");

        PersonalSignerEnabled personalSignerEnabled = deserialiseResponse(
                PersonalSignerEnabled.class);
        assertTrue(personalSignerEnabled.isSignerEnabled());
    }

    @Test
    public void testPersonalListAccounts() {
        buildResponse("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 83,\n" +
                "    \"result\": [\n" +
                "        \"0x7bf87721a96849d168de02fd6ea5986a3a147383\",\n" +
                "        \"0xca807a90fd64deed760fb98bf0869b475c469348\"\n" +
                "    ]\n" +
                "}\n");

        PersonalListAccounts personalListAccounts = deserialiseResponse(PersonalListAccounts.class);
        assertThat(personalListAccounts.getAccountIds(),
                equalTo(Arrays.asList(
                        "0x7bf87721a96849d168de02fd6ea5986a3a147383",
                        "0xca807a90fd64deed760fb98bf0869b475c469348"
                )));
    }

    @Test
    public void testNewAccountIdentifier() {
        buildResponse("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 22,\n" +
                "    \"result\": \"0x8f0227d45853a50eefd48dd4fec25d5b3fd2295e\"\n" +
                "}");

        NewAccountIdentifier newAccountIdentifier = deserialiseResponse(NewAccountIdentifier.class);
        assertThat(newAccountIdentifier.getAccountId(),
                is("0x8f0227d45853a50eefd48dd4fec25d5b3fd2295e"));
    }

    @Test
    public void testPersonalUnlockAccount() {
        buildResponse("{\n" +
                "    \"jsonrpc\":\"2.0\",\n" +
                "    \"id\":22,\n" +
                "    \"result\":true\n" +
                "}");

        PersonalUnlockAccount personalUnlockAccount = deserialiseResponse(PersonalUnlockAccount.class);
        assertTrue(personalUnlockAccount.accountUnlocked());
    }

    @Test
    public void testVoidResponse() {
        buildResponse("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\":22,\n" +
                "    \"result\":null\n" +
                "}");

        VoidResponse voidResponse = deserialiseResponse(VoidResponse.class);
        assertTrue(voidResponse.isValid());
    }

    @Test
    public void testPersonalAccountsInfo() {
        buildResponse("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\":22,\n" +
                "    \"result\":{\n" +
                "        \"0xfc390d8a8ddb591b010fda52f4db4945742c3809\":{\n" +
                "            \"name\":\"Savings\",\n" +
                "            \"uuid\":\"7fee0393-7571-2b4f-8672-862fea01a4a0\",\n" +
                "            \"meta\":{}\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Map<String, PersonalAccountsInfo.AccountsInfo> accountsInfoMap = new HashMap<>(1);
        accountsInfoMap.put("0xfc390d8a8ddb591b010fda52f4db4945742c3809",
                new PersonalAccountsInfo.AccountsInfo(
                        "Savings",
                        "7fee0393-7571-2b4f-8672-862fea01a4a0",
                        Collections.<String, Object>emptyMap()
                ));

        PersonalAccountsInfo personalAccountsInfo = deserialiseResponse(PersonalAccountsInfo.class);
        assertThat(personalAccountsInfo.getAccountsInfo(), equalTo(accountsInfoMap));
    }

    @Test
    public void testPersonalRequestsToConfirm() {
        buildResponse("{\"jsonrpc\":\"2.0\",\"result\":[" +
        "{\"id\":\"0x1\",\"payload\":{\"transaction\":{\"data\":\"0x\",\"from\":\"0x0000000000000000000000000000000000000001\",\"gas\":\"0x989680\",\"gasPrice\":\"0x2710\",\"nonce\":null,\"to\":\"0xd46e8dd67c5d32be8058bb8eb970870f07244567\",\"value\":\"0x1\"}}}," +
        "{\"id\":\"0x2\",\"payload\":{\"sign\":{\"address\":\"0x0000000000000000000000000000000000000001\",\"hash\":\"0x0000000000000000000000000000000000000000000000000000000000000005\"}}}" +
        "],\"id\":1}"
        );

        List<PersonalRequestsToConfirm.RequestsToConfirm> requestsToConfirm =
                Arrays.asList(
                        new PersonalRequestsToConfirm.RequestsToConfirm(
                                "0x1",
                                new PersonalRequestsToConfirm.TransactionPayload(
                                        new PersonalRequestsToConfirm.TransactionRequestType(
                                                "0x0000000000000000000000000000000000000001",
                                                "0xd46e8dd67c5d32be8058bb8eb970870f07244567",
                                                "0x989680",
                                                "0x2710",
                                                "0x1",
                                                "0x",
                                                null
                                        )
                                )
                            ),
                        new PersonalRequestsToConfirm.RequestsToConfirm(
                                "0x2",
                                new PersonalRequestsToConfirm.SignPayload(
                                        new PersonalRequestsToConfirm.SignRequest(
                                                "0x0000000000000000000000000000000000000001",
                                                "0x0000000000000000000000000000000000000000000000000000000000000005"
                                        )
                                )
                        )
                );

        PersonalRequestsToConfirm personalRequestsToConfirm =
                deserialiseResponse(PersonalRequestsToConfirm.class);
        assertThat(personalRequestsToConfirm.getRequestsToConfirm(),equalTo(requestsToConfirm));
    }

    @Test
    public void testPersonalRejectRequest() {
        buildResponse("{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\":22,\n" +
                "    \"result\":true\n" +
                "}");

        PersonalRejectRequest personalRejectRequest =
                deserialiseResponse(PersonalRejectRequest.class);
        assertTrue(personalRejectRequest.isRejected());
    }
}
