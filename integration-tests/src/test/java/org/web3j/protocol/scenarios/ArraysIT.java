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
                new BigInteger("10"),
                new BigInteger("9"),
                new BigInteger("8"),
                new BigInteger("7"),
                new BigInteger("6"),
                new BigInteger("5"),
                new BigInteger("4"),
                new BigInteger("3"),
                new BigInteger("2"),
                new BigInteger("1"));

        final List result = contract.fixedReverse(array).send();
        array.sort(Comparator.comparing(BigInteger::intValue));

        assertThat(result, equalTo(array));
    }

    @Test
    public void testDynamicReverse() throws Exception {

        final List<BigInteger> array = java.util.Arrays.asList(
                new BigInteger("3"),
                new BigInteger("2"),
                new BigInteger("1"));

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
                new BigInteger("1"),
                new BigInteger("2"));

        final List<BigInteger> array2 = java.util.Arrays.asList(
                new BigInteger("3"),
                new BigInteger("4"));

        final List result = contract.multiDynamic(java.util.Arrays.asList(array1, array2)).send();

        assertThat(result, equalTo(java.util.Arrays.asList(
                new BigInteger("1"),
                new BigInteger("2"),
                new BigInteger("3"),
                new BigInteger("4"))));
    }

    @Test
    @Ignore("VM Exception while processing transaction: revert")
    public void testMultiFixed() throws Exception {

        final List<BigInteger> array1 = java.util.Arrays.asList(
                new BigInteger("2"),
                new BigInteger("1")
        );

        final List<BigInteger> array2 = java.util.Arrays.asList(
                new BigInteger("4"),
                new BigInteger("3")
        );

        final List<BigInteger> array3 = java.util.Arrays.asList(
                new BigInteger("6"),
                new BigInteger("5")
        );

        final List<BigInteger> array4 = java.util.Arrays.asList(
                new BigInteger("8"),
                new BigInteger("7")
        );

        final List<BigInteger> array5 = java.util.Arrays.asList(
                new BigInteger("10"),
                new BigInteger("9")
        );

        final List<BigInteger> array6 = java.util.Arrays.asList(
                new BigInteger("12"),
                new BigInteger("11")
        );

        List<List<BigInteger>> input = java.util.Arrays.asList(
                array1, array2, array3, array4, array5, array6);

        final List result = contract.multiFixed(input).send();

        assertThat(result, equalTo(java.util.Arrays.asList(
                new BigInteger("1"),
                new BigInteger("2"),
                new BigInteger("3"),
                new BigInteger("4"),
                new BigInteger("5"),
                new BigInteger("6"),
                new BigInteger("7"),
                new BigInteger("8"),
                new BigInteger("9"),
                new BigInteger("10"),
                new BigInteger("11"),
                new BigInteger("12"))));
    }

}