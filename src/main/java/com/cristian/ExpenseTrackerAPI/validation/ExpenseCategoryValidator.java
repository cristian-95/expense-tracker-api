package com.cristian.ExpenseTrackerAPI.validation;

import com.cristian.ExpenseTrackerAPI.model.Category;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExpenseCategoryValidator implements ConstraintValidator<ExpenseCategory, String> {

    @Override
    public void initialize(ExpenseCategory constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Category.fromString(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
