package coms309.Ratings;

import coms309.Recipe.Recipe;
import coms309.Security.JWTService;
import coms309.Users.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingsController {

    @Autowired
    RatingsService ratingsService;
    @Autowired
    JWTService jwtService;

    @PostMapping
    public ResponseEntity<String> addRating(@RequestParam String username, @RequestBody Ratings rating, @RequestHeader("Authorization") String encryptedToken) throws SQLException {
        if (!jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to perform this action.");
        }
        try{
            ratingsService.addRating(rating);
            return ResponseEntity.ok("Rating added successfully");
        } catch (SQLException e){
            return ResponseEntity.status(500).body("Error adding rating: " + e.getMessage());
        }
    }

    @PostMapping("/recipeName")
    public ResponseEntity<String> addRatingByName(@RequestParam String username, @RequestBody Ratings rating, @RequestParam String recipeName, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (!jwtService.checkUserIsType(username, UserType.USER, encryptedToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to perform this action.");
        }
        try{
            ratingsService.addRating(rating, recipeName);
            return ResponseEntity.ok("Rating added successfully");
        } catch (SQLException e){
            return ResponseEntity.status(500).body("Error adding rating: " + e.getMessage());
        }
    }

    @GetMapping("/all-ratings")
    public ResponseEntity<List<Ratings>> getRatingsByRecipe(@RequestParam int recipeId) throws SQLException{
        try{
            List<Ratings> ratings = ratingsService.getRatingsByRecipe(recipeId);
            return ResponseEntity.ok(ratings);
        } catch (SQLException e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user/recipeName")
    public ResponseEntity<Ratings> getUserRatingByRecipeName(@RequestParam String username, @RequestParam String recipeName) throws SQLException {
        try{
            Ratings rating = ratingsService.getUserRatingByRecipeName(username, recipeName);
            return ResponseEntity.ok(rating);
        } catch (SQLException e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/recipeName")
    public ResponseEntity<List<Ratings>> getRatingsByRecipeName(@RequestParam String recipeName) throws SQLException{
        try{
            List<Ratings> ratings = ratingsService.getRatingsByRecipeName(recipeName);
            return ResponseEntity.ok(ratings);
        } catch (SQLException e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/threshold")
    public ResponseEntity<List<Recipe>> getRecipesRatedAbove(@RequestParam double threshold) throws SQLException{
        try{
            List<Recipe> recipes = ratingsService.getRecipesRatedAbove(threshold);
            return ResponseEntity.ok(recipes);
        } catch (SQLException e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/id/averageRating")
    public ResponseEntity<Double> getAverageRating(@RequestParam int recipeId) throws SQLException{
        try{
            Double average = ratingsService.getAverageRating(recipeId);
            return ResponseEntity.ok(average);
        } catch (SQLException e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/averageRating")
    public ResponseEntity<Double> getAverageRatingByRecipeName(@RequestParam String recipeName) throws SQLException{
        try{
            Double average = ratingsService.getAverageRatingByRecipeName(recipeName);
            return ResponseEntity.ok(average);
        } catch (SQLException e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/recipeId")
    public ResponseEntity<String> updateRating(@RequestParam String username, @RequestParam int recipeId, @RequestParam int newRating) throws SQLException{
        try{
            ratingsService.updateRating(username, recipeId, newRating);
            return ResponseEntity.ok("Successfully updated rating.");
        } catch (SQLException e){
            return ResponseEntity.status(500).body("Error updating rating: " + e.getMessage());
        }
    }

    @PutMapping("/recipeName")
    public ResponseEntity<String> updateRatingByRecipeName(@RequestParam String username, @RequestParam String recipeName, @RequestParam int newRating) throws SQLException{
        try{
            ratingsService.updateRatingByRecipeName(username, recipeName, newRating);
            return ResponseEntity.ok("Successfully updated rating.");
        } catch (SQLException e){
            return ResponseEntity.status(500).body("Error updating rating: " + e.getMessage());
        }
    }

    @DeleteMapping("/recipeId")
    public ResponseEntity<String> deleteRating(@RequestParam String username, @RequestParam int recipeId) throws SQLException{
        try{
            ratingsService.deleteRating(username, recipeId);
            return ResponseEntity.ok("Successfully deleted rating.");
        } catch (SQLException e){
            return ResponseEntity.status(500).body("Error deleting rating: " + e.getMessage());
        }
    }

    @DeleteMapping("/recipeName")
    public ResponseEntity<String> deleteRatingByRecipeName(@RequestParam String username, @RequestParam String recipeName) throws SQLException{
        try{
            ratingsService.deleteRatingByRecipeName(username, recipeName);
            return ResponseEntity.ok("Successfully deleted rating.");
        } catch (SQLException e){
            return ResponseEntity.status(500).body("Error deleting rating: " + e.getMessage());
        }
    }

    @DeleteMapping("/username")
    public ResponseEntity<String> deleteRatingsByUser(@RequestParam String username) throws SQLException{
        try{
            ratingsService.deleteRatingsByUser(username);
            return ResponseEntity.ok("Successfully deleted ratings.");
        } catch (SQLException e){
            return ResponseEntity.status(500).body("Error deleting ratings: " + e.getMessage());
        }
    }

    @DeleteMapping("/recipe")
    public ResponseEntity<String> deleteRatingsByRecipe(@RequestParam int recipeId, @RequestParam String username, @RequestHeader("Authorization") String encryptedToken) throws SQLException{
        if (!jwtService.checkUserIsType(username, UserType.MODERATOR, encryptedToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to perform this action.");
        }
        try{
            ratingsService.deleteRatingsByRecipe(recipeId);
            return ResponseEntity.ok("Successfully deleted ratings.");
        } catch (SQLException e){
            return ResponseEntity.status(500).body("Error deleting ratings: " + e.getMessage());
        }
    }
}