package com.example.nutrinavigator;

public class Recipe {
    private String name;
    private String ingredients;
    private int calories;

    public Recipe(){}

    public Recipe(String name, String ingredients, int calories){
        this.name = name;
        this.ingredients = ingredients;
        this.calories = calories;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getIngredients(){
        return ingredients;
    }

    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }

    public int getCalories(){
        return calories;
    }

    public void setCalories(int calories){
        this.calories = calories;
    }


}
