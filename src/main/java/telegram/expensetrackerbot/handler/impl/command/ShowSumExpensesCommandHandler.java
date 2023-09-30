package telegram.expensetrackerbot.handler.impl.command;

import static telegram.expensetrackerbot.util.Constants.*;

import java.util.List;
import org.springframework.stereotype.Component;
import telegram.expensetrackerbot.enums.CommandState;
import telegram.expensetrackerbot.enums.SessionState;
import telegram.expensetrackerbot.handler.UserRequestHandler;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.model.UserSession;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.util.KeyboardBuilder;

@Component
public class ShowSumExpensesCommandHandler implements UserRequestHandler {
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final UserSessionService userSessionService;

    public ShowSumExpensesCommandHandler(KeyboardBuilder keyboardBuilder,
                                         MessageSender messageSender,
                                         UserSessionService userSessionService) {
        this.keyboardBuilder = keyboardBuilder;
        this.messageSender = messageSender;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), COMMAND_SHOW_SUM_EXPENSES);
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        userSession.setSessionState(SessionState.WAITING_FOR_PERIOD);
        userSession.setCommandState(CommandState.SHOW_SUMMARY_EXPENSES);
        userSession.setStartDateCache(null);
        userSession.setCalendarDateCache(null);
        userSessionService.save(userRequest.getChatId(), userSession);
        messageSender.sendMessage(userRequest.getChatId(), MESSAGE_REPORT,
                keyboardBuilder.buildInlineMenu(
                        List.of(BUTTON_CURRENT_MONTH, BUTTON_PREVIOUS_MONTH, BUTTON_CALENDAR)));
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
