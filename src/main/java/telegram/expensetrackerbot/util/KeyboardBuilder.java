package telegram.expensetrackerbot.util;

import static telegram.expensetrackerbot.util.Constants.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class KeyboardBuilder {

    public ReplyKeyboardMarkup buildAddExpenseButtonMenu() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(BUTTON_ADD_EXPENSE);
        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

    public ReplyKeyboardMarkup buildCancelButtonMenu() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(BUTTON_CANCEL);
        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow))
                .resizeKeyboard(true)
                .build();
    }

    public InlineKeyboardMarkup buildInlineMenu(List<String> buttonNames) {
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        for (String buttonName : buttonNames) {
            InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder()
                    .text(buttonName)
                    .callbackData(buttonName)
                    .build();
            keyboardRows.add(List.of(keyboardButton));
        }
        return InlineKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .build();
    }

    public InlineKeyboardMarkup buildCalendarMenu(LocalDate date) {
        if (date == null) {
            return null;
        }
        List<InlineKeyboardButton> headerRow = new ArrayList<>();
        headerRow.add(createButton(BUTTON_LEFT_SHIFT, BUTTON_LEFT_SHIFT));
        headerRow.add(createButton(
                date.format(DateTimeFormatter.ISO_ORDINAL_DATE),
                date.format(DateTimeFormatter.ofPattern("MMM yyyy"))));
        headerRow.add(createButton(BUTTON_RIGHT_SHIFT, BUTTON_RIGHT_SHIFT));

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(headerRow);

        List<InlineKeyboardButton> daysOfWeekRow = new ArrayList<>();
        for (String dayOfWeek : BUTTONS_WEEK_DAYS) {
            daysOfWeekRow.add(createButton(BUTTON_IGNORED, dayOfWeek));
        }
        keyboard.add(daysOfWeekRow);

        LocalDate firstDay = date.withDayOfMonth(1);
        int shift = firstDay.getDayOfWeek().getValue() - 1;
        int daysInMonth = firstDay.getMonth().length(firstDay.isLeapYear());
        int rows = ((daysInMonth + shift) % 7 > 0 ? 1 : 0) + (daysInMonth + shift) / 7;
        for (int i = 0; i < rows; i++) {
            keyboard.add(createRow(firstDay, shift));
            firstDay = firstDay.plusDays(7 - shift);
            shift = 0;
        }
        return InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
    }

    private InlineKeyboardButton createButton(String callBack, String text) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callBack)
                .build();
    }

    private List<InlineKeyboardButton> createRow(LocalDate date, int shift) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        int dayOfMonth = date.getDayOfMonth();
        LocalDate callbackDate = date;
        for (int j = 0; j < shift; j++) {
            row.add(createButton(BUTTON_IGNORED, " "));
        }
        for (int j = shift; j < 7; j++) {
            if (dayOfMonth <= (date.getMonth().length(date.isLeapYear()))) {
                row.add(createButton(callbackDate.toString(), Integer.toString(dayOfMonth++)));
                callbackDate = callbackDate.plusDays(1);
            } else {
                row.add(createButton(BUTTON_IGNORED, " "));
            }
        }
        return row;
    }
}
