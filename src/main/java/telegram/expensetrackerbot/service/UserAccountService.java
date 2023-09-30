package telegram.expensetrackerbot.service;

import telegram.expensetrackerbot.model.entity.UserAccount;

public interface UserAccountService {
    UserAccount save(UserAccount userAccount);
}
