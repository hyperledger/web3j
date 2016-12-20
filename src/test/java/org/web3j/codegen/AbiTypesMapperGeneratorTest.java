package org.web3j.codegen;

import org.junit.Test;


public class AbiTypesMapperGeneratorTest extends GeneratorBase {

    @Test
    public void testGeneration() throws Exception {
        AbiTypesMapperGenerator.main(new String[] { tempDirPath });
    }
}
