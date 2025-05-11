package com.cristian.ExpenseTrackerAPI.service;

import com.cristian.ExpenseTrackerAPI.exceptions.ExpenseNotFoundException;
import com.cristian.ExpenseTrackerAPI.mapper.ExpenseMapper;
import com.cristian.ExpenseTrackerAPI.model.dto.CreateExpenseDTO;
import com.cristian.ExpenseTrackerAPI.model.dto.ExpenseResponseDTO;
import com.cristian.ExpenseTrackerAPI.model.dto.UpdateExpenseDTO;
import com.cristian.ExpenseTrackerAPI.model.entity.Expense;
import com.cristian.ExpenseTrackerAPI.model.entity.User;
import com.cristian.ExpenseTrackerAPI.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.logging.Logger;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;
    private final UserService userService;
    private final ExpenseMapper expenseMapper;
    private final Logger log = Logger.getLogger("Expense Service");

    public ExpenseService(ExpenseMapper expenseMapper, ExpenseRepository repository, UserService userService) {
        this.expenseMapper = expenseMapper;
        this.repository = repository;
        this.userService = userService;
    }

    public Page<ExpenseResponseDTO> getAllExpensesByUser(Pageable pageable, String username) {
        log.info("Retrieving all expenses of user %s".formatted(username));
        User user = userService.findByUsername(username);
        return this.repository.findByUser(pageable, user).map(expenseMapper::toResponseDTO);
    }

    public Page<ExpenseResponseDTO> getExpensesByUserBetweenDates(Pageable pageable, LocalDate startDate, LocalDate endDate, String username) {
        log.info("Retrieving all expenses of user %s between dates %s and %s"
                .formatted(username, startDate.toString(), endDate.toString()));
        if (startDate.isAfter(endDate)) throw new IllegalArgumentException("startDate must be before endDate");
        User user = userService.findByUsername(username);
        return this.repository.findByUserAndDateBetween(pageable, user, startDate, endDate).map(expenseMapper::toResponseDTO);
    }

    public ExpenseResponseDTO getExpenseById(Long id, String username) {
        log.info("Retrieving an expense with id = %s of user %s".formatted(id, username));
        User user = userService.findByUsername(username);
        Expense expense = repository.findByIdAndUser(id, user).orElseThrow(() -> new ExpenseNotFoundException("Expense with id = " + id + " not found."));
        return expenseMapper.toResponseDTO(expense);
    }

    private Expense getExpenseEntityById(Long id, String username) {
        log.info("Retrieving an expense with id = %s of user %s".formatted(id, username));
        User user = userService.findByUsername(username);
        return repository.findByIdAndUser(id, user).orElseThrow(() -> new ExpenseNotFoundException("Expense with id = " + id + " not found."));

    }

    @Transactional
    public ExpenseResponseDTO createExpense(@Valid CreateExpenseDTO createExpenseDTO, String username) {
        log.info("Creating an expense for user %s".formatted(username));
        Expense expense = expenseMapper.toEntity(createExpenseDTO);
        if (expense.getDate() == null) expense.setDate(LocalDate.now());
        User user = userService.findByUsername(username);
        expense.setUser(user);
        return expenseMapper.toResponseDTO(repository.save(expense));
    }

    @Transactional
    public ExpenseResponseDTO updateExpense(Long id, @Valid UpdateExpenseDTO updateExpenseDTO, String username) {
        log.info("Updating an expense for user %s".formatted(username));
        Expense expense = getExpenseEntityById(id, username);
        expenseMapper.updateFromDTO(updateExpenseDTO, expense);
        return expenseMapper.toResponseDTO(repository.save(expense));
    }

    @Transactional
    public void deleteExpense(Long id, String username) {
        log.info("Deleting expense with id = %s for user %s".formatted(id, username));
        Expense expense = getExpenseEntityById(id, username);
        repository.delete(expense);
    }
}
