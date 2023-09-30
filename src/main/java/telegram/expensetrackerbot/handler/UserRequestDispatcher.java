package telegram.expensetrackerbot.handler;

import telegram.expensetrackerbot.model.UserRequest;

public interface UserRequestDispatcher {
    boolean dispatch(UserRequest userRequest);
}
