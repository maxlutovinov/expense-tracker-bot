package telegram.expensetrackerbot;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Log4j2
@SpringBootApplication
public class ExpenseTrackerBotApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(
                ExpenseTrackerBotApplication.class, args);

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            log.info("Registering bot");
            telegramBotsApi.registerBot(context.getBean(ExpenseTrackerBot.class));
        } catch (TelegramApiException e) {
            log.error("Failed to register bot", e);
        }
        log.info("Bot is ready to process updates from users");
    }
}
