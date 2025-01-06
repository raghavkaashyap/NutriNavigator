package coms309.UserRecipes;

public class UserRecipes {

    private int recipeID;
    private String username;

    public UserRecipes(int recipeID, String username) {
        this.recipeID = recipeID;
        this.username = username;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
