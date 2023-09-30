package telegram.expensetrackerbot.handler.impl.command;

import static telegram.expensetrackerbot.util.Constants.*;

import org.springframework.context.annotation.Primary;
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
@Primary
public class StartCommandHandler implements UserRequestHandler {
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final UserSessionService userSessionService;

    public StartCommandHandler(KeyboardBuilder keyboardBuilder,
                               MessageSender messageSender,
                               UserSessionService userSessionService) {
        this.keyboardBuilder = keyboardBuilder;
        this.messageSender = messageSender;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), COMMAND_START);
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        userSession.setSessionState(SessionState.SESSION_STARTED);
        userSession.setCommandState(CommandState.NO_TRACKED);
        userSession.setStartDateCache(null);
        userSession.setCalendarDateCache(null);
        Long chatId = userRequest.getChatId();
        userSessionService.save(chatId, userSession);
        messageSender.sendMessage(chatId, MESSAGE_WELCOME);
        messageSender.sendMessage(chatId, MESSAGE_SELECT_COMMAND,
                keyboardBuilder.buildAddExpenseButtonMenu());
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
