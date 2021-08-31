package it.kernelpanic.chronos.model;

/**
 * Class used to generate users object for the authentication via Google.
 * We created a separate object to handle authentication with Google to avoid
 * possible conflicts using the same class.
 */
public class FakeUser {
    private long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String secret_key;
    private String picture;

    public FakeUser() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "FakeUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", secret_key='" + secret_key + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
