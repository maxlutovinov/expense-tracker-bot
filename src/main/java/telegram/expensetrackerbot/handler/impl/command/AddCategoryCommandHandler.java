package telegram.expensetrackerbot.handler.impl.command;

import static telegram.expensetrackerbot.util.Constants.*;

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
public class AddCategoryCommandHandler implements UserRequestHandler {
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final UserSessionService userSessionService;

    public AddCategoryCommandHandler(KeyboardBuilder keyboardBuilder,
                                     MessageSender messageSender,
                                     UserSessionService userSessionService) {
        this.keyboardBuilder = keyboardBuilder;
        this.messageSender = messageSender;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), COMMAND_ADD_EXPENSE_CATEGORY);
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        userSession.setSessionState(SessionState.WAITING_FOR_NEW_CATEGORY);
        userSession.setCommandState(CommandState.NO_TRACKED);
        userSession.setStartDateCache(null);
        userSession.setCalendarDateCache(null);
        userSessionService.save(userRequest.getChatId(), userSession);
        messageSender.sendMessage(userRequest.getChatId(), MESSAGE_NEW_EXPENSE_CATEGORY,
                keyboardBuilder.buildCancelButtonMenu());
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
