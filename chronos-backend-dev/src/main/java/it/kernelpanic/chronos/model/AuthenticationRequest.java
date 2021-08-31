package it.kernelpanic.chronos.model;

import java.io.Serializable;

/**
 * With this class we generates objects serializable to handle authentication.
 * We need them to be serializable since we will call them remotely invoking
 * objects and not methods.
 * For the authentication we need both username and password that are being
 * checked on the database.
 */
public class AuthenticationRequest implements Serializable {
    private String username;
    private String password;

    public AuthenticationRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
