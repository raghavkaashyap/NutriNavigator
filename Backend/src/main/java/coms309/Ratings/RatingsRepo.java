package coms309.Ratings;

import coms309.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class RatingsRepo {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }

    public void addRating (Ratings rating) throws SQLException{
        String query = "INSERT INTO RecipeRatings (recipeId, username, rating, date_rated) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, rating.getRecipeId());
            statement.setString(2, rating.getRaterUsername());
            statement.setInt(3, rating.getRating());
            statement.setDate(4, Date.valueOf(LocalDate.now()));
            statement.executeUpdate();
        }
    }

    //add ratings when recipeId isnt known
    public void addRating(Ratings rating, String recipeName) throws SQLException {
        String getRecipeIdQuery = "SELECT recipeId FROM Recipes WHERE recipe = ?";
        String insertRatingQuery = "INSERT INTO RecipeRatings (recipeId, username, rating, date_rated) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement getRecipeIdStatement = connection.prepareStatement(getRecipeIdQuery);
             PreparedStatement insertRatingStatement = connection.prepareStatement(insertRatingQuery)) {
            getRecipeIdStatement.setString(1, recipeName);
            ResultSet resultSet = getRecipeIdStatement.executeQuery();

            if (!resultSet.next()) {
                throw new SQLException("Recipe with name '" + recipeName + "' does not exist.");
            }
            int recipeId = resultSet.getInt("recipeId");
            insertRatingStatement.setInt(1, recipeId);
            insertRatingStatement.setString(2, rating.getRaterUsername());
            insertRatingStatement.setInt(3, rating.getRating());
            insertRatingStatement.setDate(4, Date.valueOf(LocalDate.now()));
            insertRatingStatement.executeUpdate();
        }
    }

    public List<Ratings> getRatingsByRecipe(int recipeId) throws SQLException{
        String query = "SELECT id, recipeId, username, rating, date_rated FROM RecipeRatings WHERE recipeId = ?";
        String idQuery = "SELECT recipe from Recipes where recipeId = ?";
        List<Ratings> ratings = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement statement1 = connection.prepareStatement(idQuery)){
            statement1.setInt(1, recipeId);
            statement.setInt(1, recipeId);
            ResultSet resultSet1 = statement1.executeQuery();
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Ratings rating = new Ratings();
                rating.setId(resultSet.getInt("id"));
                if (resultSet1.next()){
                    rating.setRecipeName(resultSet1.getString("recipe"));
                }
                rating.setRating(resultSet.getInt("rating"));
                rating.setRecipeId(resultSet.getInt("recipeId"));
                rating.setDate_rated(resultSet.getDate("date_rated").toLocalDate());
                rating.setRaterUsername(resultSet.getString("username"));
                ratings.add(rating);
            }
        }
        return ratings;
    }

    //get user's rating of a particular recipe
    public Ratings getUserRatingByRecipeName(String username, String recipeName) throws SQLException {
        String query = "SELECT rr.id, rr.recipeId, rr.rating, rr.date_rated " + "FROM RecipeRatings rr " + "JOIN Recipes r ON rr.recipeId = r.recipeId " +
                "WHERE r.recipe = ? AND rr.username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, recipeName);
            statement.setString(2, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Ratings rating = new Ratings();
                rating.setId(resultSet.getInt("id"));
                rating.setRecipeName(recipeName);
                rating.setRaterUsername(username);
                rating.setRecipeId(resultSet.getInt("recipeId"));
                rating.setRating(resultSet.getInt("rating"));
                rating.setDate_rated(resultSet.getDate("date_rated").toLocalDate());
                return rating;
            } else {
                throw new SQLException("Rating not found for the recipe.");
            }
        }
    }

    //get ratings by recipe name
    public List<Ratings> getRatingsByRecipeName(String recipeName) throws SQLException{
        String query = "SELECT rr.id, rr.recipeId, rr.rating, rr.username, rr.date_rated " + "FROM RecipeRatings rr " + "JOIN Recipes r ON rr.recipeId = r.recipeId " +
                "WHERE r.recipe = ?";
        List<Ratings> ratings = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, recipeName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Ratings rating = new Ratings();
                rating.setRecipeName(recipeName);
                rating.setRecipeId(resultSet.getInt("recipeId"));
                rating.setId(resultSet.getInt("id"));
                rating.setRaterUsername(resultSet.getString("username"));
                rating.setDate_rated(resultSet.getDate("date_rated").toLocalDate());
                rating.setRating(resultSet.getInt("rating"));
                ratings.add(rating);
            }
        }
        return ratings;
    }

    //update to include dietary preferences in return
    public List<Recipe> getRecipesRatedAbove(double threshold) throws SQLException{
        String query = "SELECT r.recipe, r.ingredients, r.calories, r.date_added, AVG(rr.rating) AS average_rating, " + "GROUP_CONCAT(DISTINCT dp.enum_value) AS preferences, " +
                "GROUP_CONCAT(DISTINCT dr.enum_value) AS restrictions " + "FROM Recipes r " + "JOIN RecipeRatings rr ON r.recipeId = rr.recipeId " +
                "LEFT JOIN preferences_join pj ON r.recipeId = pj.recipeId " + "LEFT JOIN dietary_preferences dp ON pj.enum = dp.id " +
                "LEFT JOIN restrictions_join rj ON r.recipeId = rj.recipeId " + "LEFT JOIN dietary_restrictions dr ON rj.enum = dr.id " +
                "GROUP BY r.recipeId " + "HAVING AVG(rr.rating) > ?";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setDouble(1, threshold);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Recipe recipe = new Recipe();
                recipe.setName(resultSet.getString("recipe"));
                recipe.setCalories(resultSet.getInt("calories"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setDate(resultSet.getDate("date_added").toLocalDate());
                String preferences = resultSet.getString("preferences");
                if (preferences != null) {
                    recipe.setDietaryPreferences(Arrays.asList(preferences.split(",")).toString());
                } else {
                    recipe.setDietaryPreferences(String.valueOf(new ArrayList<>()));
                }

                String restrictions = resultSet.getString("restrictions");
                if (restrictions != null) {
                    recipe.setDietaryRestrictions(Arrays.asList(restrictions.split(",")).toString());
                } else {
                    recipe.setDietaryRestrictions(String.valueOf(new ArrayList<>()));
                }
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    //cache average ratings in SQL table, use this + ingredients + bookmarks for recommendations
    public double getAverageRating(int recipeId) throws SQLException{
        String query = "SELECT AVG(rating) AS average_rating FROM RecipeRatings WHERE recipeId = ?";
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){
            statement.setInt(1, recipeId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                return resultSet.getDouble("average_rating");
            }
        }
        return 0.0;
    }

    //average by recipe name
    public double getAverageRatingByRecipeName(String recipeName) throws SQLException{
        String query = "SELECT AVG(rr.rating) AS avg_rating " + "FROM RecipeRatings rr " + "JOIN Recipes r ON rr.recipeId = r.recipeId " +
                "WHERE r.recipe = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){
            statement.setString(1, recipeName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getDouble("avg_rating");
            }
        }
        return 0.0;
    }

    public boolean hasUserRated(int recipeId, String username) throws SQLException{
        String query = "SELECT COUNT(*) AS count FROM RecipeRatings WHERE username = ? AND recipeId = ?";
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, username);
            statement.setInt(2, recipeId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                return resultSet.getInt("count") > 0;
            }
        }
        return false;
    }

    public void updateRating(String username, int recipeId, int newRating) throws SQLException{
        String query = "UPDATE RecipeRatings SET rating = ?, date_rated = ? WHERE username = ? AND recipeId = ?";
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, newRating);
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.setString(3, username);
            statement.setInt(4, recipeId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Rating not found or user not authorized to update.");
            }
        }
    }

    public void updateRatingByRecipeName(String username, String recipeName, int newRating) throws SQLException{
        String query = "UPDATE RecipeRatings rr " + "JOIN Recipes r ON rr.recipeId = r.recipeId " +
                "SET rr.rating = ?, rr.date_rated = ? " +
                "WHERE r.recipe = ? AND rr.username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, newRating);
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.setString(3, recipeName);
            statement.setString(4, username);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Rating not found or user not authorized to update.");
            }
        }
    }

    public void deleteRating(String username, int recipeId) throws SQLException{
        String query = "DELETE FROM RecipeRatings WHERE username = ? AND recipeId = ?";
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, username);
            statement.setInt(2, recipeId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Rating not found or user not authorized to delete.");
            }
        }
    }

    //deletes a user's rating of a recipe by recipe name
    public void deleteRatingByRecipeName(String username, String recipeName) throws SQLException{
        String query = "DELETE rr " + "FROM RecipeRatings rr " + "JOIN Recipes r ON rr.recipeId = r.recipeId " +
                "WHERE r.recipe = ? AND rr.username = ?";
        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, recipeName);
            statement.setString(2, username);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Rating not found or user not authorized to delete.");
            }
        }
    }


    public void deleteRatingsByUser(String username) throws SQLException{
        String query = "DELETE FROM RecipeRatings WHERE username = ?";
        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, username);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0){
                throw new SQLException("No ratings found for this user");
            }
        }
    }

    //delete all ratings under a given recipe (Moderator)
    public void deleteRatingsByRecipe(int recipeId) throws SQLException{
        String query = "DELETE FROM RecipeRatings WHERE recipeId = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, recipeId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0){
                throw new SQLException("No ratings found for this recipe");
            }
        }
    }
}
