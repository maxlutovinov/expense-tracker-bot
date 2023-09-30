package telegram.expensetrackerbot.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import telegram.expensetrackerbot.model.UserRequest;

public interface UserRequestHandler {

    boolean isApplicable(UserRequest userRequest);

    void handle(UserRequest userRequest);

    boolean isGlobal();

    default boolean isCallbackQuery(Update update) {
        return update.hasCallbackQuery()
                && update.getCallbackQuery().getMessage().hasText();
    }

    default boolean isCommand(Update update, String command) {
        return update.hasMessage()
                && update.getMessage().isCommand()
                && update.getMessage().getText().equals(command);
    }

    default boolean isTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    default boolean isTextMessage(Update update, String text) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().equals(text);
    }
}
