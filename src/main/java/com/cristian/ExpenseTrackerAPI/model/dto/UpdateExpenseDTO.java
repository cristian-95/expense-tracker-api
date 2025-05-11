package com.cristian.ExpenseTrackerAPI.model.dto;

import com.cristian.ExpenseTrackerAPI.validation.ExpenseCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record UpdateExpenseDTO(
        @Nullable
        @Length(min = 3, max = 255)
        String name,
        @Nullable
        @Positive
        Double amount,
        @Nullable
        @JsonFormat(pattern = "yyyy-MM-dd")
        @PastOrPresent
        LocalDate date,
        @Nullable
        @ExpenseCategory(message = "Please choose an existing category.")
        String category
) {
}
