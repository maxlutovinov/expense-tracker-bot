package telegram.expensetrackerbot.handler.impl.command;

import static telegram.expensetrackerbot.util.Constants.*;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import telegram.expensetrackerbot.enums.CommandState;
import telegram.expensetrackerbot.enums.SessionState;
import telegram.expensetrackerbot.handler.UserRequestHandler;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.model.UserSession;
import telegram.expensetrackerbot.model.entity.ExpenseCategory;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.ExpenseCategoryService;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.util.KeyboardBuilder;

@Component
public class ShowCategoriesCommandHandler implements UserRequestHandler {
    private final ExpenseCategoryService expenseCategoryService;
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final UserSessionService userSessionService;

    public ShowCategoriesCommandHandler(ExpenseCategoryService expenseCategoryService,
                                        KeyboardBuilder keyboardBuilder,
                                        MessageSender messageSender,
                                        UserSessionService userSessionService) {
        this.expenseCategoryService = expenseCategoryService;
        this.keyboardBuilder = keyboardBuilder;
        this.messageSender = messageSender;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), COMMAND_SHOW_EXPENSE_CATEGORIES);
    }

    @Override
    public void handle(UserRequest userRequest) {
        Long chatId = userRequest.getChatId();
        String expenseCategories = expenseCategoryService.getAll().stream()
                .map(ExpenseCategory::getName)
                .sorted()
                .collect(Collectors.joining(System.lineSeparator()));
        if (!expenseCategories.isEmpty()) {
            messageSender.sendMessage(chatId, expenseCategories);
            messageSender.sendMessage(chatId, MESSAGE_SELECT_COMMAND,
                    keyboardBuilder.buildAddExpenseButtonMenu());
        } else {
            messageSender.sendMessage(chatId, MESSAGE_NO_RECORDS_FOUND,
                    keyboardBuilder.buildAddExpenseButtonMenu());
        }
        UserSession userSession = userRequest.getUserSession();
        userSession.setSessionState(SessionState.SESSION_STARTED);
        userSession.setCommandState(CommandState.NO_TRACKED);
        userSession.setStartDateCache(null);
        userSession.setCalendarDateCache(null);
        userSessionService.save(chatId, userSession);
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
