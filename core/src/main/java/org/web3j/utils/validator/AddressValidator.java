package org.web3j.utils.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.web3j.crypto.WalletUtils;

/**
 * Validator class, have the logic to apply the validation.
 */
public class AddressValidator implements ConstraintValidator<Address, String> {

    /**
     * Annotation.
     */
    private Address annotation;

    @Override
    public void initialize(Address constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return WalletUtils.isValidAddress(value);
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }
}
