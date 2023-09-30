package telegram.expensetrackerbot;

import static telegram.expensetrackerbot.util.Constants.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import telegram.expensetrackerbot.handler.UserRequestDispatcher;
import telegram.expensetrackerbot.handler.UserRequestHandler;
import telegram.expensetrackerbot.model.UserRequest;
import telegram.expensetrackerbot.model.UserSession;
import telegram.expensetrackerbot.model.entity.UserAccount;
import telegram.expensetrackerbot.security.AuthorizationChecker;
import telegram.expensetrackerbot.sender.MessageSender;
import telegram.expensetrackerbot.service.UserAccountService;
import telegram.expensetrackerbot.service.UserSessionService;
import telegram.expensetrackerbot.util.BotLogger;

@Component
public class ExpenseTrackerBot extends TelegramLongPollingBot {
    private final AuthorizationChecker authorizationChecker;
    private final BotLogger botLogger;
    private final MessageSender messageSender;
    private final UserAccountService userAccountService;
    private final UserRequestDispatcher userRequestDispatcher;
    private final UserRequestHandler userRequestHandler;
    private final UserSessionService userSessionService;
    @Value("${bot.username}")
    private String botUserName;

    public ExpenseTrackerBot(@Value("${bot.token}") String botToken,
                             AuthorizationChecker authorizationChecker,
                             BotLogger botLogger,
                             MessageSender messageSender,
                             UserAccountService userAccountService,
                             UserRequestDispatcher userRequestDispatcher,
                             UserRequestHandler userRequestHandler,
                             UserSessionService userSessionService) {
        super(botToken);
        this.authorizationChecker = authorizationChecker;
        this.botLogger = botLogger;
        this.messageSender = messageSender;
        this.userAccountService = userAccountService;
        this.userRequestDispatcher = userRequestDispatcher;
        this.userRequestHandler = userRequestHandler;
        this.userSessionService = userSessionService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!authorizationChecker.authorize(update)) {
            return;
        }

        Long chatId;
        User user;
        String textFromUser;
        if (userRequestHandler.isTextMessage(update)) {
            Message message = update.getMessage();
            chatId = message.getChatId();
            user = message.getFrom();
            textFromUser = message.getText();
        } else if (userRequestHandler.isCallbackQuery(update)) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            chatId = callbackQuery.getMessage().getChatId();
            user = callbackQuery.getFrom();
            String beforeInlineButton = callbackQuery.getMessage().getText();
            String selectedInlineButton = callbackQuery.getData();
            textFromUser = beforeInlineButton + " --> " + selectedInlineButton;
        } else {
            botLogger.warn(ERROR_UNEXPECTED_UPDATE, update.getMessage().getFrom());
            messageSender.sendMessage(update.getMessage().getChatId(), ERROR_UNEXPECTED_UPDATE);
            return;
        }
        botLogger.info(NEW_UPDATE, user, textFromUser);

        UserAccount userAccount = UserAccount.builder()
                .telegramUserId(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        userAccountService.save(userAccount);

        UserSession userSession = userSessionService.get(chatId);
        UserRequest userRequest = UserRequest.builder()
                .chatId(chatId)
                .update(update)
                .userAccount(userAccount)
                .userSession(userSession)
                .build();

        boolean dispatched = userRequestDispatcher.dispatch(userRequest);
        if (!dispatched) {
            botLogger.warn(ERROR_UNHANDLED_REQUEST, user, textFromUser);
            if (userRequestHandler.isCallbackQuery(update)) {
                messageSender.sendMessage(chatId, update.getCallbackQuery().getId(),
                        ERROR_UNHANDLED_REQUEST);
            } else {
                messageSender.sendMessage(chatId, ERROR_UNHANDLED_REQUEST);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
}
