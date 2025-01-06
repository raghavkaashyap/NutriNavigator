package coms309.Recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    Recipes recipes;

    public List<Recipe> getAllRecipes() throws SQLException{
        return recipes.getAllRecipes();
    }

    public Recipe getRecipeById(int id) throws SQLException{
        return recipes.getRecipeById(id);
    }

    public Recipe getRecipeByName(String name) throws SQLException{
        return recipes.getRecipeByName(name);
    }

    public void addRecipe(Recipe recipe) throws SQLException{
        recipes.addRecipe(recipe);
    }

    public void updateRecipe(int id, Recipe recipe) throws SQLException{
        recipes.updateRecipe(id, recipe);
    }

    public void updateRecipeByName(String name, Recipe recipe) throws SQLException{
        recipes.updateRecipeByName(name, recipe);
    }

    public void deleteRecipeById(int id) throws SQLException{
        recipes.deleteRecipeById(id);
    }

    public void deleteRecipeByName(String name) throws SQLException {
        recipes.deleteRecipeByName(name);
    }
}
