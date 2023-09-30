package telegram.expensetrackerbot.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import telegram.expensetrackerbot.model.UserSession;
import telegram.expensetrackerbot.service.UserSessionService;

@Service
public class UserSessionServiceImpl implements UserSessionService {
    private final Map<Long, UserSession> userSessionById = new HashMap<>();

    @Override
    public UserSession get(Long chatId) {
        return userSessionById.getOrDefault(chatId, UserSession
                .builder()
                .chatId(chatId)
                .build());
    }

    @Override
    public UserSession save(Long chatId, UserSession userSession) {
        return userSessionById.put(chatId, userSession);
    }
}
