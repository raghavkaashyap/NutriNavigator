package coms309.Recommendations;

import coms309.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RecommendationsService {

    @Autowired
    RecommendationsRepo recommendationsRepo;

    public List<Recipe> findHighlyRatedRecipes() throws SQLException {
        return recommendationsRepo.findHighlyRatedRecipes();
    }

    public List<Recipe> findRecipeByIngredients(String username) throws SQLException{
        return recommendationsRepo.findRecipeByIngredients(username);
    }

    public List<Recipe> findRecipeByPreferences(String username) throws SQLException{
        return recommendationsRepo.findRecipeByPreferences(username);
    }

    public List<Recipe> recommendPopularRecipes() throws SQLException{
        return recommendationsRepo.recommendPopularRecipes();
    }
}
