package telegram.expensetrackerbot.handler.impl.entered;

import static telegram.expensetrackerbot.util.Constants.*;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegram.expensetrackerbot.enums.SessionState;
import telegram.expensetrackerbot.handler.UserRequestHandler;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.model.UserSession;
import telegram.expensetrackerbot.model.entity.Expense;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.ExpenseService;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.util.BotLogger;
import telegram.expensetrackerbot.util.KeyboardBuilder;

@Component
public class CommentEnteredHandler implements UserRequestHandler {
    private final BotLogger botLogger;
    private final ExpenseService expenseService;
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final UserSessionService userSessionService;

    public CommentEnteredHandler(BotLogger botLogger,
                                 ExpenseService expenseService,
                                 KeyboardBuilder keyboardBuilder,
                                 MessageSender messageSender,
                                 UserSessionService userSessionService) {
        this.botLogger = botLogger;
        this.expenseService = expenseService;
        this.keyboardBuilder = keyboardBuilder;
        this.messageSender = messageSender;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return (isTextMessage(userRequest.getUpdate()) || isCallbackQuery(userRequest.getUpdate()))
                && SessionState.WAITING_FOR_COMMENT.equals(
                userRequest.getUserSession().getSessionState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        Long chatId = userRequest.getChatId();
        Update update = userRequest.getUpdate();
        String comment;
        if (isCallbackQuery(update)) {
            if (update.getCallbackQuery().getData().equals(BUTTON_NO_COMMENT)) {
                comment = null;
            } else {
                messageSender.sendMessage(chatId, update.getCallbackQuery().getId(),
                        ERROR_INVALID_INPUT);
                botLogger.warn(ERROR_INVALID_INPUT, update.getCallbackQuery().getFrom());
                return;
            }
        } else {
            comment = update.getMessage().getText();
        }
        UserSession userSession = userRequest.getUserSession();
        Expense expense = userSession.getExpense();
        expense.setComment(comment);
        expenseService.save(expense);
        userSession.setSessionState(SessionState.SESSION_STARTED);
        userSessionService.save(chatId, userSession);
        if (isCallbackQuery(update)) {
            messageSender.sendMessage(chatId, update.getCallbackQuery().getId(), MESSAGE_READY);
        } else {
            messageSender.sendMessage(chatId, MESSAGE_READY);
        }
        messageSender.sendMessage(chatId, MESSAGE_SELECT_COMMAND,
                keyboardBuilder.buildAddExpenseButtonMenu());
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
