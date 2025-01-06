package coms309.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EncryptionKeyRepository {
    @Autowired
    private JdbcTemplate jdbc;

    public boolean save(String userId, String key) {
        String sqlQuery = "INSERT INTO encryption_keys (user_id, encryption_key) VALUES (?,?);";
        try {
            jdbc.update(sqlQuery, userId, key);
        } catch (Exception ex) {
            System.out.println("Error while inserting encryption key: " + ex.getMessage());
            return false;
        }
        return true;
    }
}
