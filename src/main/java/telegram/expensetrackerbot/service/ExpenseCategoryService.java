package telegram.expensetrackerbot.service;

import java.util.List;
import telegram.expensetrackerbot.model.entity.ExpenseCategory;

public interface ExpenseCategoryService {

    ExpenseCategory findByName(String name);

    List<ExpenseCategory> getAll();

    ExpenseCategory save(ExpenseCategory expenseCategory);
}
