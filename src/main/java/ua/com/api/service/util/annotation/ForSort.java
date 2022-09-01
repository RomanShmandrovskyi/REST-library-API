package ua.com.api.service.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ForSort {

    /**
     * Different 'sortBy' parameter values. Use this anno for field to enable sorting by this field
     */
    String defaultValue();
    String[] aliases();
}
