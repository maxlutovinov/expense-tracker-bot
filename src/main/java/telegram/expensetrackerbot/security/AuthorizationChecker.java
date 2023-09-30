package telegram.expensetrackerbot.security;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface AuthorizationChecker {
    boolean authorize(Update update);
}
