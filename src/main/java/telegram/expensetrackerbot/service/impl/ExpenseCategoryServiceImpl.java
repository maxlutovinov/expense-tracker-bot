package telegram.expensetrackerbot.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import telegram.expensetrackerbot.model.entity.ExpenseCategory;
import telegram.expensetrackerbot.repository.ExpenseCategoryRepository;
import telegram.expensetrackerbot.service.ExpenseCategoryService;

@Service
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {
    private final ExpenseCategoryRepository repository;

    public ExpenseCategoryServiceImpl(ExpenseCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExpenseCategory findByName(String name) {
        return repository.findByName(name).orElseThrow(
                () -> new NoSuchElementException("Category with name " + name + " not found"));
    }

    @Override
    public List<ExpenseCategory> getAll() {
        return repository.findAll();
    }

    @Override
    public ExpenseCategory save(ExpenseCategory expenseCategory) {
        List<String> existingCategoryNames = repository.findAll().stream()
                .map(ExpenseCategory::getName).toList();
        if (!existingCategoryNames.contains(expenseCategory.getName())) {
            repository.save(expenseCategory);
        }
        return expenseCategory;
    }
}
