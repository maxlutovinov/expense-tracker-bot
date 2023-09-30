package telegram.expensetrackerbot.service;

import java.time.LocalDate;
import java.util.List;
import telegram.expensetrackerbot.model.entity.Expense;

public interface ExpenseService {
    List<Expense> findAllByExpenseDateBetween(LocalDate from, LocalDate to);

    Expense save(Expense expense);
}
