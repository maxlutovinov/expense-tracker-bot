package telegram.expensetrackerbot.service.handler;

import java.util.List;
import telegram.expensetrackerbot.enums.CommandState;
import telegram.expensetrackerbot.model.entity.Expense;

public interface ReportHandler {

    boolean isApplicable(CommandState commandState);

    String buildReport(List<Expense> expenses);

}
