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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.web3j.ens.NameHash.nameHash;
import static org.web3j.ens.NameHash.normalise;

public class NameHashTest {

    @Test
    public void testNameHash() {
        assertEquals(
                nameHash(""),
                ("0x0000000000000000000000000000000000000000000000000000000000000000"));

        assertEquals(
                nameHash("eth"),
                ("0x93cdeb708b7545dc668eb9280176169d1c33cfd8ed6f04690a0bcc88a93fc4ae"));

        assertEquals(
                nameHash("foo.eth"),
                ("0xde9b09fd7c5f901e23a3f19fecc54828e9c848539801e86591bd9801b019f84f"));
    }

    @Test
    public void testNormalise() {
        assertEquals(normalise("foo"), ("foo"));
        assertEquals(normalise("foo.bar.baz.eth"), ("foo.bar.baz.eth"));
        assertEquals(normalise("fOo.eth"), ("foo.eth"));
        assertEquals(normalise("foo-bar.eth"), ("foo-bar.eth"));
    }

    @Test
    public void testNormaliseInvalid() {
        testInvalidName("foo..bar");
        testInvalidName("ba\\u007Fr.eth");
        testInvalidName("-baz.eth-");
        testInvalidName("foo_bar.eth");
    }

    private void testInvalidName(final String ensName) {
        try {
            normalise(ensName);
            fail("Name should not be valid");
        } catch (final EnsResolutionException e) {
            // expected
        }
    }
}
