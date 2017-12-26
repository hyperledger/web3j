package org.web3j.utils.validator;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AddressValidatorTest {

    private static AddressValidator addressValidator;

    @Before
    public void setUp() {
        addressValidator = new AddressValidator();
        Address address = new Address() {

            @Override
            public String message() {
                return null;
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class[] payload() {
                return new Class[0];
            }

            @Override
            public Class<? extends Address> annotationType() {
                return Address.class;
            }
        };
        addressValidator.initialize(address);

    }

    @Test
    public void testNoValidAddress() {
        Assert.assertFalse(addressValidator.isValid(UUID.randomUUID().toString(), null));
    }

    @Test
    public void testNullAddress() {
        Assert.assertFalse(addressValidator.isValid(null, null));
    }

    @Test
    public void testEmptyAddress() {
        Assert.assertFalse(addressValidator.isValid("", null));
    }

    @Test
    public void testValidAddress() {
        Assert.assertTrue(addressValidator
                .isValid("0x2dfBf35bb7c3c0A466A6C48BEBf3eF7576d3C420", null));
    }

}
