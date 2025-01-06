package coms309.Plan;

import java.time.LocalDate;

public class Plan {
    private int userId;
    private int recipeId;
    private LocalDate date;

    public Plan() {}

    public Plan(int userId, int recipeId, LocalDate date) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.date = date;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public LocalDate getDate() {
        return date;
    }
}
