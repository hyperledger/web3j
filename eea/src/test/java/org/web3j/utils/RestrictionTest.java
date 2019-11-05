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
package org.web3j.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.web3j.utils.Restriction.RESTRICTED;
import static org.web3j.utils.Restriction.UNRESTRICTED;

public class RestrictionTest {

    @Test
    public void getRestriction() {
        assertEquals(RESTRICTED.getRestriction(), "restricted");
        assertEquals(UNRESTRICTED.getRestriction(), "unrestricted");
    }

    @Test
    public void tesFromString() {
        assertEquals(RESTRICTED, Restriction.fromString("restricted"));
        assertEquals(UNRESTRICTED, Restriction.fromString("unrestricted"));
    }
}
