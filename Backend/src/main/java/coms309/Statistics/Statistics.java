package coms309.Statistics;

import coms309.Recipe.Recipe;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Statistics {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }

    private List<Recipe> getRecipesInDateRange(String username, LocalDate startDate) throws SQLException{
        List<Recipe> recipes = new ArrayList<>();
        String query = "SELECT r.recipe, r.ingredients, r.calories, r.date_added " +
                "FROM Recipes r " +
                "JOIN User_Recipes ur ON r.recipeId = ur.recipeId " +
                "WHERE ur.username = ? AND r.date_added >= ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setDate(2, Date.valueOf(startDate));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Recipe recipe = new Recipe();
                recipe.setName(resultSet.getString("recipe"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setCalories(resultSet.getInt("calories"));
                recipe.setDate(resultSet.getDate("date_added").toLocalDate());
                recipes.add(recipe);
            }
        }
        return recipes;
    }
    public List<Recipe> getYearToDate(String username) throws SQLException{
        LocalDate yearBeginning = LocalDate.now().withDayOfYear(1);
        return getRecipesInDateRange(username, yearBeginning);
    }

    public List<Recipe> getLastSixMonths(String username) throws SQLException{
        LocalDate sixMonths = LocalDate.now().minusMonths(6);
        return getRecipesInDateRange(username, sixMonths);
    }

    public List<Recipe> getLastYear(String username) throws SQLException{
        LocalDate lastYear = LocalDate.now().minusYears(1);
        return getRecipesInDateRange(username, lastYear);
    }

    public List<Recipe> getThisMonth(String username) throws SQLException{
        LocalDate startMonth = LocalDate.now().withDayOfMonth(1);
        return getRecipesInDateRange(username, startMonth);
    }

    public List<Recipe> getThisWeek(String username) throws SQLException{
        LocalDate startWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        return getRecipesInDateRange(username, startWeek);
    }

    //get user recipes within given date range

    public List<Recipe> getWithinRange(String username, LocalDate startDate, LocalDate endDate) throws SQLException{
        List<Recipe> recipes = new ArrayList<>();
        String query = "SELECT r.recipe, r.ingredients, r.calories, r.date_added " + "FROM Recipes r " + "JOIN User_Recipes ur ON r.recipeId = ur.recipeId " +
                "WHERE ur.username = ? AND r.date_added BETWEEN ? AND ?";

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, username);
            statement.setDate(2, Date.valueOf(startDate));
            statement.setDate(3, Date.valueOf(endDate));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Recipe recipe = new Recipe();
                recipe.setName(resultSet.getString("recipe"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setCalories(resultSet.getInt("calories"));
                recipe.setDate(resultSet.getDate("date_added").toLocalDate());
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    //get user recipes by month and year
    public List<Recipe> getRecipeForMonth(String username, int month, int year) throws SQLException{
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return getWithinRange(username, startDate, endDate);
    }

    public int getFollowers(String username) throws SQLException{
        String query = "SELECT COUNT(*) AS totalFollowers " + "FROM following_users fu " + "WHERE fu.following = (SELECT id FROM users WHERE username = ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("totalFollowers");
            }
        }
        return 0;
    }

    public int getFollowing(String username) throws SQLException{
        String query = "SELECT COUNT(*) AS totalFollowing " + "FROM following_users fu " + "WHERE fu.follower = (SELECT id FROM users WHERE username = ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("totalFollowing");
            }
        }
        return 0;
    }

    public double averageCaloriesPerUserRecipe(String username) throws SQLException{
        String query = "SELECT AVG(r.calories) AS averageCalories FROM Recipes r " + "JOIN User_Recipes ur ON r.recipeID = ur.recipeID " + "WHERE ur.username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getDouble("averageCalories");
            }
        }
        return 0.0;
    }

    public int getNumberOfRegularUsers() throws SQLException{
        String query = "SELECT COUNT(*) FROM users WHERE usertype = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, 2);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public Map<String, Integer> getNumberOfRecipesPerUser() throws SQLException{
        String query = "SELECT u.username, COUNT(ur.recipeId) AS recipe_count " + "FROM users u LEFT JOIN User_Recipes ur ON u.username = ur.username " + "GROUP BY u.username";
        Map<String, Integer> countMap = new HashMap<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String username = resultSet.getString("username");
                int recipeCount = resultSet.getInt("recipe_count");
                countMap.put(username, recipeCount);
            }
        }
        return countMap;
    }
}
