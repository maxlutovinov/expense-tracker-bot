package telegram.expensetrackerbot.handler.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import telegram.expensetrackerbot.handler.UserRequestDispatcher;
import telegram.expensetrackerbot.handler.UserRequestHandler;
import telegram.expensetrackerbot.model.UserRequest;

@Component
public class UserRequestDispatcherImpl implements UserRequestDispatcher {
    private final List<UserRequestHandler> handlers;

    public UserRequestDispatcherImpl(List<UserRequestHandler> handlers) {
        this.handlers = handlers.stream()
                .sorted(Comparator
                        .comparing(UserRequestHandler::isGlobal)
                        .reversed())
                .collect(Collectors.toList());
    }

    @Override
    public boolean dispatch(UserRequest userRequest) {
        for (UserRequestHandler handler : handlers) {
            if (handler.isApplicable(userRequest)) {
                handler.handle(userRequest);
                return true;
            }
        }
        return false;
    }
}
