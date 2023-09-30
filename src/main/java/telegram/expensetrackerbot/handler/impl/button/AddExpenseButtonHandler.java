package telegram.expensetrackerbot.handler.impl.button;

import static telegram.expensetrackerbot.util.Constants.BUTTON_ADD_EXPENSE;

import org.springframework.stereotype.Component;
import telegram.expensetrackerbot.handler.AddExpenseHandler;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.util.KeyboardBuilder;

@Component
public class AddExpenseButtonHandler extends AddExpenseHandler {
    protected AddExpenseButtonHandler(KeyboardBuilder keyboardBuilder,
                                      MessageSender messageSender,
                                      UserSessionService userSessionService) {
        super(keyboardBuilder, messageSender, userSessionService);
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate(), BUTTON_ADD_EXPENSE);
    }
}
