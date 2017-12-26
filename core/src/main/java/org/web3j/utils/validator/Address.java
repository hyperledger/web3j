package org.web3j.utils.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation used to validate a address.
 */
@Documented
@Constraint(validatedBy = {AddressValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Address {

    /**
     * Error message.
     *
     * @return The error message
     */
    String message() default "Invalid address. This is not permitted.";

    /**
     * Group attribute.
     *
     * @return the class group
     */
    Class<?>[] groups() default {};

    /**
     * Payload.
     *
     * @return the payload array attributes.
     */
    Class<? extends Payload>[] payload() default {};

}
