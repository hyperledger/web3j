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
package org.web3j.codegen;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.protocol.core.methods.response.AbiDefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.web3j.codegen.TruffleJsonFunctionWrapperGenerator.Contract;
import static org.web3j.codegen.TruffleJsonFunctionWrapperGenerator.loadContractDefinition;

/** Test that we can parse Truffle Contract from JSON file. */
public class ContractJsonParseTest {

    static final String BUILD_CONTRACTS = "build" + File.separator + "contracts";
    private String contractBaseDir;

    static String jsonFileLocation(
            final String baseDir, final String contractName, final String inputFileName) {
        return baseDir
                + File.separator
                + contractName
                + File.separator
                + BUILD_CONTRACTS
                + File.separator
                + inputFileName
                + ".json";
    }

    @SuppressWarnings("SameParameterValue")
    static Contract parseContractJson(
            final String baseDir, final String contractName, final String inputFileName)
            throws Exception {

        final String fileLocation = jsonFileLocation(baseDir, contractName, inputFileName);
        return loadContractDefinition(new File(fileLocation));
    }

    @BeforeEach
    public void setUp() throws Exception {
        final URL url = SolidityFunctionWrapperGeneratorTest.class.getResource("/truffle");
        contractBaseDir = url.getPath();
    }

    @Test
    public void testParseMetaCoin() throws Exception {
        final Contract mc = parseContractJson(contractBaseDir, "MetaCoin", "MetaCoin");

        assertEquals("MetaCoin", mc.getContractName(), "Unexpected contract name");
    }

    @Test
    public void testParseConvertLib() throws Exception {
        final Contract mc = parseContractJson(contractBaseDir, "MetaCoin", "ConvertLib");

        assertEquals("ConvertLib", mc.getContractName(), "Unexpected contract name");
        assertEquals(1, mc.abi.size());
        final AbiDefinition abi = mc.abi.get(0);
        assertEquals("convert", abi.getName(), "Unexpected function name");
        assertTrue(abi.isConstant(), "Expected function to be 'constant");
        assertFalse(abi.isPayable(), "Expected function to not be 'payable'");
        assertEquals("function", abi.getType(), "Expected abi to represent a function");
        assertEquals(
                "pure",
                abi.getStateMutability(),
                "Expected the 'pure' for the state mutability setting");
    }
}
