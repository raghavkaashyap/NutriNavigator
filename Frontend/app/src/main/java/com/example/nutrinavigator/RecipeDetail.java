package com.example.nutrinavigator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetail extends AppCompatActivity {

    private TextView recipeNameTextView, ingredientsTextView, caloriesTextView;
    private RatingBar ratingBar;
    private Button submitRatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipedetail);

        recipeNameTextView = findViewById(R.id.recipe_name);
        ingredientsTextView = findViewById(R.id.ingredients);
        caloriesTextView = findViewById(R.id.calories);
        ratingBar = findViewById(R.id.recipe_rating_bar);
        submitRatingButton = findViewById(R.id.submit_rating_button);

        Intent intent = getIntent();
        String recipeName = intent.getStringExtra("name");
        String ingredients = intent.getStringExtra("ingredients");
        String calories = intent.getStringExtra("calories");

        recipeNameTextView.setText(recipeName);
        ingredientsTextView.setText("ingredients: " + (ingredients.isEmpty() ? "None" : ingredients.toString()));
        caloriesTextView.setText("calories: " + (calories.isEmpty() ? "None" : calories.toString()));

        submitRatingButton.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();
            submitRating(recipeName, rating);
        });
    }

    private void submitRating(String recipeName, int rating) {
        String url = "http://coms-3090-005.class.las.iastate.edu:8080/api/ratings/recipeName?recipeName=" + recipeName;

        JSONObject ratingData = new JSONObject();
        try {
            ratingData.put("rating", rating);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                ratingData,
                response -> Toast.makeText(this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(this, "Failed to submit rating", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}

