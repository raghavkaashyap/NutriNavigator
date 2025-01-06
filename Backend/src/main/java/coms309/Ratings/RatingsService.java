package coms309.Ratings;

import coms309.Recipe.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RatingsService {

    @Autowired
    RatingsRepo ratingsRepo;

    public void addRating(Ratings rating) throws SQLException{
        ratingsRepo.addRating(rating);
    }

    public void addRating(Ratings rating, String recipeName) throws SQLException{
        ratingsRepo.addRating(rating, recipeName);
    }

    public List<Ratings> getRatingsByRecipe(int recipeId) throws SQLException{
        return ratingsRepo.getRatingsByRecipe(recipeId);
    }

    public Ratings getUserRatingByRecipeName(String username, String recipeName) throws SQLException {
        return ratingsRepo.getUserRatingByRecipeName(username, recipeName);
    }

    public List<Ratings> getRatingsByRecipeName(String recipeName) throws SQLException{
        return ratingsRepo.getRatingsByRecipeName(recipeName);
    }

    public List<Recipe> getRecipesRatedAbove(double threshold) throws SQLException{
        return ratingsRepo.getRecipesRatedAbove(threshold);
    }

    public double getAverageRating(int recipeId) throws SQLException{
        return ratingsRepo.getAverageRating(recipeId);
    }

    public double getAverageRatingByRecipeName(String recipeName) throws SQLException{
        return ratingsRepo.getAverageRatingByRecipeName(recipeName);
    }

    public boolean hasUserRated(int recipeId, String username) throws SQLException{
        return ratingsRepo.hasUserRated(recipeId, username);
    }

    public void updateRating(String username, int recipeId, int newRating) throws SQLException{
        ratingsRepo.updateRating(username, recipeId, newRating);
    }

    public void updateRatingByRecipeName(String username, String recipeName, int newRating) throws SQLException{
        ratingsRepo.updateRatingByRecipeName(username, recipeName, newRating);
    }

    public void deleteRating(String username, int recipeId) throws SQLException{
        ratingsRepo.deleteRating(username, recipeId);
    }

    public void deleteRatingByRecipeName(String username, String recipeName) throws SQLException{
        ratingsRepo.deleteRatingByRecipeName(username, recipeName);
    }

    public void deleteRatingsByUser(String username) throws SQLException{
        ratingsRepo.deleteRatingsByUser(username);
    }

    public void deleteRatingsByRecipe(int recipeId) throws SQLException{
        ratingsRepo.deleteRatingsByRecipe(recipeId);
    }
}
