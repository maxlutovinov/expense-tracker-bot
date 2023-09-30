package telegram.expensetrackerbot.security.impl;

import static telegram.expensetrackerbot.util.Constants.ERROR_UNAUTHORIZED_ACCESS;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegram.expensetrackerbot.handler.UserRequestHandler;
import telegram.expensetrackerbot.security.AuthorizationChecker;
import telegram.expensetrackerbot.util.BotLogger;

@Component
public class AuthorizationCheckerImpl implements AuthorizationChecker {
    private final BotLogger botLogger;
    private final UserRequestHandler userRequestHandler;
    @Value("#{${bot.authorized-users}}")
    private List<String> authorizedUsers;

    public AuthorizationCheckerImpl(BotLogger botLogger,
                                    UserRequestHandler userRequestHandler) {
        this.botLogger = botLogger;
        this.userRequestHandler = userRequestHandler;
    }

    @Override
    public boolean authorize(Update update) {
        if (update.hasMessage() && authorizedUsers.contains(
                String.valueOf(update.getMessage().getFrom().getId()))) {
            return true;
        }
        if (update.hasCallbackQuery() && authorizedUsers.contains(
                String.valueOf(update.getCallbackQuery().getFrom().getId()))) {
            return true;
        }
        try {
            botLogger.warn(ERROR_UNAUTHORIZED_ACCESS, update.getMessage().getFrom());
        } catch (Exception e) {
            botLogger.error(ERROR_UNAUTHORIZED_ACCESS, e);
        }
        return false;
    }
}
