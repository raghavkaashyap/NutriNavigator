package coms309.Users;

import coms309.Notifications.NotificationController;
import coms309.Security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private EncryptionKeyRepository encryptionKeyRepository;

    public boolean insert(User newUser) {
        String sqlQuery = "INSERT INTO users (username, passwordHash, usertype, email, ban_time, date_created, last_login) VALUES (?,?,?,?,?,?,?);";
        try {
            jdbc.update(sqlQuery, newUser.getUsername(), newUser.getPasswordHash(), newUser.getUserType().ordinal(), newUser.getEmail(), newUser.getBanTime(), newUser.getDateCreated(), newUser.getLastLogin());
        } catch (Exception ex) {
            System.out.println("User add exception: " + ex.getMessage());
            return false;
        }
        NotificationController.sendNotification("[" + newUser.getUsername() + "] Has been created.");
        return true;
    }

    public boolean exists(User user) {
        return exists(user.getUsername());
    }

    public boolean exists(String username) {
        String sqlQuery = "SELECT COUNT(*) FROM users WHERE username = ?;";
        int count = 0;
        count = jdbc.queryForObject(sqlQuery, new Object[]{username}, Integer.class);
        return count > 0;
    }

    public User find(String username) {
        String sqlQuery = "SELECT * FROM users WHERE username = ?;";
        return jdbc.queryForObject(sqlQuery, new Object[] {username}, new UserMapper());
    }

    public User find(User user) {
        return find(user.getUsername());
    }

    public List<User> listRecent() {
        String sqlQuery = "SELECT id, username, passwordHash, usertype, email, ban_time, date_created, last_login FROM users ORDER BY last_login LIMIT 50;";
        return jdbc.query(sqlQuery, new UserMapper());
    }

    public boolean delete(String username) {
        String sqlQuery = "DELETE FROM users WHERE username = ?;";
        try {
            jdbc.update(sqlQuery, username);
        } catch (Exception ex) {
            return false;
        }
        NotificationController.sendNotification("[" + username + "] Has been deleted.");
        return true;
    }

    public boolean updateUserType(String username, UserType type) {
        String sqlQuery = "UPDATE users SET usertype = ? WHERE username = ?;";
        try {
            jdbc.update(sqlQuery, type.ordinal(), username);
        } catch (Exception ex) {
            return false;
        }
        NotificationController.sendNotification("[" + username + "] Control has been updated.");
        return true;
    }

    public boolean updateEmail(String username, String email) {
        String sqlQuery = "UPDATE users SET email = ? WHERE username = ?;";
        try {
            jdbc.update(sqlQuery, email, username);
        } catch (Exception ex) {
            return false;
        }
        NotificationController.sendNotification("[" + username + "] Email has been updated.");
        return true;
    }

    public boolean updatePasswordHash(String username, String passwordHash) {
        String sqlQuery = "UPDATE users SET passwordHash = ? WHERE username = ?;";
        try {
            jdbc.update(sqlQuery, passwordHash, username);
        } catch (Exception ex) {
            return false;
        }
        NotificationController.sendNotification("[" + username + "] Password has been updated.");
        return true;
    }

    public boolean updateBanTime(String username, long bantime) {
        String sqlQuery = "UPDATE users SET ban_time = ? WHERE username = ?;";
        try {
            jdbc.update(sqlQuery, bantime, username);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean updateLastLogin(String username, long lastLogin) {
        String sqlQuery = "UPDATE users SET last_login = ? WHERE username = ?";
        try {
            jdbc.update(sqlQuery, lastLogin, username);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public EncryptionKey getEncryptionKey(String username) {
        String sqlQuery = "SELECT e.* FROM users u " +
                "JOIN encryption_keys e ON u.username = e.user_id " +
                "WHERE username = ?;";
        try {
            return jdbc.queryForObject(sqlQuery, new Object[] {username}, new EncryptionKeyMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public boolean deleteEncryptionKey(String username) {
        String sqlQuery = "DELETE e FROM encryption_keys e JOIN users u " +
                "ON e.user_id = u.username WHERE u.username = ?";
        try {
            jdbc.update(sqlQuery, username);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public List<User> listFollowed(int userId) {
        String sqlQuery = "SELECT u.* FROM users u " +
                "JOIN following_users f ON u.id = f.following " +
                "WHERE f.follower = ?;";
        try {
            return jdbc.query(sqlQuery, new Object[] {userId}, new UserMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public List<User> listFollowers(int userId) {
        String sqlQuery = "SELECT u.* FROM users u " +
                "JOIN following_users f on u.id = f.follower " +
                "WHERE f.following = ?;";
        try {
            return jdbc.query(sqlQuery, new Object[] {userId}, new UserMapper());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
