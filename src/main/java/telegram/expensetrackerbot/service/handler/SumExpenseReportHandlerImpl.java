package telegram.expensetrackerbot.service.handler;

import static telegram.expensetrackerbot.util.Constants.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Component;
import telegram.expensetrackerbot.enums.CommandState;
import telegram.expensetrackerbot.model.entity.Expense;

@Component
public class SumExpenseReportHandlerImpl implements ReportHandler {
    private final StringBuilder expenseReport = new StringBuilder(SUMMARY_HEADER);

    @Override
    public boolean isApplicable(CommandState commandState) {
        return commandState.equals(CommandState.SHOW_SUMMARY_EXPENSES);
    }

    @Override
    public String buildReport(List<Expense> expenses) {
        Map<String, BigDecimal> expenseByUserAndCategory = getExpenseByKey(expenses, USER_CATEGORY);
        appendInfoToReport(expenseByUserAndCategory, null);

        Map<String, BigDecimal> expenseByUser = getExpenseByKey(expenses, USER);
        BigDecimal totalCost = BigDecimal.valueOf(0);
        appendInfoToReport(expenseByUser, totalCost);

        expenseReport.append(TOTAL).append(REPORT_DELIMITER).append(totalCost);
        return expenseReport.toString();
    }

    private void appendInfoToReport(Map<String, BigDecimal> expenseByKey, BigDecimal totalCost) {
        for (Map.Entry<String, BigDecimal> expenseSet : expenseByKey.entrySet()) {
            String userFirstNameAndCategory = expenseSet.getKey().substring(
                    expenseSet.getKey().indexOf(REPORT_DELIMITER.trim()) + 2);
            expenseReport.append(System.lineSeparator())
                    .append(userFirstNameAndCategory)
                    .append(REPORT_DELIMITER)
                    .append(expenseSet.getValue());
            if (totalCost != null) {
                totalCost = totalCost.add(expenseSet.getValue());
            }
        }
        expenseReport.append(System.lineSeparator());
    }

    private static Map<String, BigDecimal> getExpenseByKey(List<Expense> expenses, String key) {
        Map<String, BigDecimal> expenseByKey = new TreeMap<>();
        for (Expense expense : expenses) {
            Long telegramUserId = expense.getUserAccount().getTelegramUserId();
            String userFirstName = expense.getUserAccount().getFirstName();
            String user = telegramUserId + REPORT_DELIMITER + userFirstName;
            key = key.equals(USER) ? user
                    : user + REPORT_DELIMITER + expense.getExpenseCategory().getName();
            BigDecimal cost = expense.getCost();
            if (expenseByKey.containsKey(key)) {
                BigDecimal sumExpense = expenseByKey.get(key).add(cost);
                expenseByKey.put(key, sumExpense);
            } else {
                expenseByKey.put(key, cost);
            }
        }
        return expenseByKey;
    }
}
