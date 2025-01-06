package coms309.Social;

import coms309.Notifications.NotificationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SocialRepository {
    @Autowired
    private JdbcTemplate jdbc;

    //Create
    public boolean insert(int follower, int following) {
        String sqlQuery = "INSERT INTO following_users (follower, following) VALUES (?,?);";
        try {
            jdbc.update(sqlQuery, follower, following);
        } catch (Exception ex) {
            System.out.println("Exception while adding to following_users table: " + ex.getMessage());
            return false;
        }
        NotificationController.sendNotification("Followers list has been updated."); //update to include username
        return true;
    }

    //Delete
    public boolean remove(int follower, int following) {
        String sqlQuery = "DELETE FROM following_users WHERE follower = ? AND following = ?;";
        try {
            jdbc.update(sqlQuery, follower, following);
        } catch (Exception ex) {
            System.out.println("Exception while removing from following_users table: " + ex.getMessage());
            return false;
        }
        NotificationController.sendNotification("Followers list has been updated."); //update to include username
        return true;
    }
}
