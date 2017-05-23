package org.web3j.utils;

import java.math.BigDecimal;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ConvertTest {

    @Test
    public void testFromWei() {
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.WEI),
                is(new BigDecimal("21000000000000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.KWEI),
                is(new BigDecimal("21000000000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.MWEI),
                is(new BigDecimal("21000000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.GWEI),
                is(new BigDecimal("21000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.SZABO),
                is(new BigDecimal("21")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.FINNEY),
                is(new BigDecimal("0.021")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.ETHER),
                is(new BigDecimal("0.000021")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.KETHER),
                is(new BigDecimal("0.000000021")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.METHER),
                is(new BigDecimal("0.000000000021")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.GETHER),
                is(new BigDecimal("0.000000000000021")));
    }

    @Test
    public void testToWei() {
        assertThat(Convert.toWei("21", Convert.Unit.WEI), is(new BigDecimal("21")));
        assertThat(Convert.toWei("21", Convert.Unit.KWEI), is(new BigDecimal("21000")));
        assertThat(Convert.toWei("21", Convert.Unit.MWEI), is(new BigDecimal("21000000")));
        assertThat(Convert.toWei("21", Convert.Unit.GWEI), is(new BigDecimal("21000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.SZABO), is(new BigDecimal("21000000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.FINNEY),
                is(new BigDecimal("21000000000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.ETHER),
                is(new BigDecimal("21000000000000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.KETHER),
                is(new BigDecimal("21000000000000000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.METHER),
                is(new BigDecimal("21000000000000000000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.GETHER),
                is(new BigDecimal("21000000000000000000000000000")));
    }

    @Test
    public void testUnit() {
        assertThat(Convert.Unit.fromString("ether"), is(Convert.Unit.ETHER));
        assertThat(Convert.Unit.fromString("ETHER"), is(Convert.Unit.ETHER));
        assertThat(Convert.Unit.fromString("wei"), is(Convert.Unit.WEI));
    }
}
