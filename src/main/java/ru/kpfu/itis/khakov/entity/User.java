package ru.kpfu.itis.khakov.entity;

/**
 * @author Rustam Khakov on 01.07.2017.
 */
public class User {
    private Long id;
    private String username;
    private String password;
    private String accessToken;
    private String secretAccessToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSecretAccessToken() {
        return secretAccessToken;
    }

    public void setSecretAccessToken(String secretAccessToken) {
        this.secretAccessToken = secretAccessToken;
    }
}
