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

import org.junit.Test;

import org.web3j.TempFileProvider;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AbiTypesGeneratorTest extends TempFileProvider {

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
        AbiTypesGenerator.main(new String[] {tempDirPath});
    }
}
