/*
 * Copyright 2022 Web3 Labs Ltd.
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnsUtilsTest {

    @Test
    void isEIP3668WhenDataNull() {
        assertFalse(EnsUtils.isEIP3668(null));
    }

    @Test
    void isEIP3668WhenEmptyOrLessLength() {
        assertFalse(EnsUtils.isEIP3668(""));
        assertFalse(EnsUtils.isEIP3668("123456789"));
    }

    @Test
    void isEIP3668WhenNotRightPrefix() {
        assertFalse(EnsUtils.isEIP3668("123456789012"));
    }

    @Test
    void isEIP3668WhenSuccess() {
        assertTrue(EnsUtils.isEIP3668(EnsUtils.EIP_3668_CCIP_INTERFACE_ID + "some data"));
    }

    @Test
    void getParentWhenSuccess() {
        assertEquals("offchainexample.eth", EnsUtils.getParent("1.offchainexample.eth"));
        assertEquals("eth", EnsUtils.getParent("offchainexample.eth"));
    }

    @Test
    void getParentWhenUrlNullOrEmpty() {
        assertNull(EnsUtils.getParent(null));
        assertNull(EnsUtils.getParent(""));
        assertNull(EnsUtils.getParent(" "));
    }

    @Test
    void getParentWhenUrlWithoutParent() {
        assertNull(EnsUtils.getParent("parent"));
    }
}
