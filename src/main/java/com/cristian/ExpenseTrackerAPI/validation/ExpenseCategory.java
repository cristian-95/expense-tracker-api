package com.cristian.ExpenseTrackerAPI.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExpenseCategoryValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpenseCategory {

    String message() default "Invalid expense category";


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
