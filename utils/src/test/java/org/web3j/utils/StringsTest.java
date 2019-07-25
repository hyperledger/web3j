/*
 * Copyright 2019 Web3 Labs LTD.
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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
        assertThat(toCsv(Collections.<String>emptyList()), is(""));
        assertThat(toCsv(Collections.singletonList("a")), is("a"));
        assertThat(toCsv(Arrays.asList("a", "b", "c")), is("a, b, c"));
    }

    @Test
    public void testJoin() {
        assertThat(join(Arrays.asList("a", "b"), "|"), is("a|b"));
        assertNull(join(null, "|"));
        assertThat(join(Collections.singletonList("a"), "|"), is("a"));
    }

    @Test
    public void testCapitaliseFirstLetter() {
        assertThat(capitaliseFirstLetter(""), is(""));
        assertThat(capitaliseFirstLetter("a"), is("A"));
        assertThat(capitaliseFirstLetter("aa"), is("Aa"));
        assertThat(capitaliseFirstLetter("A"), is("A"));
        assertThat(capitaliseFirstLetter("Ab"), is("Ab"));
    }

    @Test
    public void testLowercaseFirstLetter() {
        assertThat(lowercaseFirstLetter(""), is(""));
        assertThat(lowercaseFirstLetter("A"), is("a"));
        assertThat(lowercaseFirstLetter("AA"), is("aA"));
        assertThat(lowercaseFirstLetter("a"), is("a"));
        assertThat(lowercaseFirstLetter("aB"), is("aB"));
    }

    @Test
    public void testRepeat() {
        assertThat(repeat('0', 0), is(""));
        assertThat(repeat('1', 3), is("111"));
    }

    @Test
    public void testZeros() {
        assertThat(zeros(0), is(""));
        assertThat(zeros(3), is("000"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testEmptyString() {
        assertTrue(isEmpty(null));
        assertTrue(isEmpty(""));
        assertFalse(isEmpty("hello world"));
    }
}
