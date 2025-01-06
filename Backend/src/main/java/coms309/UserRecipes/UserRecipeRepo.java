package coms309.UserRecipes;

import coms309.Notifications.NotificationController;
import coms309.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRecipeRepo {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }

    //Modify all methods to add and retrieve date

    public void addRecipeToUser(String user, Integer recipeId) throws SQLException {
        String query = "INSERT INTO User_Recipes (recipeId, username) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, recipeId);
            statement.setString(2, user); //maps username to recipeId
            statement.executeUpdate();
            NotificationController.sendNotification("[" + user + "] A recipe was added.");
        }
    }

    public void addRecipeToUser(String user, Recipe recipe) throws SQLException {
        Integer recipeId = addRecipe(recipe);
        String query = "INSERT INTO User_Recipes (recipeId, username) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, recipeId);
            statement.setString(2, user);
            statement.executeUpdate();
            NotificationController.sendNotification("[" + user + "] A recipe was added: " + recipe.getName());
        }
    }

    public List<Recipe> getRecipesByUserName(String user) throws SQLException {
        String query = "SELECT r.* FROM Recipes r JOIN User_Recipes ur ON r.recipeID = ur.recipeID WHERE ur.username = ?";
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
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

    public Recipe getUserRecipeByRecipeName(String user, String recipeName) throws SQLException {
        String query = "SELECT r.* FROM Recipes r JOIN User_Recipes ur ON r.recipeID = ur.recipeID WHERE ur.username = ? AND r.recipe = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user);
            statement.setString(2, recipeName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Recipe recipe = new Recipe();
                recipe.setName(resultSet.getString("recipe"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setCalories(resultSet.getInt("calories"));
                recipe.setDate(resultSet.getDate("date_added").toLocalDate());
                return recipe;
            }
        }
        return null;
    }

    public Recipe getUserRecipeByRecipeID(String username, int recipeID) throws SQLException {
        String query = "SELECT r.* FROM Recipes r JOIN User_Recipes ur ON r.recipeID = ur.recipeID WHERE ur.username = ? AND ur.recipeID = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setInt(2, recipeID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Recipe recipe = new Recipe();
                recipe.setName(resultSet.getString("recipe"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setCalories(resultSet.getInt("calories"));
                recipe.setDate(resultSet.getDate("date_added").toLocalDate());
                return recipe;
            }
        }
        return null;
    }

    public void updateUserRecipe(String username, int recipeID, Recipe recipe) throws SQLException {
        String sql = "UPDATE Recipes SET recipe = ?, ingredients = ?, calories = ? WHERE recipeID = ? AND recipeID IN (SELECT recipeID FROM User_Recipes WHERE username = ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, recipe.getName());
            preparedStatement.setString(2, recipe.getIngredients());
            preparedStatement.setInt(3, recipe.getCalories());
            preparedStatement.setInt(4, recipeID);
            preparedStatement.setString(5, username);
            preparedStatement.executeUpdate();
            NotificationController.sendNotification("[" + username + "] A recipe was added: " + recipe.getName());
        }
    }

    public void deleteUserRecipe(String username, int recipeID) throws SQLException {
        String sql = "DELETE FROM User_Recipes WHERE username = ? AND recipeID = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, recipeID);
            preparedStatement.executeUpdate();
            NotificationController.sendNotification("[" + username + "] A recipe was deleted.");
        }
    }

    private Integer addRecipe(Recipe recipe) throws SQLException {
        String query = "INSERT INTO Recipes (recipe, ingredients, calories, date_added) VALUES (?, ?, ?, CURRENT_DATE)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, recipe.getName());
            statement.setString(2, recipe.getIngredients());
            statement.setInt(3, recipe.getCalories());
            statement.executeUpdate();

            // Retrieve the auto-generated recipeID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating recipe failed, no ID obtained.");
            }
        }
    }

}