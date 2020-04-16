/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.admin;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalSign;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Parity/Geth Shared Protocol Response tests. */
public class ResponseTest extends ResponseTester {

    @Test
    public void testBooleanResponse() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\":\"2.0\",\n"
                        + "    \"id\":22,\n"
                        + "    \"result\":true\n"
                        + "}");

        BooleanResponse booleanResponse = deserialiseResponse(BooleanResponse.class);
        assertTrue(booleanResponse.success());
    }

    @Test
    public void testNewAccountIdentifier() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 22,\n"
                        + "    \"result\": \"0x8f0227d45853a50eefd48dd4fec25d5b3fd2295e\"\n"
                        + "}");

        NewAccountIdentifier newAccountIdentifier = deserialiseResponse(NewAccountIdentifier.class);
        assertEquals(
                newAccountIdentifier.getAccountId(),
                ("0x8f0227d45853a50eefd48dd4fec25d5b3fd2295e"));
    }

    @Test
    public void testPersonalListAccounts() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 83,\n"
                        + "    \"result\": [\n"
                        + "        \"0x7bf87721a96849d168de02fd6ea5986a3a147383\",\n"
                        + "        \"0xca807a90fd64deed760fb98bf0869b475c469348\"\n"
                        + "    ]\n"
                        + "}\n");

        PersonalListAccounts personalListAccounts = deserialiseResponse(PersonalListAccounts.class);
        assertEquals(
                personalListAccounts.getAccountIds(),
                (Arrays.asList(
                        "0x7bf87721a96849d168de02fd6ea5986a3a147383",
                        "0xca807a90fd64deed760fb98bf0869b475c469348")));
    }

    @Test
    public void testPersonalSign() {

        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"0xf1aabd691c887ee5c98af871239534f194a51fdeb801b1601d77c45afa74dae67ddd81aa5bb8a54b7974ef5be10b55a8535b040883501f76d14cb74e05e5635d1c\"\n"
                        + "}");

        PersonalSign personalSign = deserialiseResponse(PersonalSign.class);

        assertEquals(
                personalSign.getSignedMessage(),
                ("0xf1aabd691c887ee5c98af871239534f194a51fdeb801b1601d77c45afa74dae67ddd81aa5bb8a54b7974ef5be10b55a8535b040883501f76d14cb74e05e5635d1c"));
    }

    @Test
    public void testPersonalUnlockAccount() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\":\"2.0\",\n"
                        + "    \"id\":22,\n"
                        + "    \"result\":true\n"
                        + "}");

        PersonalUnlockAccount personalUnlockAccount =
                deserialiseResponse(PersonalUnlockAccount.class);
        assertTrue(personalUnlockAccount.accountUnlocked());
    }
}
