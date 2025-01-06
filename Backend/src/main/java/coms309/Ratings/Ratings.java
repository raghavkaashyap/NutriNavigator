package coms309.Ratings;

import java.time.LocalDate;

public class Ratings {

    private int id;
    private int recipeId;
    private String raterUsername;
    private int rating;
    private LocalDate date_rated;
    private String recipeName;

    public LocalDate getDate_rated() {
        return date_rated;
    }

    public void setDate_rated(LocalDate date_rated) {
        this.date_rated = date_rated;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRaterUsername() {
        return raterUsername;
    }

    public void setRaterUsername(String raterUsername) {
        this.raterUsername = raterUsername;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
}
