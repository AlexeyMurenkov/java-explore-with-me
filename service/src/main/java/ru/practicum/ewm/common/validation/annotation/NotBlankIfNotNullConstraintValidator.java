package ru.practicum.ewm.common.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankIfNotNullConstraintValidator implements ConstraintValidator<NotBlankIfNotNull, CharSequence> {
    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
        return charSequence == null || charSequence.toString().trim().length() > 0;
    }
}
