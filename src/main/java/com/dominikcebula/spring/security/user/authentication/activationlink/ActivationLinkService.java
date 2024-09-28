package com.dominikcebula.spring.security.user.authentication.activationlink;

import com.dominikcebula.spring.security.user.authentication.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivationLinkService {
    @Autowired
    private ActivationTokenFactory activationTokenFactory;
    @Autowired
    private ActivationLinkRepository activationLinkRepository;

    public void createAndSendActivationLink(User user) {
        String activationToken = activationTokenFactory.create();
        ActivationLink activationLink = new ActivationLink(user, activationToken);

        activationLinkRepository.save(activationLink);
    }
}
