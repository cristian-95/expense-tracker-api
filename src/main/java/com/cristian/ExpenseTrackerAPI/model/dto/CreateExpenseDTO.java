package com.cristian.ExpenseTrackerAPI.model.dto;

import com.cristian.ExpenseTrackerAPI.validation.ExpenseCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record CreateExpenseDTO(
        @NotNull(message = "Name cannot be blank.")
        @Length(min = 3, message = "Name must have at least 3 characters")
        String name,
        @Min(value = 1, message = "Amount value must be greater than zero.")
        @NotNull(message = "Amount cannot be null or zero.")
        Double amount,
        @Nullable
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @ExpenseCategory(message = "Please choose an existing category.")
        String category
) {
    public CreateExpenseDTO(@NotNull(message = "Name cannot be blank.")
                            @Length(min = 3, message = "Name name must have at least 3 characters")
                            String name, @Min(value = 1, message = "Amount value must be greater than zero.")
                            @NotNull(message = "Amount cannot be null or zero.")
                            Double amount, @Nullable
                            @JsonFormat(pattern = "yyyy-MM-dd")
                            LocalDate date,
                            String category) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.category = category.toUpperCase();
    }
}
