package org.web3j.protocol.admin;

import java.util.Arrays;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.parity.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalSign;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;

/**
 * Parity/Geth Shared Protocol Response tests.
 */
public class ResponseTest extends ResponseTester{    

    @Test
    public void testBooleanResponse() {
        buildResponse("{\n"
                + "    \"jsonrpc\":\"2.0\",\n"
                + "    \"id\":22,\n"
                + "    \"result\":true\n"
                + "}");

        BooleanResponse booleanResponse =
                deserialiseResponse(BooleanResponse.class);
        assertTrue(booleanResponse.success());
    }

    @Test
    public void testNewAccountIdentifier() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 22,\n"
                + "    \"result\": \"0x8f0227d45853a50eefd48dd4fec25d5b3fd2295e\"\n"
                + "}");

        NewAccountIdentifier newAccountIdentifier = deserialiseResponse(NewAccountIdentifier.class);
        assertThat(newAccountIdentifier.getAccountId(),
                is("0x8f0227d45853a50eefd48dd4fec25d5b3fd2295e"));
    }

    @Test
    public void testPersonalListAccounts() {
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 83,\n"
                + "    \"result\": [\n"
                + "        \"0x7bf87721a96849d168de02fd6ea5986a3a147383\",\n"
                + "        \"0xca807a90fd64deed760fb98bf0869b475c469348\"\n"
                + "    ]\n"
                + "}\n");

        PersonalListAccounts personalListAccounts = deserialiseResponse(PersonalListAccounts.class);
        assertThat(personalListAccounts.getAccountIds(),
                equalTo(Arrays.asList(
                        "0x7bf87721a96849d168de02fd6ea5986a3a147383",
                        "0xca807a90fd64deed760fb98bf0869b475c469348"
                )));
    }

    @Test
    public void testPersonalSign() {
        //CHECKSTYLE:OFF
        buildResponse("{\n"
                + "    \"jsonrpc\": \"2.0\",\n"
                + "    \"id\": 1,\n"
                + "    \"result\": \"0xf1aabd691c887ee5c98af871239534f194a51fdeb801b1601d77c45afa74dae67ddd81aa5bb8a54b7974ef5be10b55a8535b040883501f76d14cb74e05e5635d1c\"\n"
                + "}");
        //CHECKSTYLE:ON

        PersonalSign personalSign = deserialiseResponse(PersonalSign.class);
        //CHECKSTYLE:OFF
        assertThat(personalSign.getSignedMessage(),is("0xf1aabd691c887ee5c98af871239534f194a51fdeb801b1601d77c45afa74dae67ddd81aa5bb8a54b7974ef5be10b55a8535b040883501f76d14cb74e05e5635d1c"));
        //CHECKSTYLE:ON
    }

    @Test
    public void testPersonalUnlockAccount() {
        buildResponse("{\n"
                + "    \"jsonrpc\":\"2.0\",\n"
                + "    \"id\":22,\n"
                + "    \"result\":true\n"
                + "}");

        PersonalUnlockAccount personalUnlockAccount =
                deserialiseResponse(PersonalUnlockAccount.class);
        assertTrue(personalUnlockAccount.accountUnlocked());
    }
    
}
