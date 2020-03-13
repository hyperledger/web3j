/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.web3j.abi.datatypes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.generated.Uint256;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {

    @Test
    public void testCreation() {

        List<TypeReference<?>> parameters =
                Arrays.<TypeReference<?>>asList(
                        new TypeReference<Address>() {}, new TypeReference<Uint256>() {});
        Event event = new Event("testName", parameters);

        assertEquals(event.getName(), "testName");

        Iterator<TypeReference<?>> expectedParameter = parameters.iterator();
        for (TypeReference<?> actualParameter : event.getParameters()) {
            assertEquals(expectedParameter.next(), actualParameter);
        }

        assertEquals(0, event.getIndexedParameters().size());

        for (TypeReference<?> nonIndexedParameter : event.getNonIndexedParameters()) {
            assertEquals(false, nonIndexedParameter.isIndexed());
        }
    }
}
