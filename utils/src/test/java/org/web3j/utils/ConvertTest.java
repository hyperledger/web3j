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

import java.math.BigDecimal;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConvertTest {

    @Test
    public void testFromWei() {
        assertThat(
                Convert.fromWei("21000000000000", Convert.Unit.WEI),
                is(new BigDecimal("21000000000000")));
        assertThat(
                Convert.fromWei("21000000000000", Convert.Unit.KWEI),
                is(new BigDecimal("21000000000")));
        assertThat(
                Convert.fromWei("21000000000000", Convert.Unit.MWEI),
                is(new BigDecimal("21000000")));
        assertThat(
                Convert.fromWei("21000000000000", Convert.Unit.GWEI), is(new BigDecimal("21000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.SZABO), is(new BigDecimal("21")));
        assertThat(
                Convert.fromWei("21000000000000", Convert.Unit.FINNEY),
                is(new BigDecimal("0.021")));
        assertThat(
                Convert.fromWei("21000000000000", Convert.Unit.ETHER),
                is(new BigDecimal("0.000021")));
        assertThat(
                Convert.fromWei("21000000000000", Convert.Unit.KETHER),
                is(new BigDecimal("0.000000021")));
        assertThat(
                Convert.fromWei("21000000000000", Convert.Unit.METHER),
                is(new BigDecimal("0.000000000021")));
        assertThat(
                Convert.fromWei("21000000000000", Convert.Unit.GETHER),
                is(new BigDecimal("0.000000000000021")));
    }

    @Test
    public void testToWei() {
        assertThat(Convert.toWei("21", Convert.Unit.WEI), is(new BigDecimal("21")));
        assertThat(Convert.toWei("21", Convert.Unit.KWEI), is(new BigDecimal("21000")));
        assertThat(Convert.toWei("21", Convert.Unit.MWEI), is(new BigDecimal("21000000")));
        assertThat(Convert.toWei("21", Convert.Unit.GWEI), is(new BigDecimal("21000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.SZABO), is(new BigDecimal("21000000000000")));
        assertThat(
                Convert.toWei("21", Convert.Unit.FINNEY), is(new BigDecimal("21000000000000000")));
        assertThat(
                Convert.toWei("21", Convert.Unit.ETHER),
                is(new BigDecimal("21000000000000000000")));
        assertThat(
                Convert.toWei("21", Convert.Unit.KETHER),
                is(new BigDecimal("21000000000000000000000")));
        assertThat(
                Convert.toWei("21", Convert.Unit.METHER),
                is(new BigDecimal("21000000000000000000000000")));
        assertThat(
                Convert.toWei("21", Convert.Unit.GETHER),
                is(new BigDecimal("21000000000000000000000000000")));
    }

    @Test
    public void testUnit() {
        assertThat(Convert.Unit.fromString("ether"), is(Convert.Unit.ETHER));
        assertThat(Convert.Unit.fromString("ETHER"), is(Convert.Unit.ETHER));
        assertThat(Convert.Unit.fromString("wei"), is(Convert.Unit.WEI));
    }
}
