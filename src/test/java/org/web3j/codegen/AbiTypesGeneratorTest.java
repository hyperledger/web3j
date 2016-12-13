package org.web3j.codegen;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class AbiTypesGeneratorTest extends GeneratorBase {

    @Test
    public void testGetPackageName() {
        assertThat(AbiTypesGenerator.getPackageName(String.class), is("java.lang"));
    }

    @Test
    public void testCreatePackageName() {
        assertThat(AbiTypesGenerator.createPackageName(String.class), is("java.lang.generated"));
    }

    @Test
    public void testGeneration() throws Exception {
        AbiTypesGenerator.main(new String[] { tempDirPath });
    }
}
