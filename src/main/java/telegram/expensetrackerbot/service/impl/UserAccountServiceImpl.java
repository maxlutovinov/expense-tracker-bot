package telegram.expensetrackerbot.service.impl;

import org.springframework.stereotype.Service;
import telegram.expensetrackerbot.model.entity.UserAccount;
import telegram.expensetrackerbot.repository.UserAccountRepository;
import telegram.expensetrackerbot.service.UserAccountService;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private final UserAccountRepository repository;

    public UserAccountServiceImpl(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        return repository.save(userAccount);
    }
}
