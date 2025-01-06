package coms309.Recipe;

import coms309.Preferences.DietaryPreferencesService;
import coms309.Preferences.DietaryRestrictionsService;
import coms309.Security.JWTService;
import coms309.Users.User;
import coms309.Users.UserRepository;
import coms309.Users.UserType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@OpenAPIDefinition(info = @Info(title = "Controller for Recipe handling", version = "1.0"))
@RestController
@RequestMapping("/api/recipes")
@Tag(name = "Recipes API", description = "Endpoints for recipe handling")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private DietaryRestrictionsService restrictionsService;
    @Autowired
    private DietaryPreferencesService preferencesService;

    @Operation(summary = "Retrieves all recipes", description = "Retrieves all the recipes in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException {
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)){
            List<Recipe> recipes = recipeService.getAllRecipes();

            for (Recipe r : recipes) {
                restrictionsService.fillRestrictions(r);
                preferencesService.fillPreferences(r);
            }

            return ResponseEntity.ok(recipes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Retrieves a recipe by id", description = "Retrieves one recipe by its unique id from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/getId/{id}")
    public ResponseEntity<Recipe> getRecipeById(@RequestParam String username, @PathVariable Integer id, @RequestHeader("Authorization") String encryptedToken) throws SQLException {
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)) {
            Recipe recipe = recipeService.getRecipeById(id);
            if (recipe != null) {
                restrictionsService.fillRestrictions(recipe);
                preferencesService.fillPreferences(recipe);
                return ResponseEntity.ok(recipe);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Retrieves a recipe by its name", description = "Retrieves one recipe by name from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/getName/{name}")
    public ResponseEntity<Recipe> getRecipeByName(@RequestParam String username, @PathVariable String name, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)) {
            Recipe recipe = recipeService.getRecipeByName(name);
            if (recipe != null) {
                restrictionsService.fillRestrictions(recipe);
                preferencesService.fillPreferences(recipe);
                return ResponseEntity.ok(recipe);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Creates a recipe", description = "Creates a recipe and adds it to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @PostMapping
    public ResponseEntity<Recipe> addRecipe(@RequestParam String username, @RequestBody Recipe recipe, @RequestHeader("Authorization") String encryptedToken) throws SQLException {
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)) {
            recipeService.addRecipe(recipe);
            restrictionsService.addRestrictions(recipe, recipe.getDietaryRestrictions());
            preferencesService.addPreferences(recipe, recipe.getDietaryPreferences());
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Updates a recipe by its id", description = "Updates an existing recipe (in the database) by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Recipe> updateRecipe(@RequestParam String username, @PathVariable Integer id, @RequestBody Recipe recipe, @RequestHeader("Authorization") String encryptedToken) throws SQLException {
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)) {
            restrictionsService.removeRestrictions(id);
            preferencesService.removePreferences(id);
            recipeService.updateRecipe(id, recipe);
            restrictionsService.addRestrictions(recipe, recipe.getDietaryRestrictions());
            preferencesService.addPreferences(recipe, recipe.getDietaryPreferences());
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Updates a recipe by its name", description = "Updates an existing recipe (in the database) by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @PutMapping("/updateName/{name}")
    public ResponseEntity<Recipe> updateRecipeByName(@RequestParam String username, @PathVariable String name, @RequestBody Recipe recipe, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)) {
            restrictionsService.removeRestrictions(name);
            preferencesService.removePreferences(name);
            recipeService.updateRecipeByName(name, recipe);
            restrictionsService.addRestrictions(recipe, recipe.getDietaryRestrictions());
            preferencesService.addPreferences(recipe, recipe.getDietaryPreferences());
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Deletes a recipe by its id", description = "Deletes an existing recipe (in the database) by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteRecipebyId(@RequestParam String username, @PathVariable Integer id, @RequestHeader("Authorization") String encryptedToken) throws SQLException {
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)) {
            restrictionsService.removeRestrictions(id);
            preferencesService.removePreferences(id);
            recipeService.deleteRecipeById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Deletes a recipe by its name", description = "Deletes an existing recipe (in the database) by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteRecipebyName(@RequestParam String username, @PathVariable String name, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)) {
            restrictionsService.removeRestrictions(name);
            preferencesService.removePreferences(name);
            recipeService.deleteRecipeByName(name);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
