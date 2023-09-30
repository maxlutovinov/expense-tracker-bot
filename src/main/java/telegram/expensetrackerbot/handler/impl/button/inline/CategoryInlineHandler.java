package telegram.expensetrackerbot.handler.impl.button.inline;

import static telegram.expensetrackerbot.util.Constants.*;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram.expensetrackerbot.enums.SessionState;
import telegram.expensetrackerbot.handler.UserRequestHandler;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.model.UserSession;
import telegram.expensetrackerbot.model.entity.ExpenseCategory;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.ExpenseCategoryService;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.util.BotLogger;
import telegram.expensetrackerbot.util.KeyboardBuilder;

@Component
public class CategoryInlineHandler implements UserRequestHandler {
    private final BotLogger botLogger;
    private final ExpenseCategoryService expenseCategoryService;
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final UserSessionService userSessionService;

    public CategoryInlineHandler(BotLogger botLogger,
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
        return isCallbackQuery(userRequest.getUpdate())
                && SessionState.WAITING_FOR_CATEGORY.equals(
                userRequest.getUserSession().getSessionState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        Long chatId = userRequest.getChatId();
        CallbackQuery callbackQuery = userRequest.getUpdate().getCallbackQuery();
        String callbackData = callbackQuery.getData();
        try {
            ExpenseCategory expenseCategory = expenseCategoryService.findByName(callbackData);
            UserSession userSession = userRequest.getUserSession();
            userSession.getExpense().setExpenseCategory(expenseCategory);
            userSession.setSessionState(SessionState.WAITING_FOR_DATE);
            userSessionService.save(chatId, userSession);
            messageSender.sendMessage(chatId, callbackQuery.getId(),
                    callbackData + MESSAGE_SELECTED);
            messageSender.sendMessage(chatId, MESSAGE_DATE, keyboardBuilder.buildInlineMenu(
                    List.of(BUTTON_TODAY, BUTTON_YESTERDAY, BUTTON_CALENDAR)));
        } catch (NoSuchElementException e) {
            messageSender.sendMessage(chatId, callbackQuery.getId(), ERROR_INVALID_INPUT);
            botLogger.warn(ERROR_INVALID_INPUT, callbackQuery.getFrom());
        }
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
