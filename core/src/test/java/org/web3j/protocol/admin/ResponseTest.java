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
import org.web3j.protocol.admin.methods.response.TxPoolContent;

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

        final BooleanResponse booleanResponse = deserialiseResponse(BooleanResponse.class);
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

        final NewAccountIdentifier newAccountIdentifier =
                deserialiseResponse(NewAccountIdentifier.class);
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

        final PersonalListAccounts personalListAccounts =
                deserialiseResponse(PersonalListAccounts.class);
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

        final PersonalSign personalSign = deserialiseResponse(PersonalSign.class);

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

        final PersonalUnlockAccount personalUnlockAccount =
                deserialiseResponse(PersonalUnlockAccount.class);
        assertTrue(personalUnlockAccount.accountUnlocked());
    }

    @Test
    public void testTxPoolContent() {
        buildResponse(
                "{\n"
                        + "  \"jsonrpc\": \"2.0\",\n"
                        + "  \"id\": 1,\n"
                        + "  \"result\": {\n"
                        + "    \"pending\": {\n"
                        + "      \"0x0032D05F320fa74C871E892F48F0e6387c0Dfe95\": {\n"
                        + "        \"0\": {\n"
                        + "          \"blockHash\": null,\n"
                        + "          \"blockNumber\": null,\n"
                        + "          \"from\": \"0x0032d05f320fa74c871e892f48f0e6387c0dfe95\",\n"
                        + "          \"gas\": \"0x63cad\",\n"
                        + "          \"gasPrice\": \"0x1\",\n"
                        + "          \"hash\": \"0x56cf53cbd377535c14b28cd373fa43d129f501b1a20b36903fd14b747c3f6cf5\",\n"
                        + "          \"input\": \"0x608060405234801561001057600080fd5b5060405161060a38038061060a833981018060405281019080805190602001909291908051820192919060200180519060200190929190805190602001909291908051906020019092919050505084848160008173ffffffffffffffffffffffffffffffffffffffff1614151515610116576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260248152602001807f496e76616c6964206d617374657220636f707920616464726573732070726f7681526020017f696465640000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506000815111156101a35773ffffffffffffffffffffffffffffffffffffffff60005416600080835160208501846127105a03f46040513d6000823e600082141561019f573d81fd5b5050505b5050600081111561036d57600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156102b7578273ffffffffffffffffffffffffffffffffffffffff166108fc829081150290604051600060405180830381858888f1935050505015156102b2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f436f756c64206e6f74207061792073616665206372656174696f6e207769746881526020017f206574686572000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b61036c565b6102d1828483610377640100000000026401000000009004565b151561036b576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f436f756c64206e6f74207061792073616665206372656174696f6e207769746881526020017f20746f6b656e000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b5b5b5050505050610490565b600060608383604051602401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001925050506040516020818303038152906040527fa9059cbb000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff19166020820180517bffffffffffffffffffffffffffffffffffffffffffffffffffffffff838183161783525050505090506000808251602084016000896127105a03f16040513d6000823e3d60008114610473576020811461047b5760009450610485565b829450610485565b8151158315171594505b505050509392505050565b61016b8061049f6000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680634555d5c91461008b5780635c60da1b146100b6575b73ffffffffffffffffffffffffffffffffffffffff600054163660008037600080366000845af43d6000803e6000811415610086573d6000fd5b3d6000f35b34801561009757600080fd5b506100a061010d565b6040518082815260200191505060405180910390f35b3480156100c257600080fd5b506100cb610116565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60006002905090565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050905600a165627a7a7230582007fffd557dfc8c4d2fdf56ba6381a6ce5b65b6260e1492d87f26c6d4f1d0410800290000000000000000000000008942595a2dc5181df0465af0d7be08c8f23c93af00000000000000000000000000000000000000000000000000000000000000a00000000000000000000000004ba9692da667218aa968ced8cbe59fe193e0d7860000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000006968500000000000000000000000000000000000000000000000000000000000001240ec78d9e00000000000000000000000000000000000000000000000000000000000000800000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e0000000000000000000000000000000000000000000000000000000000000000200000000000000000000000006ea3e1c44f2ad3e54cf32a25eb9fab965fc010f0000000000000000000000009542597a73c7371f07b4532bda22d39cfc4912180000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\",\n"
                        + "          \"nonce\": \"0x0\",\n"
                        + "          \"to\": null,\n"
                        + "          \"transactionIndex\": null,\n"
                        + "          \"value\": \"0x0\",\n"
                        + "          \"v\": \"0x1b\",\n"
                        + "          \"r\": \"0x1b53058de9ed675d8b6583c559d23013e941905dad28dfd95ba3ff4f38ace0\",\n"
                        + "          \"s\": \"0x8631cfd224ee69034f8040d2297a28229ee91cd5acdabc7f1de5be62220\"\n"
                        + "        }\n"
                        + "      }\n"
                        + "    },\n"
                        + "    \"queued\": {\n"
                        + "      \"0x00Bf700CeB382877F8bFa38b05fcC81126f4f228\": {\n"
                        + "        \"49\": {\n"
                        + "          \"blockHash\": null,\n"
                        + "          \"blockNumber\": null,\n"
                        + "          \"from\": \"0x00bf700ceb382877f8bfa38b05fcc81126f4f228\",\n"
                        + "          \"gas\": \"0xfa00\",\n"
                        + "          \"gasPrice\": \"0x3b9aca00\",\n"
                        + "          \"hash\": \"0xa87ab980c4d277de6c4faf2670a2ec1b6e577482e582c8082d208b7e630cf395\",\n"
                        + "          \"input\": \"0xa6ab36f2000000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000000000a0000000000000000000000009590f23a286dade6fbf778ca651ad560d4e02fdc\",\n"
                        + "          \"nonce\": \"0x31\",\n"
                        + "          \"to\": \"0x974d7219184a41d4f5e3664ddce808c7853d3ab4\",\n"
                        + "          \"transactionIndex\": null,\n"
                        + "          \"value\": \"0x0\",\n"
                        + "          \"v\": \"0x2b\",\n"
                        + "          \"r\": \"0xc747162f13dd24fd03dd00c66c5c3d222595908319333c2b08e7f7def8dad2e7\",\n"
                        + "          \"s\": \"0x8e6a2c4e3794d782d51f950e0ba546f44412e8c2409bd39149d78d7ca316e8a\"\n"
                        + "        }\n"
                        + "      }\n"
                        + "    }\n"
                        + "  }\n"
                        + "}");

        TxPoolContent content = deserialiseResponse(TxPoolContent.class);

        assertEquals(
                content.getResult().getPendingTransactions().get(0).getFrom(),
                "0x0032d05f320fa74c871e892f48f0e6387c0dfe95");
        assertEquals(
                content.getResult().getQueuedTransactions().get(0).getFrom(),
                "0x00bf700ceb382877f8bfa38b05fcc81126f4f228");
    }
}
