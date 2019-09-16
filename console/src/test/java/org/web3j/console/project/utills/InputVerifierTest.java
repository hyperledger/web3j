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
        final String[] args = {"TestProjectName", "test.package.name",};
        assertTrue(InputVerifier.requiredArgsAreNotEmpty(args));
    }
    @Test
    public void classNameIsValidTest()
    {
        assertTrue(InputVerifier.classNameIsValid("ClassNameTest"));
    }
    @Test
    public void classNameIsNotValidWhenFirstCharacterIsNumberTest()
    {
        assertFalse(InputVerifier.classNameIsValid("1BadClassName"));

    }
    @Test
    public void ClassNameIsNotValidWhenFirstCharacterIsSymbol()
    {
        assertFalse(InputVerifier.classNameIsValid("!BadClassName"));
    }
    @Test
    public void packageNameIsValidTest()
    {
        assertTrue(InputVerifier.packageNameIsValid("org.com"));
    }
    @Test
    public void packageNameIsNotValidTest()
    {
        assertFalse(InputVerifier.packageNameIsValid("1.com"));
    }

}
