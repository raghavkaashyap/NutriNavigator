package coms309.Recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class LiveTrackingRepository {
    @Autowired
    private JdbcTemplate jdbc;

    //Create
    public boolean insert(int recipeId, int pulls, Date date) {
        String sqlQuery = "INSERT INTO popular_recipes_log (recipeId, pulls, date_logged) VALUES (?,?,?);";
        try {
            jdbc.update(sqlQuery, recipeId, pulls, date);
        } catch (Exception ex) {
            System.out.println("Exception while adding to popular_recipes_log table: " + ex.getMessage());
            return false;
        }
        return true;
    }

    //List
    public List<LiveTrackingObject> listLogs() {
        return listLogs(20);
    }
    //List
    public List<LiveTrackingObject> listLogs(int cap) {
        String sqlQuery = "SELECT * FROM popular_recipes_log ORDER BY date_logged LIMIT ?;";
        try {
            return jdbc.query(sqlQuery, new Object[] {cap}, new LiveTrackingObjectMapper());
        } catch (Exception ex) {
            System.out.println("Exception while listing popular_recipes_log: " + ex.getMessage());
            return null;
        }
    }
}
