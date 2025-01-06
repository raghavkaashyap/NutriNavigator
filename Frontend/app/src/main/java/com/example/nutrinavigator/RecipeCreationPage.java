package com.example.nutrinavigator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RecipeCreationPage extends AppCompatActivity {

    private EditText recipeName, calories, ingredients, steps;
    private RequestQueue requestQueue;
    private static final String CREATE_RECIPE_URL = "http://coms-3090-005.class.las.iastate.edu:8080/api/recipes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipecreation);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Initialize UI elements
        recipeName = findViewById(R.id.etRecipeName);
        calories = findViewById(R.id.etCalories);
        ingredients = findViewById(R.id.etIngredients);
        steps = findViewById(R.id.etSteps);
        Button btnCreateRecipe = findViewById(R.id.btnCreateRecipe);

        // Set click listener for Create Recipe button
        btnCreateRecipe.setOnClickListener(v -> createRecipe());
    }

    private void createRecipe() {
        String name = recipeName.getText().toString().trim();
        String cal = calories.getText().toString().trim();
        String ingr = ingredients.getText().toString().trim();
        String stp = steps.getText().toString().trim();


        if (name.isEmpty() || cal.isEmpty() || ingr.isEmpty() || stp.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        int calorieValue;
        try {
            calorieValue = Integer.parseInt(cal);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Calories must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create JSON object with recipe data
        JSONObject recipeData = new JSONObject();
        try {
            recipeData.put("name", name);
            recipeData.put("calories", calorieValue);
            recipeData.put("ingredients", ingr);
            recipeData.put("steps", stp);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make API call to create recipe
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CREATE_RECIPE_URL, recipeData,
                response -> {
                    // Handle successful response
                    Toast.makeText(RecipeCreationPage.this, "Recipe created successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                },
                error -> {
                    // Handle error
                    Toast.makeText(RecipeCreationPage.this, "Failed to create recipe: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void clearFields() {
        recipeName.setText("");
        calories.setText("");
        ingredients.setText("");
        steps.setText("");
    }
}



