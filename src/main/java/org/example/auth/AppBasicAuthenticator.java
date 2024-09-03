package org.example.auth;

import com.google.common.collect.ImmutableSet;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import java.util.Optional;
import java.util.Set;

public class AppBasicAuthenticator implements Authenticator<BasicCredentials, User> {

    private final String login;
    private final String password;

    public AppBasicAuthenticator(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        System.out.println("Authenticating user: " + credentials.getUsername());

        if (login.equals(credentials.getUsername()) && password.equals(credentials.getPassword())) {
            System.out.println("User authenticated successfully: " + credentials.getUsername());
            return Optional.of(new User(credentials.getUsername(), ImmutableSet.of("USER")));
        }

        System.out.println("Authentication failed for user: " + credentials.getUsername());
        return Optional.empty();
    }
}
