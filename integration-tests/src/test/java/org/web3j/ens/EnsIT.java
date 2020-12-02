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
package org.web3j.ens;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.protocol.Web3j;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
@EVMTest
public class EnsIT {

    @Test
    public void testEns(Web3j web3j) throws Exception {

        EnsResolver ensResolver = new EnsResolver(web3j);

        assertEquals(
                ensResolver.resolve("web3j.test"), ("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
    }
}
