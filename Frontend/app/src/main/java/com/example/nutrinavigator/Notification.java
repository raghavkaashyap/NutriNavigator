package com.example.nutrinavigator;

public class Notification {
    public enum Type {
        RECIPE_CREATED,
        RECIPE_DELETED,
        RECIPE_UPDATED,
        USER_SIGNUP,
        USER_DELETED
    }

    private String id;
    private Type type;
    private String message;
    private String timestamp;
    private String additionalInfo; // For recipe name or user details

    // Constructor, getters, and setters
    // ...

    public String getFormattedMessage() {
        switch (type) {
            case RECIPE_CREATED:
                return "New recipe created: " + additionalInfo;
            case RECIPE_DELETED:
                return "Recipe deleted: " + additionalInfo;
            case RECIPE_UPDATED:
                return "Recipe updated: " + additionalInfo;
            case USER_SIGNUP:
                return "New user signed up: " + additionalInfo;
            case USER_DELETED:
                return "User account deleted: " + additionalInfo;
            default:
                return message;
        }
    }
}
