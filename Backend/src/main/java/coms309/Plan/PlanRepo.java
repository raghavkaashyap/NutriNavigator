package coms309.Plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PlanRepo {
    @Autowired
    private JdbcTemplate jdbc;

    public boolean insert(int userId, int recipeId, LocalDate date) {
        String sqlQuery = "INSERT INTO plans (userId, recipeId, plannedDate) VALUES (?,?,?);";
        try {
            jdbc.update(sqlQuery, userId, recipeId, date);
        } catch (Exception ex) {
            System.out.println("Exception in insert() of PlanRepo: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean delete(int userId, int recipeId, LocalDate date) {
        String sqlQuery = "DELETE FROM plans WHERE userId = ? AND recipeId = ? AND plannedDate = ?;";
        try {
            jdbc.update(sqlQuery, userId, recipeId, date);
        } catch (Exception ex) {
            System.out.println("Exception in delete() of PlanRepo: " + ex.getMessage());
            return false;
        }
        return true;
    }

//    public boolean update(int userId, int recipeId, LocalDate date) {
//        String sqlQuery = "";
//        try {
//
//        } catch (Exception ex) {
//            System.out.println("Exception in update() of PlanRepo");
//            return false;
//        }
//        return true;
//    }

    public List<Plan> list(int userId) {
        String sqlQuery = "SELECT userId, recipeId, plannedDate FROM plans WHERE userId = ?;";
        try {
            return jdbc.query(sqlQuery, new Object[] { userId }, new PlanMapper());
        } catch (Exception ex) {
            System.out.println("Exception in list() of PlanRepo: " + ex.getMessage());
            return null;
        }
    }
}