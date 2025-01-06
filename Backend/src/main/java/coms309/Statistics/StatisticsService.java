package coms309.Statistics;

import coms309.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private Statistics statistics;
    public List<Recipe> getYearToDate(String username) throws SQLException {
        return statistics.getYearToDate(username);
    }

    public List<Recipe> getLastSixMonths(String username) throws SQLException{
        return statistics.getLastSixMonths(username);
    }

    public List<Recipe> getLastYear(String username) throws SQLException{
        return statistics.getLastYear(username);
    }

    public List<Recipe> getThisMonth(String username) throws SQLException{
        return statistics.getThisMonth(username);
    }

    public List<Recipe> getThisWeek(String username) throws SQLException{
        return statistics.getThisWeek(username);
    }

    public List<Recipe> getWithinRange(String username, LocalDate startDate, LocalDate endDate) throws SQLException{
        return statistics.getWithinRange(username, startDate, endDate);
    }

    public List<Recipe> getRecipeForMonth(String username, int month, int year) throws SQLException{
        return statistics.getRecipeForMonth(username, month, year);
    }

    public int getFollowerCount(String username) throws SQLException{
        return statistics.getFollowers(username);
    }

    public int getFollowingCount(String username) throws SQLException{
        return statistics.getFollowing(username);
    }

    public double averageCaloriesPerUser(String username) throws SQLException{
        return statistics.averageCaloriesPerUserRecipe(username);
    }

    public int getNumberOfRegularUsers() throws SQLException{
        return statistics.getNumberOfRegularUsers();
    }

    public Map<String, Integer> getNumberOfRecipesPerUser() throws SQLException{
        return statistics.getNumberOfRecipesPerUser();
    }
}
