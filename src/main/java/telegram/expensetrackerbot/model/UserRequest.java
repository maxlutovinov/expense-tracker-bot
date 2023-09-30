package telegram.expensetrackerbot.model;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;
import telegram.expensetrackerbot.model.entity.UserAccount;

@Data
@Builder
public class UserRequest {
    private Long chatId;
    private Update update;
    private UserAccount userAccount;
    private UserSession userSession;
}
