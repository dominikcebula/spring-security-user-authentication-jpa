package com.dominikcebula.spring.security.user.authentication.activationlink.event;

import com.dominikcebula.spring.security.user.authentication.activationlink.service.ActivationLinkService;
import com.dominikcebula.spring.security.user.authentication.users.event.OnUserRegistrationCompletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnUserRegistrationCompletedListener implements ApplicationListener<OnUserRegistrationCompletedEvent> {
    @Autowired
    private ActivationLinkService activationLinkService;

    @Override
    public void onApplicationEvent(OnUserRegistrationCompletedEvent event) {
        activationLinkService.createAndSendActivationLink(event.getUser());
    }
}
