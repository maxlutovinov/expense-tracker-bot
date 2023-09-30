package telegram.expensetrackerbot.service.impl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import telegram.expensetrackerbot.model.entity.Expense;
import telegram.expensetrackerbot.repository.ExpenseRepository;
import telegram.expensetrackerbot.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository repository;

    public ExpenseServiceImpl(ExpenseRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Expense> findAllByExpenseDateBetween(LocalDate from, LocalDate to) {
        return repository.findAllByExpenseDateBetween(from, to);
    }

    @Override
    public Expense save(Expense expense) {
        return repository.save(expense);
    }
}
