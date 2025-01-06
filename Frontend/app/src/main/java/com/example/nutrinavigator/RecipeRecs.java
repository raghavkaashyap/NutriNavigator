package com.example.nutrinavigator;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecs extends AppCompatActivity {
    private RecyclerView recyclerViewRecommendations;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended);

        recyclerViewRecommendations = findViewById(R.id.recyclerViewRecommendations);
        recyclerViewRecommendations.setLayoutManager(new LinearLayoutManager(this));
        requestQueue = Volley.newRequestQueue(this);

        fetchRecommendedRecipes();
    }

    private void fetchRecommendedRecipes() {
        String url = "http://coms-3090-005.class.las.iastate.edu:8080/api/recipeRecommendations";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Recipe> recipes = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Recipe recipe = new Recipe(
                                    obj.getString("name"),
                                    obj.getString("ingredients"),
                                    (int) obj.getInt("calories")
                            );
                            recipes.add(recipe);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    RecipeRecsAdapter adapter = new RecipeRecsAdapter() {
                        @Override
                        public void onLike(int recipeId) {

                        }

                        @Override
                        public void onDislike(int recipeId) {

                        }
                    };
                    recyclerViewRecommendations.setAdapter(adapter);
                },
                error -> {
                    Toast.makeText(this, "Failed to fetch recommendations.", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }

    private void handleRecipeLikeDislike(int recipeId, boolean isLiked) {
        String action = isLiked ? "liked" : "disliked";
        Toast.makeText(this, "You " + action + " recipe with ID: " + recipeId, Toast.LENGTH_SHORT).show();
    }
}
