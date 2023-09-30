package telegram.expensetrackerbot.service.handler;

import static telegram.expensetrackerbot.util.Constants.REPORT_DELIMITER;

import java.util.List;
import org.springframework.stereotype.Component;
import telegram.expensetrackerbot.enums.CommandState;
import telegram.expensetrackerbot.model.entity.Expense;

@Component
public class ExpenseReportHandlerImpl implements ReportHandler {
    @Override
    public boolean isApplicable(CommandState commandState) {
        return commandState.equals(CommandState.SHOW_EXPENSES);
    }

    @Override
    public String buildReport(List<Expense> expenses) {
        StringBuilder expenseReport = new StringBuilder(
                "<u>user, date, category, cost, comment</u>");
        for (Expense expense : expenses) {
            expenseReport.append(System.lineSeparator())
                    .append(expense.getUserAccount().getFirstName())
                    .append(REPORT_DELIMITER)
                    .append(expense.getExpenseDate())
                    .append(REPORT_DELIMITER)
                    .append(expense.getExpenseCategory().getName())
                    .append(REPORT_DELIMITER)
                    .append(expense.getCost())
                    .append(REPORT_DELIMITER)
                    .append(expense.getComment());
        }
        return expenseReport.toString();
    }
}
