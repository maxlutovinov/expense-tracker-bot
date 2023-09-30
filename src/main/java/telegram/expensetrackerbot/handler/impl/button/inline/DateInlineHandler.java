package telegram.expensetrackerbot.handler.impl.button.inline;

import static telegram.expensetrackerbot.util.Constants.*;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram.expensetrackerbot.enums.SessionState;
import telegram.expensetrackerbot.handler.UserRequestHandler;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.model.UserSession;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.util.BotLogger;
import telegram.expensetrackerbot.util.DateValidator;
import telegram.expensetrackerbot.util.KeyboardBuilder;

@Component
public class DateInlineHandler implements UserRequestHandler {
    private final BotLogger botLogger;
    private final DateValidator dateValidator;
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final UserSessionService userSessionService;

    public DateInlineHandler(BotLogger botLogger,
                             DateValidator dateValidator,
                             KeyboardBuilder keyboardBuilder,
                             MessageSender messageSender,
                             UserSessionService userSessionService) {
        this.botLogger = botLogger;
        this.dateValidator = dateValidator;
        this.keyboardBuilder = keyboardBuilder;
        this.messageSender = messageSender;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCallbackQuery(userRequest.getUpdate())
                && SessionState.WAITING_FOR_DATE.equals(
                userRequest.getUserSession().getSessionState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        Long chatId = userRequest.getChatId();
        CallbackQuery callbackQuery = userRequest.getUpdate().getCallbackQuery();
        String callbackData = callbackQuery.getData();
        String queryId = callbackQuery.getId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        UserSession userSession = userRequest.getUserSession();
        LocalDate date;
        // Handle inline buttons
        if (callbackData.equals(BUTTON_TODAY)) {
            date = LocalDate.now();
        } else if (callbackData.equals(BUTTON_YESTERDAY)) {
            date = LocalDate.now().minusDays(1);
        } else if (callbackData.equals(BUTTON_CALENDAR)) {
            messageSender.tapButton(chatId, queryId, messageId, MESSAGE_DATE,
                    keyboardBuilder.buildCalendarMenu(LocalDate.now()));
            return;
        } else if (dateValidator.isDate(callbackData)) {
            date = LocalDate.parse(callbackData);
        } else if (callbackData.equals(BUTTON_LEFT_SHIFT)) {
            if (userSession.getCalendarDateCache() == null) {
                userSession.setCalendarDateCache(
                        LocalDate.now().minusMonths(1));
            } else {
                userSession.setCalendarDateCache(
                        userSession.getCalendarDateCache().minusMonths(1));
            }
            messageSender.tapButton(chatId, queryId, messageId, MESSAGE_DATE,
                    keyboardBuilder.buildCalendarMenu(userSession.getCalendarDateCache()));
            return;
        } else if (callbackData.equals(BUTTON_RIGHT_SHIFT)) {
            if (userSession.getCalendarDateCache() == null) {
                userSession.setCalendarDateCache(
                        LocalDate.now().plusMonths(1));
            } else {
                userSession.setCalendarDateCache(
                        userSession.getCalendarDateCache().plusMonths(1));
            }
            messageSender.tapButton(chatId, queryId, messageId, MESSAGE_DATE,
                    keyboardBuilder.buildCalendarMenu(userSession.getCalendarDateCache()));
            return;
        } else {
            messageSender.sendMessage(chatId, queryId, ERROR_INVALID_INPUT);
            botLogger.warn(ERROR_INVALID_INPUT, callbackQuery.getFrom());
            return;
        }

        userSession.setSessionState(SessionState.WAITING_FOR_COMMENT);
        userSession.setCalendarDateCache(null);
        userSession.getExpense().setExpenseDate(date);
        userSessionService.save(chatId, userSession);
        messageSender.sendMessage(chatId, queryId, callbackData + MESSAGE_SELECTED);
        messageSender.sendMessage(chatId, MESSAGE_COMMENT,
                keyboardBuilder.buildInlineMenu(List.of(BUTTON_NO_COMMENT)));
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
