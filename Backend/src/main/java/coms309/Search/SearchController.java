package coms309.Search;

import coms309.APIResponse;
import coms309.Preferences.DietaryPreferencesService;
import coms309.Preferences.DietaryRestrictionsService;
import coms309.Recipe.Recipe;
import coms309.Recipe.RecipeService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@OpenAPIDefinition(info = @Info(title = "Controller for thorough searching Recipes", version = "1.0"))
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private DietaryRestrictionsService restrictionsService;

    @Autowired
    private DietaryPreferencesService preferencesService;

    @Operation(summary = "Search", description = "Allows any user to search all Recipes by upload user, ingredients, calorie range, date range, dietary preferences, and dietary restrictions")
    @GetMapping("/search")
    public APIResponse<List<Recipe>> search(@RequestParam(required = false) String uploaduser, @RequestParam(required = false) String ingredients,
                                             @RequestParam(required = false, defaultValue = "-1") int calorieslow, @RequestParam(required = false, defaultValue = "-1") int calorieshigh,
                                             @RequestParam(required = false) String datestart, @RequestParam(required = false) String dateend,
                                             @RequestParam(required = false) String dietaryrestrictions, @RequestParam(required = false) String dietarypreferences) {
        List<Recipe> candidates = null;
        try {
            candidates = recipeService.getAllRecipes();
            for (Recipe r : candidates) {
                restrictionsService.fillRestrictions(r);
                preferencesService.fillPreferences(r);
            }
            candidates = searchService.search(candidates, uploaduser, ingredients, calorieslow, calorieshigh, datestart, dateend, dietaryrestrictions, dietarypreferences);
        } catch (SQLException ex) {
            return new APIResponse<>(null, "Could not search sql database");
        }
        if (candidates == null) {
            return new APIResponse<>(null, "No results to return");
        }
        return new APIResponse<>(candidates, "Recipes successfully searched");
    }
}
