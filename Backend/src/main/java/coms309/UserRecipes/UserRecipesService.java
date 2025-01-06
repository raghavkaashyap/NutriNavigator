package coms309.UserRecipes;

import coms309.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserRecipesService {
    @Autowired
    private UserRecipeRepo userRecipeRepo; // Assuming you have a repository for database operations

    // Add a recipe for a user
    public void addRecipeToUser(String username, Integer recipeId) throws SQLException {
        userRecipeRepo.addRecipeToUser(username, recipeId);
    }

    public void addRecipeToUser(String username, Recipe recipe) throws SQLException{
        userRecipeRepo.addRecipeToUser(username, recipe);
    }

    // Get all recipes for a user
    public List<Recipe> getRecipesByUser(String username) throws SQLException {
        return userRecipeRepo.getRecipesByUserName(username);
    }

    // Get a specific recipe by user and recipe ID
    public Recipe getUserRecipeByID(String username, Integer recipeId) throws SQLException {
        return userRecipeRepo.getUserRecipeByRecipeID(username, recipeId);
    }

    // Get a specific recipe by user and recipe name
    public Recipe getUserRecipeByRecipeName(String username, String recipeName) throws SQLException {
        return userRecipeRepo.getUserRecipeByRecipeName(username, recipeName);
    }

    // Update a recipe for a user
    public void updateUserRecipe(String username, Integer recipeId, Recipe recipe) throws SQLException {
        userRecipeRepo.updateUserRecipe(username, recipeId, recipe);
    }

    // Delete a recipe for a user
    public void deleteUserRecipe(String username, Integer recipeId) throws SQLException {
        userRecipeRepo.deleteUserRecipe(username, recipeId);
    }
}
