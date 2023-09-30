package telegram.expensetrackerbot.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import telegram.expensetrackerbot.enums.CommandState;
import telegram.expensetrackerbot.enums.SessionState;
import telegram.expensetrackerbot.model.entity.Expense;

@Data
@Builder
public class UserSession {
    private Long chatId;
    private SessionState sessionState;
    private CommandState commandState;
    private Expense expense;
    private LocalDate startDateCache;
    private LocalDate calendarDateCache;
}
