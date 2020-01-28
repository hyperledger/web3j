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
package org.web3j.codegen.unit.gen.java;

import java.io.File;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaClassGeneratorTest extends Setup {
    @Test
    public void testThatTheClassWasSuccessfullyWritten() {
        assertTrue(new File(pathToTest).exists());
    }

    @Test
    public void testThatExceptionIsThrownWhenAClassIsNotWritten() {
        assertThrows(
                NullPointerException.class,
                () -> new JavaClassGenerator(null, "org.com", temp.toString()).writeClass());
    }

    @Test
    public void testThatClassWasGeneratedWithCorrectFields() {
        assertTrue(classAsString.contains("private static Greeter greeter;"));
    }
}
