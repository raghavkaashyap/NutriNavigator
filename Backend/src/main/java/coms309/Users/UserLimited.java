package coms309.Users;


//Limited user information for sending to client application to not expose too much information from other users
public class UserLimited {
    private int id;
    private String username;
    private UserType userType;

    public UserLimited(int id, String username, UserType userType) {
        this.id = id;
        this.username = username;
        this.userType = userType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUserType() {
        return userType;
    }
}