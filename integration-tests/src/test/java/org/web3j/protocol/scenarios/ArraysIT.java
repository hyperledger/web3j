package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.web3j.generated.Arrays;
import org.web3j.tx.gas.DefaultGasProvider;

import static java.math.BigInteger.valueOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Simple integration test to demonstrate arrays usage in web3j.
 */
public class ArraysIT extends Scenario {

    private Arrays contract;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.contract = Arrays.deploy(web3j, ALICE, new DefaultGasProvider()).send();
    }

    @Test
    public void testFixedReverse() throws Exception {

        final List<BigInteger> array = java.util.Arrays.asList(
                valueOf(10), valueOf(9), valueOf(8), valueOf(7),
                valueOf(6), valueOf(5), valueOf(4),
                valueOf(3), valueOf(2), valueOf(1));

        final List result = contract.fixedReverse(array).send();
        array.sort(Comparator.comparing(BigInteger::intValue));

        assertThat(result, equalTo(array));
    }

    @Test
    public void testDynamicReverse() throws Exception {

        final List<BigInteger> array = java.util.Arrays.asList(
                valueOf(3), valueOf(2), valueOf(1));

        final List result = contract.dynamicReverse(array).send();
        array.sort(Comparator.comparing(BigInteger::intValue));

        assertThat(result, equalTo(array));
    }

    @Test
    public void testEmptyDynamicReverse() throws Exception {
        final List result = contract.dynamicReverse(new ArrayList<>()).send();
        assertThat(result, equalTo(Collections.emptyList()));
    }

    @Test
    @Ignore("VM Exception while processing transaction: revert")
    public void testMultiDynamic() throws Exception {

        final List<BigInteger> array1 = java.util.Arrays.asList(
                valueOf(1), valueOf(2));

        final List<BigInteger> array2 = java.util.Arrays.asList(
                valueOf(3), valueOf(4));

        final List result = contract.multiDynamic(java.util.Arrays.asList(array1, array2)).send();

        assertThat(result, equalTo(java.util.Arrays.asList(
                valueOf(1), valueOf(2), valueOf(3), valueOf(4))));
    }

    @Test
    @Ignore("VM Exception while processing transaction: revert")
    public void testMultiFixed() throws Exception {

        final List<BigInteger> array1 = java.util.Arrays.asList(
                valueOf(1), valueOf(2));

        final List<BigInteger> array2 = java.util.Arrays.asList(
                valueOf(3), valueOf(4));

        final List<BigInteger> array3 = java.util.Arrays.asList(
                valueOf(5), valueOf(6));

        final List<BigInteger> array4 = java.util.Arrays.asList(
                valueOf(7), valueOf(8));

        final List<BigInteger> array5 = java.util.Arrays.asList(
                valueOf(9), valueOf(10));

        final List<BigInteger> array6 = java.util.Arrays.asList(
                valueOf(11), valueOf(12));

        List<List<BigInteger>> input = java.util.Arrays.asList(
                array1, array2, array3, array4, array5, array6);

        final List result = contract.multiFixed(input).send();

        assertThat(result, equalTo(java.util.Arrays.asList(
                valueOf(1), valueOf(2), valueOf(3), valueOf(4),
                valueOf(5), valueOf(6), valueOf(7), valueOf(8),
                valueOf(9), valueOf(10), valueOf(11), valueOf(12))));
    }

}