package com.cristian.ExpenseTrackerAPI.model.dto;

public record RegisterRequestDTO(
        String username,
        String name,
        String password
) {
}
