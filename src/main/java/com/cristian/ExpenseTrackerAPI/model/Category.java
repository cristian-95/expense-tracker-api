package com.cristian.ExpenseTrackerAPI.model;

public enum Category {
    GROCERIES,
    LEISURE,
    ELECTRONICS,
    UTILITIES,
    CLOTHING,
    HEALTH,
    OTHERS;

    public static Category fromString(String value) {
        try {
            return Category.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Category.OTHERS;
        }
    }
}