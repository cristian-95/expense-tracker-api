package com.cristian.ExpenseTrackerAPI.exceptions;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException() {
    }

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
