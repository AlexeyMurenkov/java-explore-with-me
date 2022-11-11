package ru.practicum.ewm.common.validation.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(FutureShift.List.class)
@Constraint(validatedBy = FutureShiftConstraintValidator.class)
public @interface FutureShift {
    int shiftHours() default 0;
    String message() default "Invalid future shift validation";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    @Target(FIELD)
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FutureShift[] value();
    }

}
