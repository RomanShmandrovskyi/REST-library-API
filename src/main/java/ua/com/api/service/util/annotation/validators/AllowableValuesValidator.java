package ua.com.api.service.util.annotation.validators;

import ua.com.api.service.util.annotation.AllowableValues;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class AllowableValuesValidator implements ConstraintValidator<AllowableValues, String> {
    private List<String> valuesToCheck;
    private String errorMessage;

    @Override
    public void initialize(AllowableValues constraintAnnotation) {
        valuesToCheck = Arrays.asList(constraintAnnotation.values());
        errorMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (valuesToCheck.stream().noneMatch(s -> s.equalsIgnoreCase(value))) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
