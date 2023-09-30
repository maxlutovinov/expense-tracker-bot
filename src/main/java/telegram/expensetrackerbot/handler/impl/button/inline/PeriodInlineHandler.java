package telegram.expensetrackerbot.handler.impl.button.inline;

import static telegram.expensetrackerbot.util.Constants.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import telegram.expensetrackerbot.enums.CommandState;
import telegram.expensetrackerbot.enums.SessionState;
import telegram.expensetrackerbot.handler.UserRequestHandler;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.model.UserSession;
import telegram.expensetrackerbot.model.entity.Expense;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.ExpenseService;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.service.handler.ReportHandler;
import telegram.expensetrackerbot.service.handler.ReportHandlerStrategy;
import telegram.expensetrackerbot.util.BotLogger;
import telegram.expensetrackerbot.util.DateValidator;
import telegram.expensetrackerbot.util.KeyboardBuilder;

@Component
public class PeriodInlineHandler implements UserRequestHandler {
    private final BotLogger botLogger;
    private final DateValidator dateValidator;
    private final ExpenseService expenseService;
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final ReportHandlerStrategy reportHandlerStrategy;
    private final UserSessionService userSessionService;

    public PeriodInlineHandler(BotLogger botLogger,
                               DateValidator dateValidator,
                               ExpenseService expenseService,
                               KeyboardBuilder keyboardBuilder,
                               MessageSender messageSender,
                               ReportHandlerStrategy reportHandlerStrategy,
                               UserSessionService userSessionService) {
        this.botLogger = botLogger;
        this.dateValidator = dateValidator;
        this.expenseService = expenseService;
        this.keyboardBuilder = keyboardBuilder;
        this.messageSender = messageSender;
        this.reportHandlerStrategy = reportHandlerStrategy;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCallbackQuery(userRequest.getUpdate())
                && SessionState.WAITING_FOR_PERIOD.equals(
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
        LocalDate startDate;
        LocalDate endDate;
        // Handle inline buttons
        if (callbackData.equals(BUTTON_CURRENT_MONTH)) {
            startDate = LocalDate.now().withDayOfMonth(1);
            endDate = LocalDate.now();
        } else if (callbackData.equals(BUTTON_PREVIOUS_MONTH)) {
            LocalDate dateInPrevMonth = LocalDate.now().minusMonths(1);
            startDate = dateInPrevMonth.withDayOfMonth(1);
            int monthLength = getMonthLength(dateInPrevMonth);
            endDate = dateInPrevMonth.withDayOfMonth(monthLength);
        } else if (callbackData.equals(BUTTON_CALENDAR)) {
            messageSender.tapButton(chatId, queryId, messageId, MESSAGE_START_DATE,
                    keyboardBuilder.buildCalendarMenu(LocalDate.now()));
            return;
        } else if (callbackData.equals(BUTTON_LEFT_SHIFT)) {
            if (userSession.getCalendarDateCache() == null) {
                userSession.setCalendarDateCache(
                        LocalDate.now().minusMonths(1));
            } else {
                userSession.setCalendarDateCache(
                        userSession.getCalendarDateCache().minusMonths(1));
            }
            String message = userSession.getStartDateCache() != null
                    ? MESSAGE_END_DATE : MESSAGE_START_DATE;
            messageSender.tapButton(chatId, queryId, messageId, message,
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
            String message = userSession.getStartDateCache() != null
                    ? MESSAGE_END_DATE : MESSAGE_START_DATE;
            messageSender.tapButton(chatId, queryId, messageId, message,
                    keyboardBuilder.buildCalendarMenu(userSession.getCalendarDateCache()));
            return;
        } else if (dateValidator.isDate(callbackData)) {
            if (userSession.getStartDateCache() == null) {
                startDate = LocalDate.parse(callbackData);
                userSession.setStartDateCache(startDate);
                userSessionService.save(chatId, userSession);
                messageSender.tapButton(chatId, queryId, messageId, MESSAGE_END_DATE,
                        keyboardBuilder.buildCalendarMenu(startDate));
                return;
            } else {
                startDate = userSession.getStartDateCache();
                endDate = LocalDate.parse(callbackData);
            }
        } else if (dateValidator.isOrdinalDate(callbackData)) {
            LocalDate dateInCalendarMonth = LocalDate.parse(callbackData,
                    DateTimeFormatter.ISO_ORDINAL_DATE);
            startDate = dateInCalendarMonth.withDayOfMonth(1);
            int monthLength = getMonthLength(dateInCalendarMonth);
            endDate = dateInCalendarMonth.withDayOfMonth(monthLength);

        } else {
            messageSender.sendMessage(chatId, queryId, ERROR_INVALID_INPUT);
            botLogger.warn(ERROR_INVALID_INPUT, callbackQuery.getFrom());
            return;
        }
        messageSender.sendMessage(chatId, queryId,
                startDate + " - " + endDate + MESSAGE_SELECTED,
                keyboardBuilder.buildAddExpenseButtonMenu());
        // Build report
        List<Expense> expenses = expenseService.findAllByExpenseDateBetween(startDate, endDate);
        if (!expenses.isEmpty()) {
            ReportHandler reportHandler = reportHandlerStrategy.getHandler(
                    userSession.getCommandState());
            String expenseReport = reportHandler.buildReport(expenses);
            messageSender.sendMessage(chatId, expenseReport,
                    keyboardBuilder.buildAddExpenseButtonMenu());
        } else {
            messageSender.sendMessage(chatId, MESSAGE_NO_RECORDS_FOUND,
                    keyboardBuilder.buildAddExpenseButtonMenu());
        }

        userSession.setSessionState(SessionState.SESSION_STARTED);
        userSession.setCommandState(CommandState.NO_TRACKED);
        userSession.setStartDateCache(null);
        userSession.setCalendarDateCache(null);
        userSessionService.save(chatId, userSession);
    }

    private static int getMonthLength(LocalDate dateInSelectedMonth) {
        return dateInSelectedMonth.getMonth().length(dateInSelectedMonth.isLeapYear());
    }

    @Override
    public boolean isGlobal() {
        return false;
    }
}
