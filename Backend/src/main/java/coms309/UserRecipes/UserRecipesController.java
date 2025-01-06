package coms309.UserRecipes;

import coms309.Preferences.DietaryPreferencesService;
import coms309.Preferences.DietaryRestrictionsService;
import coms309.Recipe.Recipe;
import coms309.Security.JWTService;
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
import java.util.List;

@OpenAPIDefinition(info = @Info(title = "Controller for User recipe and join table handling", version = "1.0"))
@RestController
@RequestMapping("api/user-recipes")
@Tag(name = "UserRecipes API", description = "Endpoints for managing user recipes")
public class UserRecipesController {

    @Autowired
    private UserRecipesService userRecipesService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private DietaryRestrictionsService restrictionsService;
    @Autowired
    private DietaryPreferencesService preferencesService;

    @Operation(summary = "Add recipe to user", description = "Adds an existing recipe (in the database) to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added recipe to user"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @PostMapping("/id/{recipeId}")
    public ResponseEntity<Void> addRecipeToUser(@RequestParam String username, @PathVariable Integer recipeId, @RequestHeader("Authorization") String encryptedToken) throws SQLException, SQLException {
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            userRecipesService.addRecipeToUser(username, recipeId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Adds a new recipe to a user", description = "Creates a new recipe and adds it to the users list of recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added recipe to user"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @PostMapping("/name")
    public ResponseEntity<Void> addRecipeToUser(@RequestParam String username, @RequestBody Recipe recipe, @RequestHeader("Authorization") String encryptedToken) throws SQLException, SQLException {
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            userRecipesService.addRecipeToUser(username, recipe);
            restrictionsService.addRestrictions(recipe, recipe.getDietaryRestrictions());
            preferencesService.addPreferences(recipe, recipe.getDietaryPreferences());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Retrieves all the user's recipes", description = "Retrieves all the recipes a particular user has added")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping
    public ResponseEntity<List<Recipe>> getUserRecipes(@RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException {
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            List<Recipe> recipes = userRecipesService.getRecipesByUser(username);
            for (Recipe r : recipes) {
                restrictionsService.fillRestrictions(r);
                preferencesService.fillPreferences(r);
            }
            return ResponseEntity.ok(recipes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Retrieves a user's recipe by recipe id", description = "Retrieves one recipe for a particular user by recipeId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<Recipe> getUserRecipeById(@RequestParam String username, @PathVariable Integer recipeId, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            Recipe recipe = userRecipesService.getUserRecipeByID(username, recipeId);
            if (recipe != null) {
                restrictionsService.fillRestrictions(recipe);
                preferencesService.fillPreferences(recipe);
            }
            return recipe != null ? ResponseEntity.ok(recipe) : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Retrieves user recipe by recipe name", description = "Retrieves one recipe for a particular user by the recipe name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @GetMapping("/recipe/name/{recipeName}")
    public ResponseEntity<Recipe> getUserRecipeByName(@RequestParam String username, @PathVariable String recipeName, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            Recipe recipe = userRecipesService.getUserRecipeByRecipeName(username, recipeName);
            if (recipe != null) {
                restrictionsService.fillRestrictions(recipe);
                preferencesService.fillPreferences(recipe);
            }
            return recipe != null ? ResponseEntity.ok(recipe) : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Update user recipe", description = "Updates a particular user's recipe by recipe Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @PutMapping("/recipe/{recipeId}")
    public ResponseEntity<Void> updateUserRecipe(@RequestParam String username, @PathVariable Integer recipeId, @RequestBody Recipe recipe, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            userRecipesService.updateUserRecipe(username, recipeId, recipe);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Operation(summary = "Delete user recipe", description = "Delete a user recipe by recipe Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted recipe"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User does not have the necessary permissions")
    })
    @DeleteMapping("/recipe/{recipeId}")
    public ResponseEntity<Void> deleteUserRecipe(@RequestParam String username, @PathVariable Integer recipeId, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            restrictionsService.removeRestrictions(recipeId);
            preferencesService.removePreferences(recipeId);
            userRecipesService.deleteUserRecipe(username, recipeId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
