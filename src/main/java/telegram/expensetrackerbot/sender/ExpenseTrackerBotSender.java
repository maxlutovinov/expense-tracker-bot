package telegram.expensetrackerbot.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Component
public class ExpenseTrackerBotSender extends DefaultAbsSender {
    protected ExpenseTrackerBotSender(@Value("${bot.token}") String botToken) {
        super(new DefaultBotOptions(), botToken);
    }
}
