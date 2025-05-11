package com.cristian.ExpenseTrackerAPI.repository;

import com.cristian.ExpenseTrackerAPI.model.entity.Expense;
import com.cristian.ExpenseTrackerAPI.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByIdAndUser(Long id, User user);

    Page<Expense> findByUser(Pageable pageable, User user);

    Page<Expense> findByUserAndDateBetween(Pageable pageable, User user, LocalDate startDate, LocalDate endDate);
}
