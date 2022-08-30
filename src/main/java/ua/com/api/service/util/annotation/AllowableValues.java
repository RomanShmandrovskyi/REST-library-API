package ua.com.api.service.util.annotation;

import ua.com.api.service.util.annotation.validators.AllowableValuesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowableValuesValidator.class)
public @interface AllowableValues {
    String[] values();
    String message() default "Provided value is not allowed";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
