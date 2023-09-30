package telegram.expensetrackerbot.handler.impl.entered;

import static telegram.expensetrackerbot.util.Constants.*;

import org.springframework.stereotype.Component;
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
public class NewCategoryEnteredHandler implements UserRequestHandler {
    private final ExpenseCategoryService expenseCategoryService;
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final UserSessionService userSessionService;

    public NewCategoryEnteredHandler(KeyboardBuilder keyboardBuilder,
                                     MessageSender messageSender,
                                     UserSessionService userSessionService,
                                     ExpenseCategoryService expenseCategoryService) {
        this.keyboardBuilder = keyboardBuilder;
        this.messageSender = messageSender;
        this.userSessionService = userSessionService;
        this.expenseCategoryService = expenseCategoryService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && SessionState.WAITING_FOR_NEW_CATEGORY.equals(
                userRequest.getUserSession().getSessionState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        Long chatId = userRequest.getChatId();
        ExpenseCategory newCategory = ExpenseCategory.builder()
                .name(userRequest.getUpdate().getMessage().getText())
                .build();
        expenseCategoryService.save(newCategory);
        UserSession userSession = userRequest.getUserSession();
        userSession.setSessionState(SessionState.SESSION_STARTED);
        userSessionService.save(chatId, userSession);
        messageSender.sendMessage(chatId, MESSAGE_READY);
        messageSender.sendMessage(chatId, MESSAGE_SELECT_COMMAND,
                keyboardBuilder.buildAddExpenseButtonMenu());
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
