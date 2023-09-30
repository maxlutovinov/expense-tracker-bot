package telegram.expensetrackerbot.sender;

import static telegram.expensetrackerbot.util.Constants.ERROR_FAILED_SEND;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.expensetrackerbot.util.BotLogger;

@Component
public class MessageSender {
    private final BotLogger botLogger;
    private final ExpenseTrackerBotSender botSender;

    public MessageSender(BotLogger botLogger, ExpenseTrackerBotSender botSender) {
        this.botLogger = botLogger;
        this.botSender = botSender;
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, (ReplyKeyboard) null);
    }

    public void sendMessage(Long chatId, String queryId, String text) {
        sendMessage(chatId, queryId, text, null);
    }

    public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage.builder()
                .text(text)
                .chatId(chatId.toString())
                .parseMode(ParseMode.HTML)
                .replyMarkup(replyKeyboard)
                .build();
        try {
            botSender.execute(sendMessage);
        } catch (Exception e) {
            botLogger.error(ERROR_FAILED_SEND, e);
        }
    }

    public void sendMessage(Long chatId, String queryId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage.builder()
                .text(text)
                .chatId(chatId.toString())
                .parseMode(ParseMode.HTML)
                .replyMarkup(replyKeyboard)
                .build();
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId)
                .build();
        try {
            botSender.execute(close);
            botSender.execute(sendMessage);
        } catch (Exception e) {
            botLogger.error(ERROR_FAILED_SEND, e);
        }
    }

    public void tapButton(Long chatId, String queryId, int messageId, String message,
                          InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText newText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("")
                .build();
        EditMessageReplyMarkup newKeyboard = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
        newText.setText(message);
        newKeyboard.setReplyMarkup(inlineKeyboardMarkup);
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(queryId).build();
        try {
            botSender.execute(close);
            botSender.execute(newText);
            botSender.execute(newKeyboard);
        } catch (TelegramApiException e) {
            botLogger.error(ERROR_FAILED_SEND, e);
        }
    }
}
