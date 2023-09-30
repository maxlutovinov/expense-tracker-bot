package telegram.expensetrackerbot.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

@Log4j2
@Component
public class BotLogger {
    public void info(String logMessage, User user, String textFromUser) {
        log.info(logMessage + ", user [{}, {}, {}, {}] : {}",
                user.getId(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                textFromUser);
    }

    public void warn(String logMessage) {
        log.warn(logMessage);
    }

    public void warn(String logMessage, User user) {
        log.warn(logMessage + ", user [{}, {}, {}, {}]",
                user.getId(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName());
    }

    public void warn(String logMessage, User user, String textFromUser) {
        log.warn(logMessage + ", user [{}, {}, {}, {}] : {}",
                user.getId(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                textFromUser);
    }

    public void error(String logMessage, Exception e) {
        log.error(logMessage, e);
    }
}
