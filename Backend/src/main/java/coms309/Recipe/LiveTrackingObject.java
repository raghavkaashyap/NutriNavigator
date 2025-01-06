package coms309.Recipe;

import java.util.Date;

public class LiveTrackingObject {
    private int recipeId;
    private int pulls;
    private Date dateLogged;

    public LiveTrackingObject() {};

    public LiveTrackingObject(int pulls, Date dateLogged) {
        this.pulls = pulls;
        this.dateLogged = dateLogged;
    }

    public LiveTrackingObject(int recipeId, int pulls, Date dateLogged) {
        this.recipeId = recipeId;
        this.pulls = pulls;
        this.dateLogged = dateLogged;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void incrementPulls(int increment) {
        pulls += increment;
    }

    public void setDateLogged(Date dateLogged) {
        this.dateLogged = dateLogged;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getPulls() {
        return pulls;
    }

    public Date getDateLogged() {
        return dateLogged;
    }
}
