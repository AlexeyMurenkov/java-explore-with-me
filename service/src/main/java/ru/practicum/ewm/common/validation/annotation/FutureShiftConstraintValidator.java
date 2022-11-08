package ru.practicum.ewm.common.validation.annotation;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FutureShiftConstraintValidator implements ConstraintValidator<FutureShift, LocalDateTime> {
    int shiftHours;

    @Override
    public void initialize(FutureShift constraintAnnotation) {
        shiftHours = constraintAnnotation.shiftHours();
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return LocalDateTime.now().plusHours(shiftHours).isBefore(value);
    }
}
