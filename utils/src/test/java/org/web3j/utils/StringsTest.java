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

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.web3j.utils.Strings.capitaliseFirstLetter;
import static org.web3j.utils.Strings.isEmpty;
import static org.web3j.utils.Strings.join;
import static org.web3j.utils.Strings.lowercaseFirstLetter;
import static org.web3j.utils.Strings.repeat;
import static org.web3j.utils.Strings.toCsv;
import static org.web3j.utils.Strings.zeros;

public class StringsTest {

    @Test
    public void testToCsv() {
        assertEquals(toCsv(Collections.<String>emptyList()), (""));
        assertEquals(toCsv(Collections.singletonList("a")), ("a"));
        assertEquals(toCsv(Arrays.asList("a", "b", "c")), ("a, b, c"));
    }

    @Test
    public void testJoin() {
        assertEquals(join(Arrays.asList("a", "b"), "|"), ("a|b"));
        assertNull(join(null, "|"));
        assertEquals(join(Collections.singletonList("a"), "|"), ("a"));
    }

    @Test
    public void testCapitaliseFirstLetter() {
        assertEquals(capitaliseFirstLetter(""), (""));
        assertEquals(capitaliseFirstLetter("a"), ("A"));
        assertEquals(capitaliseFirstLetter("aa"), ("Aa"));
        assertEquals(capitaliseFirstLetter("A"), ("A"));
        assertEquals(capitaliseFirstLetter("Ab"), ("Ab"));
    }

    @Test
    public void testLowercaseFirstLetter() {
        assertEquals(lowercaseFirstLetter(""), (""));
        assertEquals(lowercaseFirstLetter("A"), ("a"));
        assertEquals(lowercaseFirstLetter("AA"), ("aA"));
        assertEquals(lowercaseFirstLetter("a"), ("a"));
        assertEquals(lowercaseFirstLetter("aB"), ("aB"));
    }

    @Test
    public void testRepeat() {
        assertEquals(repeat('0', 0), (""));
        assertEquals(repeat('1', 3), ("111"));
    }

    @Test
    public void testZeros() {
        assertEquals(zeros(0), (""));
        assertEquals(zeros(3), ("000"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testEmptyString() {
        assertTrue(isEmpty(null));
        assertTrue(isEmpty(""));
        assertFalse(isEmpty("hello world"));
    }
}
