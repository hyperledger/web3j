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
package org.web3j.protocol.geth;

import org.junit.Test;

import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.geth.response.PersonalEcRecover;
import org.web3j.protocol.geth.response.PersonalImportRawKey;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResponseTest extends ResponseTester {

    @Test
    public void testPersonalEcRecover() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"0xadfc0262bbed8c1f4bd24a4a763ac616803a8c54\"\n"
                        + "}");

        PersonalEcRecover personalEcRecover = deserialiseResponse(PersonalEcRecover.class);
        assertThat(
                personalEcRecover.getRecoverAccountId(),
                is("0xadfc0262bbed8c1f4bd24a4a763ac616803a8c54"));
    }

    @Test
    public void testPersonalImportRawKey() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"0xadfc0262bbed8c1f4bd24a4a763ac616803a8c54\"\n"
                        + "}");

        PersonalImportRawKey personalImportRawKey = deserialiseResponse(PersonalImportRawKey.class);
        assertThat(
                personalImportRawKey.getAccountId(),
                is("0xadfc0262bbed8c1f4bd24a4a763ac616803a8c54"));
    }
}
