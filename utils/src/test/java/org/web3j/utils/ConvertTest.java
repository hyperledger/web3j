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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertTest {

    @Test
    public void testFromWei() {
        assertEquals(
                Convert.fromWei("21000000000000", Convert.Unit.WEI),
                (new BigDecimal("21000000000000")));
        assertEquals(
                Convert.fromWei("21000000000000", Convert.Unit.KWEI),
                (new BigDecimal("21000000000")));
        assertEquals(
                Convert.fromWei("21000000000000", Convert.Unit.MWEI), (new BigDecimal("21000000")));
        assertEquals(
                Convert.fromWei("21000000000000", Convert.Unit.GWEI), (new BigDecimal("21000")));
        assertEquals(Convert.fromWei("21000000000000", Convert.Unit.SZABO), (new BigDecimal("21")));
        assertEquals(
                Convert.fromWei("21000000000000", Convert.Unit.FINNEY), (new BigDecimal("0.021")));
        assertEquals(
                Convert.fromWei("21000000000000", Convert.Unit.ETHER),
                (new BigDecimal("0.000021")));
        assertEquals(
                Convert.fromWei("21000000000000", Convert.Unit.KETHER),
                (new BigDecimal("0.000000021")));
        assertEquals(
                Convert.fromWei("21000000000000", Convert.Unit.METHER),
                (new BigDecimal("0.000000000021")));
        assertEquals(
                Convert.fromWei("21000000000000", Convert.Unit.GETHER),
                (new BigDecimal("0.000000000000021")));
    }

    @Test
    public void testToWei() {
        assertEquals(Convert.toWei("21", Convert.Unit.WEI), (new BigDecimal("21")));
        assertEquals(Convert.toWei("21", Convert.Unit.KWEI), (new BigDecimal("21000")));
        assertEquals(Convert.toWei("21", Convert.Unit.MWEI), (new BigDecimal("21000000")));
        assertEquals(Convert.toWei("21", Convert.Unit.GWEI), (new BigDecimal("21000000000")));
        assertEquals(Convert.toWei("21", Convert.Unit.SZABO), (new BigDecimal("21000000000000")));
        assertEquals(
                Convert.toWei("21", Convert.Unit.FINNEY), (new BigDecimal("21000000000000000")));
        assertEquals(
                Convert.toWei("21", Convert.Unit.ETHER), (new BigDecimal("21000000000000000000")));
        assertEquals(
                Convert.toWei("21", Convert.Unit.KETHER),
                (new BigDecimal("21000000000000000000000")));
        assertEquals(
                Convert.toWei("21", Convert.Unit.METHER),
                (new BigDecimal("21000000000000000000000000")));
        assertEquals(
                Convert.toWei("21", Convert.Unit.GETHER),
                (new BigDecimal("21000000000000000000000000000")));
    }

    @Test
    public void testUnit() {
        assertEquals(Convert.Unit.fromString("ether"), (Convert.Unit.ETHER));
        assertEquals(Convert.Unit.fromString("ETHER"), (Convert.Unit.ETHER));
        assertEquals(Convert.Unit.fromString("wei"), (Convert.Unit.WEI));
    }
}
