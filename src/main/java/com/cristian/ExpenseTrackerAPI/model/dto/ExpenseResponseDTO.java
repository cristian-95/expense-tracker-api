package com.cristian.ExpenseTrackerAPI.model.dto;

import java.time.LocalDate;

public record ExpenseResponseDTO(
        Long id,
        String name,
        String category,
        Double amount,
        LocalDate date,
        String username
) {
}
