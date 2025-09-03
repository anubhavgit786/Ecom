package com.anubhav.ecom.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // applicable to fields
@Retention(RetentionPolicy.RUNTIME) // available at runtime
@Constraint(validatedBy = LowercaseValidator.class) // link to validator
public @interface Lowercase
{

    String message() default "must be lowercase";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
