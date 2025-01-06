package coms309.Recipe;

import org.springframework.stereotype.Component;

import java.time.LocalDate; //Temporary local date
@Component
public class  Recipe {

    private String name;

    private String ingredients;
    private int calories;
    private String dietaryRestrictions;
    private String dietaryPreferences;

    private LocalDate date; //update to use sqlDate method to get date from remote sql server

    public Recipe(){
        name = null;
        ingredients = null;
        calories = 0;
        date = LocalDate.now();
        dietaryPreferences = null;
        dietaryRestrictions = null;
    }

    public Recipe(String name, String ingredients, int calories, String dietaryRestrictions, String dietaryPreferences) {
        this.name = name;
        this.ingredients = ingredients;
        this.calories = calories;
        date = LocalDate.now();
        this.dietaryRestrictions = dietaryRestrictions;
        this.dietaryPreferences = dietaryPreferences;
    }


    public Recipe(String name, String ingredients, int calories){
        this.name = name;
        this.ingredients = ingredients;
        this.calories = calories;
        date = LocalDate.now();
    }

    public int getCalories(){
        return calories;
    }

    public String getIngredients(){
        return ingredients;
    }

    public String getName(){
        return name;
    }

    public LocalDate getDate(){
        return date;
    }

    public String getDietaryRestrictions() { return dietaryRestrictions; }

    public String getDietaryPreferences() { return dietaryPreferences; }

    public void setName(String name){
        this.name = name;
    }

    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }

    public void setCalories(int calories){
        this.calories = calories;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public void setDietaryRestrictions(String dietaryRestrictions) { this.dietaryRestrictions = dietaryRestrictions; }

    public void setDietaryPreferences(String dietaryPreferences) { this.dietaryPreferences = dietaryPreferences; }
}
