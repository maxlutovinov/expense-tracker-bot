package telegram.expensetrackerbot.service;

import telegram.expensetrackerbot.model.UserSession;

public interface UserSessionService {
    UserSession get(Long chatId);

    UserSession save(Long chatId, UserSession session);

}
