package coms309.Users;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private UserType userType;
    private String email;
    private long banTime;
    private long dateCreated;
    private long lastLogin;

//    public User(String username, String passwordHash, int userType) {
//        this(username, passwordHash, UserType.values()[userType]);
//    }
//
//    public User(String username, String passwordHash, UserType userType) {
//        this.username = username;
//        this.passwordHash = passwordHash;
//        this.userType = userType;
//        this.banTime = 0;
//    }

    public User(String username, String passwordHash, int userType, String email,
                long banTime, long dateCreated, long lastLogin) {
        this(username, passwordHash, UserType.values()[userType], email, banTime, dateCreated, lastLogin);
    }

    public User(String username, String passwordHash, UserType userType, String email,
                long banTime, long dateCreated, long lastLogin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.email = email;
        this.banTime = banTime;
        this.dateCreated = dateCreated;
        this.lastLogin = lastLogin;
    }

    public User(int id, String username, String passwordHash, int userType, String email,
                long banTime, long dateCreated, long lastLogin) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.userType = UserType.values()[userType];
        this.email = email;
        this.banTime = banTime;
        this.dateCreated = dateCreated;
        this.lastLogin = lastLogin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBanTime(long banTime) {
        this.banTime = banTime;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getEmail() {
        return email;
    }

    public long getBanTime() {
        return banTime;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public long getLastLogin() {
        return lastLogin;
    }
}
