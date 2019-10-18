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
package org.web3j.protocol.scenarios;

import java.math.BigInteger;

import org.junit.Test;

import org.web3j.generated.SimpleStorage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class SimpleStorageContractIT extends Scenario {

    @Test
    public void testSimpleStorageContract() throws Exception {
        BigInteger value = BigInteger.valueOf(1000L);
        SimpleStorage simpleStorage =
                SimpleStorage.deploy(web3j, ALICE, GAS_PRICE, GAS_LIMIT).send();
        assertNotNull(simpleStorage.set(value).send());
        assertThat(simpleStorage.get().send(), is(value));
    }
}
