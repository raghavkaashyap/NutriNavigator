package com.example.nutrinavigator;


public class User {
    private int id;
    private String username;
    private String userType;

    private String followingUsername;

    public User(int id, String username, String userType) {
        this.id = id;
        this.username = username;
        this.userType = userType;
    }

    public User(String followingUsername) {
        this.followingUsername= followingUsername;
    }

    // Getter for followingUsername
    public String getFollowingUsername() {
        return followingUsername;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }
}

