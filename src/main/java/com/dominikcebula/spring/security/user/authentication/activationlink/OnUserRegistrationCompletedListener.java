package com.dominikcebula.spring.security.user.authentication.activationlink;

import com.dominikcebula.spring.security.user.authentication.users.events.OnUserRegistrationCompletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnUserRegistrationCompletedListener implements ApplicationListener<OnUserRegistrationCompletedEvent> {
    @Autowired
    private ActivationLinkService activationLinkService;

    @Override
    public void onApplicationEvent(OnUserRegistrationCompletedEvent event) {
        activationLinkService.sendActivationLink(event.getUser());
    }
}
