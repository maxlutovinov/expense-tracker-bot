package telegram.expensetrackerbot.handler;

import static telegram.expensetrackerbot.util.Constants.MESSAGE_MONEY;

import telegram.expensetrackerbot.enums.CommandState;
import telegram.expensetrackerbot.enums.SessionState;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.model.UserSession;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.util.KeyboardBuilder;

public abstract class AddExpenseHandler implements UserRequestHandler {
    private final KeyboardBuilder keyboardBuilder;
    private final MessageSender messageSender;
    private final UserSessionService userSessionService;

    protected AddExpenseHandler(KeyboardBuilder keyboardBuilder,
                                MessageSender messageSender,
                                UserSessionService userSessionService) {
        this.keyboardBuilder = keyboardBuilder;
        this.messageSender = messageSender;
        this.userSessionService = userSessionService;
    }

    @Override
    public void handle(UserRequest userRequest) {
        UserSession userSession = userRequest.getUserSession();
        userSession.setSessionState(SessionState.WAITING_FOR_COST);
        userSession.setCommandState(CommandState.NO_TRACKED);
        userSession.setStartDateCache(null);
        userSession.setCalendarDateCache(null);
        userSessionService.save(userRequest.getChatId(), userSession);
        messageSender.sendMessage(userRequest.getChatId(), MESSAGE_MONEY,
                keyboardBuilder.buildCancelButtonMenu());
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
