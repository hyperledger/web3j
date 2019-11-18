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
package org.web3j.codegen.unit.gen;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitClassGeneratorTest extends Setup {
    @Test
    public void testThatTheClassWasSuccessfullyWritten() {
        assertTrue(classAsFile.exists());
    }

    @Test
    public void testThatExceptionIsThrownWhenAClassIsNotWritten() throws IOException {
        assertThrows(
                NullPointerException.class,
                () -> {
                    new UnitClassGenerator(null, "org.com").writeClass(Optional.of(temp));
                });
    }

    @Test
    public void testThatClassWasGeneratedWithCorrectFields() {
        assertTrue(classAsString.contains("private Greeter greeter;"));
    }
}
