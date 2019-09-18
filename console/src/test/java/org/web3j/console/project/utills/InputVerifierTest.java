/*
 * Copyright 2019 Web3 Labs LTD.
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
package org.web3j.console.project.utills;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputVerifierTest {
    @Test
    public void requiredArgumentsAreEmptyTest() {
        final String[] args = {"", "", ""};
        assertFalse(InputVerifier.requiredArgsAreNotEmpty(args));
    }

    @Test
    public void requiredArgsAreNotEmptyTest() {
        final String[] args = {
            "TestProjectName", "test.package.name",
        };
        assertTrue(InputVerifier.requiredArgsAreNotEmpty(args));
    }

    @Test
    public void classNameIsValidTest() {
        assertTrue(InputVerifier.classNameIsValid("ClassNameTest"));
    }

    @Test
    public void classNameIsNotValidWhenFirstCharacterIsNumberTest() {
        assertFalse(InputVerifier.classNameIsValid("1BadClassName"));
    }

    @Test
    public void ClassNameIsNotValidWhenFirstCharacterIsSymbol() {
        assertFalse(InputVerifier.classNameIsValid("!BadClassName"));
    }

    @Test
    public void packageNameIsValidTest() {
        assertTrue(InputVerifier.packageNameIsValid("org.com"));
    }

    @Test
    public void packageNameIsNotValidTest() {
        assertFalse(InputVerifier.packageNameIsValid("1.com"));
    }
}
