package org.web3j.codegen;

import org.junit.Test;

import org.web3j.TempFileProvider;
import org.web3j.codegen.AbiTypesMapperGenerator;


public class AbiTypesMapperGeneratorTest extends TempFileProvider {

    @Test
    public void testGeneration() throws Exception {
        AbiTypesMapperGenerator.main(new String[] { tempDirPath });
    }
}
