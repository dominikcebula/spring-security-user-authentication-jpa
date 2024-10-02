package com.dominikcebula.spring.security.user.authentication.users.event;

import com.dominikcebula.spring.security.user.authentication.users.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnUserRegistrationCompletedEvent extends ApplicationEvent {
    private final User user;

    public OnUserRegistrationCompletedEvent(User user) {
        super(user);
        this.user = user;
    }
}
