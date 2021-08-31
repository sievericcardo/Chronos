package it.kernelpanic.chronos.model;

import java.util.Objects;

/**
 * User model for simple user objects. They are used for standard registretion
 * and login (not google).
 * For every user, we store also the parameters for security as well as the image
 * for the avatar
 */
public class User {

    private long id;
    private String username;
    private String email;
    private String password;
    private String security_parameters;
    private String auth_provider;
    private String avatar;

    public User(String username, String email, String password, String security_parameters, String avatar) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.security_parameters = security_parameters;
        this.avatar = avatar;
    }

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurity_parameters() {
        return security_parameters;
    }

    public void setSecurity_parameters(String security_parameters) {
        this.security_parameters = security_parameters;
    }

    public String getAuth_provider() {
        return auth_provider;
    }

    public void setAuth_provider(String auth_provider) {
        this.auth_provider = auth_provider;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getEmail(), getPassword(), getSecurity_parameters(), getAuth_provider());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", security_parameters='" + security_parameters + '\'' +
                ", auth_provider='" + auth_provider + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
