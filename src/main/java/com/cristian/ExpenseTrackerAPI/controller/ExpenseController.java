package com.cristian.ExpenseTrackerAPI.controller;

import com.cristian.ExpenseTrackerAPI.model.dto.CreateExpenseDTO;
import com.cristian.ExpenseTrackerAPI.model.dto.ExpenseResponseDTO;
import com.cristian.ExpenseTrackerAPI.model.dto.UpdateExpenseDTO;
import com.cristian.ExpenseTrackerAPI.security.TokenService;
import com.cristian.ExpenseTrackerAPI.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final TokenService tokenService;

    public ExpenseController(ExpenseService expenseService, TokenService tokenService) {
        this.expenseService = expenseService;
        this.tokenService = tokenService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = {"Expense"}, description = "Retrieves a list of all expenses stored in the database")
    public ResponseEntity<Page<ExpenseResponseDTO>> findAll(
            @RequestHeader(name = "Authorization") String token,
            @PageableDefault(size = 5, sort = "date", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        String username = tokenService.getUsernameFromToken(token);
        Page<ExpenseResponseDTO> page = (startDate != null && endDate != null)
                ? expenseService.getExpensesByUserBetweenDates(pageable, startDate, endDate, username)
                : expenseService.getAllExpensesByUser(pageable, username);
        return ResponseEntity.ok(page);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = {"Expense"}, description = "Retrieves an expense with a certain id, stored in the database")
    public ResponseEntity<ExpenseResponseDTO> findById(@RequestHeader(name = "Authorization") String token, @PathVariable("id") Long id) {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(expenseService.getExpenseById(id, username));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = {"Expense"}, description = "Register an expense  and store it in the database")
    public ResponseEntity<ExpenseResponseDTO> create(@RequestHeader(name = "Authorization") String token, @RequestBody @Valid CreateExpenseDTO createExpenseDTO) {
        String username = tokenService.getUsernameFromToken(token);
        return new ResponseEntity<>(expenseService.createExpense(createExpenseDTO, username), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = {"Expense"}, description = "modify an existing expense and store it in the database")
    public ResponseEntity<ExpenseResponseDTO> update(@RequestHeader(name = "Authorization") String token, @RequestBody @Valid UpdateExpenseDTO updateExpenseDTO, @PathVariable Long id) {
        String username = tokenService.getUsernameFromToken(token);
        return new ResponseEntity<>(expenseService.updateExpense(id,updateExpenseDTO, username), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = {"Expense"}, description = "Remove an existing expense from the database")
    public ResponseEntity<Void> delete(@RequestHeader(name = "Authorization") String token, @PathVariable("id") Long id) {
        String username = tokenService.getUsernameFromToken(token);
        expenseService.deleteExpense(id, username);
        return ResponseEntity.noContent().build();
    }
}
