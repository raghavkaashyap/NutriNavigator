package coms309.Recommendations;

import coms309.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationsController {

    @Autowired
    RecommendationsService recommendationsService;

    @GetMapping
    public ResponseEntity<List<Recipe>> getRecommendations(@RequestParam String username) throws SQLException {
        Random random = new Random();
        int randomInt = random.nextInt(4);
        List<Recipe> recipes;
        try {
            switch (randomInt) {
                case 0:
                    recipes = recommendationsService.findHighlyRatedRecipes();
                    break;
                case 1:
                    recipes = recommendationsService.findRecipeByIngredients(username);
                    break;
                case 2:
                    recipes = recommendationsService.findRecipeByPreferences(username);
                    break;
                case 3:
                    recipes = recommendationsService.recommendPopularRecipes();
                    break;
                default:
                    recipes = Collections.emptyList();
            }
            return ResponseEntity.ok(recipes);
        } catch (SQLException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
