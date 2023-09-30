package telegram.expensetrackerbot.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Component;

@Component
public class DateValidator {
    public boolean isDate(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean isOrdinalDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_ORDINAL_DATE);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
