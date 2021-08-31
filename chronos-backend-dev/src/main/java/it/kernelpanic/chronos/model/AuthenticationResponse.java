package it.kernelpanic.chronos.model;

import java.io.Serializable;

/**
 * Class that handles the response for the authentication part. Once authenticated
 * into the system, we must generate remote object (serializable) that will handle
 * both session and authorization. Since our web application uses a stateless
 * paradigm, we will need jwt to do this.
 */
public class AuthenticationResponse implements Serializable {
    private final String jwt;

    private String secret;

    private String avatar;

    private long uid;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public AuthenticationResponse(String jwt, String secret) {
        this.jwt = jwt;
        this.secret = secret;
    }

    public AuthenticationResponse(String jwt, String secret, long uid) {
        this.jwt = jwt;
        this.secret = secret;
        this.uid = uid;
    }

    public AuthenticationResponse(String jwt, String secret, String avatar) {
        this.jwt = jwt;
        this.secret = secret;
        this.avatar = avatar;
    }

    public AuthenticationResponse(String jwt, String secret, String avatar, long uid) {
        this.jwt = jwt;
        this.secret = secret;
        this.avatar = avatar;
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getJwt() {
        return jwt;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
