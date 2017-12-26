package org.web3j.utils.validator;

import org.web3j.crypto.WalletUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
