package coms309.Preferences;

import coms309.Recipe.DietaryPreferences;
import coms309.Recipe.DietaryRestrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DietaryPreferencesRepo {
    @Autowired
    private JdbcTemplate jdbc;

    //Create
    public boolean insert(int recipeId, DietaryPreferences[] enumValues) {
        for (DietaryPreferences p : enumValues) {
            insert(recipeId, p);
        }
        return true;
    }

    //Create
    public boolean insert(int recipeId, DietaryPreferences enumValue) {
        String sqlQuery = "INSERT INTO preferences_join (recipeId, enum) VALUES (?,?);";
        try {
            jdbc.update(sqlQuery, recipeId, enumValue.ordinal());
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    //Delete
    public boolean delete(int recipeId, DietaryPreferences enumValue) {
        String sqlQuery = "DELETE FROM preferences_join WHERE recipeId = ? AND enum = ?;";
        try {
            jdbc.update(sqlQuery, recipeId, enumValue.ordinal());
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean delete(int recipeId) {
        String sqlQuery = "DELETE FROM preferences_join WHERE recipeId = ?;";
        try {
            jdbc.update(sqlQuery, recipeId);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    //List all
    public List<String> listAll(int recipeId) {
        String sqlQuery = "SELECT d.enum_value FROM Recipes r " +
                "JOIN preferences_join j ON r.recipeID = j.recipeId " +
                "JOIN dietary_preferences d ON j.enum = d.id " +
                "WHERE r.recipeID = ?;";
        try {
            return jdbc.queryForList(sqlQuery, new Object[] {recipeId}, String.class);
        } catch (Exception ex) {
            return null;
        }
    }

    public int getRecipeId(String name) {
        String sqlQuery = "SELECT recipeID FROM Recipes WHERE recipe = ?;";
        try {
            return jdbc.queryForObject(sqlQuery, new Object[] {name}, Integer.class);
        } catch (Exception ex) {
            return -1;
        }
    }
}
