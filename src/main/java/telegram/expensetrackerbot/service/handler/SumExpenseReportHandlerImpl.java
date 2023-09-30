package telegram.expensetrackerbot.service.handler;

import static telegram.expensetrackerbot.util.Constants.REPORT_DELIMITER;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Component;
import telegram.expensetrackerbot.enums.CommandState;
import telegram.expensetrackerbot.model.entity.Expense;

@Component
public class SumExpenseReportHandlerImpl implements ReportHandler {
    @Override
    public boolean isApplicable(CommandState commandState) {
        return commandState.equals(CommandState.SHOW_SUMMARY_EXPENSES);
    }

    @Override
    public String buildReport(List<Expense> expenses) {
        StringBuilder expenseReport = new StringBuilder("<u>user, category, cost</u>");
        Map<String, BigDecimal> expenseByUserAndCategory = getExpenseByUserAndCategory(expenses);
        for (Map.Entry<String, BigDecimal> expenseSet : expenseByUserAndCategory.entrySet()) {
            String userFirstNameAndCategory = expenseSet.getKey().substring(
                    expenseSet.getKey().indexOf(",") + 2);
            expenseReport.append(System.lineSeparator())
                    .append(userFirstNameAndCategory)
                    .append(REPORT_DELIMITER)
                    .append(expenseSet.getValue());
        }
        Map<String, BigDecimal> expenseByUser = getExpenseByUser(expenses);
        for (Map.Entry<String, BigDecimal> expenseSet : expenseByUser.entrySet()) {
            String userFirstName = expenseSet.getKey().substring(
                    expenseSet.getKey().indexOf(",") + 2);
            expenseReport.append(System.lineSeparator())
                    .append(userFirstName)
                    .append(REPORT_DELIMITER)
                    .append(expenseSet.getValue());
        }
        return expenseReport.toString();
    }

    private static Map<String, BigDecimal> getExpenseByUser(List<Expense> expenses) {
        Map<String, BigDecimal> expenseByUser = new TreeMap<>();
        for (Expense expense : expenses) {
            Long telegramUserId = expense.getUserAccount().getTelegramUserId();
            String userFirstName = expense.getUserAccount().getFirstName();
            String user = telegramUserId + ", " + userFirstName;
            BigDecimal cost = expense.getCost();
            if (expenseByUser.containsKey(user)) {
                BigDecimal sumExpense = expenseByUser.get(user).add(cost);
                expenseByUser.put(user, sumExpense);
            } else {
                expenseByUser.put(user, cost);
            }
        }
        return expenseByUser;
    }

    private static Map<String, BigDecimal> getExpenseByUserAndCategory(List<Expense> expenses) {
        Map<String, BigDecimal> expenseByUserAndCategory = new TreeMap<>();
        for (Expense expense : expenses) {
            Long telegramUserId = expense.getUserAccount().getTelegramUserId();
            String userFirstName = expense.getUserAccount().getFirstName();
            String category = expense.getExpenseCategory().getName();
            String userAndCategory = telegramUserId + ", " + userFirstName + ", " + category;
            BigDecimal cost = expense.getCost();
            if (expenseByUserAndCategory.containsKey(userAndCategory)) {
                BigDecimal sumExpense = expenseByUserAndCategory.get(userAndCategory).add(cost);
                expenseByUserAndCategory.put(userAndCategory, sumExpense);
            } else {
                expenseByUserAndCategory.put(userAndCategory, cost);
            }
        }
        return expenseByUserAndCategory;
    }
}
