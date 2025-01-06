package coms309.Recipe;

import coms309.Notifications.NotificationController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//Data access object
@Repository
public class Recipes {

    //Access and retrieve data from the Recipes table created through Workbench

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;
    private Recipe recipe;
    public Recipes(){
        this.recipe = null;
    }

    public Recipes(String name, int calories, String ingredients){
        recipe = new Recipe(name, ingredients, calories);
    }

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(dbUrl, username, password);
    }

    public List<Recipe> getAllRecipes() throws SQLException{
        List<Recipe> recipes = new ArrayList<>();
        String queryString = "Select * from Recipes";

        try(Connection conn = getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(queryString)){

            while(resultSet.next()){
                Recipe recipe = new Recipe();
                recipe.setName(resultSet.getString("recipe"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setCalories(resultSet.getInt("calories"));
                recipe.setDate(resultSet.getDate("date_added").toLocalDate()); // modification to retrieve sql date and convert it to local date
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    public Recipe getRecipeById(int id) throws SQLException{
        String queryString = "Select * from Recipes where recipeID = ?";
        Recipe recipe = null;

        try(Connection conn = getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(queryString);){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                recipe = new Recipe();
                recipe.setName(resultSet.getString("recipe"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setCalories(resultSet.getInt("calories"));
                recipe.setDate(resultSet.getDate("date_added").toLocalDate()); // modification to retrieve sql date and convert it to local date
            }
        }

        return recipe;
    }

    public Recipe getRecipeByName(String name) throws SQLException{
        String queryString = "Select * from Recipes where recipe like ?";
        Recipe recipe = null;

        try(Connection conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(queryString);){
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                recipe = new Recipe();
                recipe.setName(resultSet.getString("recipe"));
                recipe.setIngredients(resultSet.getString("ingredients"));
                recipe.setCalories(resultSet.getInt("calories"));
                recipe.setDate(resultSet.getDate("date_added").toLocalDate()); // modification to retrieve sql date and convert it to local date
            }
        }

        return recipe;
    }
    public void addRecipe(Recipe recipe) throws SQLException{
        String queryString = "Insert into Recipes(recipe, ingredients, calories, date_added) values(?, ?, ?, ?)"; //update to include date
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, recipe.getName());
            preparedStatement.setString(2, recipe.getIngredients());
            preparedStatement.setInt(3, recipe.getCalories());
            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();
            NotificationController.sendNotification("A new recipe was created: " + recipe.getName());
        }
    }

    //update recipe by id
    public void updateRecipe(int id, Recipe recipe) throws SQLException{
        String queryString = "Update Recipes set recipe = ?, ingredients = ?, calories = ? where recipeID = ?";
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            preparedStatement.setString(1, recipe.getName());
            preparedStatement.setString(2, recipe.getIngredients());
            preparedStatement.setInt(3, recipe.getCalories());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
            NotificationController.sendNotification("The recipe was updated: " + recipe.getName());
        }
    }

    public void updateRecipeByName(String name, Recipe recipe) throws SQLException{
        String queryString = "Update Recipes set recipe = ?, ingredients = ?, calories = ? where recipe like ?";
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            preparedStatement.setString(1, recipe.getName());
            preparedStatement.setString(2, recipe.getIngredients());
            preparedStatement.setInt(3, recipe.getCalories());
            preparedStatement.setString(4, name);
            preparedStatement.executeUpdate();
            NotificationController.sendNotification("The recipe was updated: " + recipe.getName());
        }
    }

    //delete recipe by id
    public void deleteRecipeById(int id) throws SQLException{
        String queryString = "Delete from Recipes where recipeID = ?";
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            NotificationController.sendNotification("A recipe was deleted.");
        }
    }

    //delete recipe by name
    public void deleteRecipeByName(String name) throws SQLException{
        String queryString = "Delete from Recipes where (recipe like ? and recipeId > 0)";
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            NotificationController.sendNotification("A recipe was deleted: " + name);
        }
    }

}
