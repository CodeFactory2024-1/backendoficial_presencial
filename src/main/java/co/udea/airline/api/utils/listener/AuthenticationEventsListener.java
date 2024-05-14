package co.udea.airline.api.utils.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import co.udea.airline.api.model.jpa.model.Person;
import co.udea.airline.api.service.LoginAttemptService;

@Component
public class AuthenticationEventsListener {

    @Autowired
    LoginAttemptService loginAttemptService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent success) {
        if (success.getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
                    loginAttemptService
                            .loginSucceededFor( ((Person) success
                                    .getAuthentication()
                                    .getPrincipal()).getEmail());
                }
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent failure) {
        if (failure.getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
            loginAttemptService.loginFailedFor(failure
                    .getAuthentication()
                    .getPrincipal().toString());
        }
    }

}
