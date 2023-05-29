package com.example.groupproject.User;

public class User {
    private String userName;
    private String email;
    private String password;
    private String profilePictureUrl;

    public User() {
    }

    public User(String userName, String email, String password, String profilePictureUrl) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
