package coms309.Messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessagesRepository {
    @Autowired
    private JdbcTemplate jdbc;

    public boolean insert(Message m) {
        String sqlQuery = "INSERT INTO admin_messages (resolved, user_submitted, subject, message, date_submitted, date_resolved, user_resolved) VALUES (?,?,?,?,?,?,?);";
        try {
            jdbc.update(sqlQuery, m.getResolved(), m.getUserSubmitted(), m.getSubject(), m.getMessage(), m.getDateSubmitted(), m.getDateResolved(), m.getUserResolved());
        } catch (Exception ex) {
            System.out.println("Exception while adding message: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public Message findById(int id) {
        String sqlQuery = "SELECT * FROM admin_messages WHERE id = ?;";
        try {
            return jdbc.queryForObject(sqlQuery, new Object[]{id}, new MessageMapper());
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean existsById(int id) {
        String sqlQuery = "SELECT COUNT(*) FROM admin_messages WHERE id = ?;";
        int count = 0;
        count = jdbc.queryForObject(sqlQuery, new Object[]{id}, Integer.class);
        return count > 0;
    }

    public List<Message> listMessages() {
        String sqlQuery = "SELECT * FROM admin_messages ORDER BY date_submitted LIMIT 10;";
        try {
            return jdbc.query(sqlQuery, new MessageMapper());
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean updateResolved(int id, int resolved) {
        String sqlQuery = "UPDATE admin_messages SET resolved = ? WHERE id = ?;";
        try {
            jdbc.update(sqlQuery, resolved, id);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean updateDateResolved(int id, long date) {
        String sqlQuery = "UPDATE admin_messages SET date_resolved = ? WHERE id = ?;";
        try {
            jdbc.update(sqlQuery, date, id);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean updateUserResolved(int id, String user) {
        String sqlQuery = "UPDATE admin_messages SET user_resolved = ? WHERE id = ?;";
        try {
            jdbc.update(sqlQuery, user, id);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean updateSubjectAndMessage(int id, String subject, String message) {
        String sqlQuery = "UPDATE admin_messages SET subject = ?, message = ? WHERE id = ?;";
        try {
            jdbc.update(sqlQuery, subject, message, id);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean deleteById(int id) {
        String sqlQuery = "DELETE FROM admin_messages WHERE id = ?;";
        try {
            jdbc.update(sqlQuery, id);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
