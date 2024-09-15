package telegram.expensetrackerbot.util;

public class Constants {
    public static final String BUTTON_ADD_EXPENSE = "💸 Add expense";
    public static final String BUTTON_CALENDAR = "Custom";
    public static final String BUTTON_CANCEL = "❌ Cancel transaction";
    public static final String BUTTON_CURRENT_MONTH = "Current month";
    public static final String BUTTON_PREVIOUS_MONTH = "Previous month";
    public static final String BUTTON_IGNORED = "ignore!@#$%^&";
    public static final String BUTTON_NO_COMMENT = "No Comment";
    public static final String BUTTON_LEFT_SHIFT = "<";
    public static final String BUTTON_RIGHT_SHIFT = ">";
    public static final String BUTTON_TODAY = "Today";
    public static final String BUTTON_YESTERDAY = "Yesterday";
    public static final String[] BUTTONS_WEEK_DAYS = {"M", "T", "W", "T", "F", "S", "S"};
    public static final String COMMAND_ADD_EXPENSE = "/add_expense";
    public static final String COMMAND_ADD_EXPENSE_CATEGORY = "/add_expense_category";
    public static final String COMMAND_CANCEL = "/cancel_transaction";
    public static final String COMMAND_SHOW_EXPENSES = "/show_expenses";
    public static final String COMMAND_SHOW_EXPENSE_CATEGORIES = "/show_expense_categories";
    public static final String COMMAND_SHOW_SUM_EXPENSES = "/show_summary_expenses";
    public static final String COMMAND_START = "/start";
    public static final String ERROR_INVALID_INPUT = "Invalid input";
    public static final String ERROR_FAILED_SEND = "Failed to send data: ";
    public static final String ERROR_UNAUTHORIZED_ACCESS = "Unauthorized access denied";
    public static final String ERROR_UNEXPECTED_UPDATE = "Unexpected update";
    public static final String ERROR_UNHANDLED_REQUEST = "Unhandled request";
    public static final String MESSAGE_COMMENT = "Enter a comment or select predefined "
            + "buttons ✍️ ⤵";
    public static final String MESSAGE_DATE = "Select transaction date 📅 ⤵";
    public static final String MESSAGE_START_DATE = "Select start date 📅 ⤵";
    public static final String MESSAGE_END_DATE = "Select end date 📅 ⤵";
    public static final String MESSAGE_MONEY = "Enter the amount of money in the format: 99.99 "
            + "or tap the Cancel button to abort the current transaction 🪙 ⤵";
    public static final String MESSAGE_NEW_EXPENSE_CATEGORY = "Enter a new expense category "
            + "or tap the Cancel button to abort the current transaction ⤵";
    public static final String MESSAGE_NO_RECORDS_FOUND = "No records found";
    public static final String MESSAGE_READY = "Done! ✔";
    public static final String MESSAGE_REPORT = "Select period 🗓 ⤵";
    public static final String MESSAGE_SELECTED = " selected ✔";
    public static final String MESSAGE_SELECT_COMMAND = "Tap the main menu button or type / "
            + "and then select the bot command. You can also use predefined buttons ⤵";
    public static final String MESSAGE_SELECT_EXPENSE_CATEGORY = "Select an expense category ⤵";
    public static final String MESSAGE_WELCOME = "This bot helps you track your expenses 🤑";
    public static final String NEW_UPDATE = "New update";
    public static final String REPORT_DELIMITER = ", ";
    public static final String DETAILED_HEADER = "<u>user, date, category, cost, comment</u>";
    public static final String SUMMARY_HEADER = "<u>user, category, cost</u>";
    public static final String TOTAL = "Total";
    public static final String USER = "user";
    public static final String USER_CATEGORY = "userAndCategory";
}
