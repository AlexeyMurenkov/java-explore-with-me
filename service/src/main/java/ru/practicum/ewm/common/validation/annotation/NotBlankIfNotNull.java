package ru.practicum.ewm.common.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = NotBlankIfNotNullConstraintValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface NotBlankIfNotNull {
    String message() default "Invalid not blank field validation";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
