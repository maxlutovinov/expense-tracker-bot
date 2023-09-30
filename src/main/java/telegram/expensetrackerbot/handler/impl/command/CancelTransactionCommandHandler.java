package telegram.expensetrackerbot.handler.impl.command;

import static telegram.expensetrackerbot.util.Constants.COMMAND_CANCEL;

import org.springframework.stereotype.Component;
import telegram.expensetrackerbot.handler.CancelTransactionHandler;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.util.KeyboardBuilder;

@Component
public class CancelTransactionCommandHandler extends CancelTransactionHandler {
    protected CancelTransactionCommandHandler(KeyboardBuilder keyboardBuilder,
                                              MessageSender messageSender,
                                              UserSessionService userSessionService) {
        super(keyboardBuilder, messageSender, userSessionService);
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isCommand(userRequest.getUpdate(), COMMAND_CANCEL);
    }
}
