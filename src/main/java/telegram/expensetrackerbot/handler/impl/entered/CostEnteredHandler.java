package telegram.expensetrackerbot.handler.impl.entered;

import static telegram.expensetrackerbot.util.Constants.*;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import telegram.expensetrackerbot.enums.SessionState;
import telegram.expensetrackerbot.handler.UserRequestHandler;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.model.UserSession;
import telegram.expensetrackerbot.model.entity.Expense;
import telegram.expensetrackerbot.model.entity.ExpenseCategory;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.ExpenseCategoryService;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.util.BotLogger;
import telegram.expensetrackerbot.util.KeyboardBuilder;

@Component
public class CostEnteredHandler implements UserRequestHandler {
    private final BotLogger botLogger;
    private final ExpenseCategoryService expenseCategoryService;
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final UserSessionService userSessionService;

    public CostEnteredHandler(BotLogger botLogger,
                              ExpenseCategoryService expenseCategoryService,
                              KeyboardBuilder keyboardBuilder,
                              MessageSender messageSender,
                              UserSessionService userSessionService) {
        this.botLogger = botLogger;
        this.expenseCategoryService = expenseCategoryService;
        this.keyboardBuilder = keyboardBuilder;
        this.messageSender = messageSender;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && SessionState.WAITING_FOR_COST.equals(
                userRequest.getUserSession().getSessionState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        Long chatId = userRequest.getChatId();
        Message message = userRequest.getUpdate().getMessage();
        BigDecimal cost;
        try {
            cost = BigDecimal.valueOf(Math.abs(
                    Double.parseDouble(message.getText())));
        } catch (NumberFormatException e) {
            messageSender.sendMessage(chatId, ERROR_INVALID_INPUT);
            botLogger.warn(ERROR_INVALID_INPUT, message.getFrom(), message.getText());
            return;
        }
        Expense expense = Expense.builder()
                .cost(cost)
                .userAccount(userRequest.getUserAccount())
                .build();
        UserSession userSession = userRequest.getUserSession();
        userSession.setExpense(expense);
        userSession.setSessionState(SessionState.WAITING_FOR_CATEGORY);
        userSessionService.save(chatId, userSession);

        List<String> categoryNames = expenseCategoryService.getAll().stream()
                .map(ExpenseCategory::getName)
                .sorted()
                .toList();
        messageSender.sendMessage(chatId, MESSAGE_SELECT_EXPENSE_CATEGORY,
                keyboardBuilder.buildInlineMenu(categoryNames));
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
