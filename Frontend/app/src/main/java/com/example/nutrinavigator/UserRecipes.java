package com.example.nutrinavigator;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRecipes extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeFormat adapter;
    private List<Recipe> recipeList;
    private static final String API_URL = "http://coms-3090-005.class.las.iastate.edu:8080/api/user-recipes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userrecipe);

        recyclerView = findViewById(R.id.rv_user_recipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeList = new ArrayList<>();
        adapter = new RecipeFormat(recipeList);
        recyclerView.setAdapter(adapter);

        fetchRecipes();
    }

    private void fetchRecipes() {
        String username = "test_user4"; // Replace with dynamic username
        String token = "token"; // Replace with dynamic JWT

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL + "?username=" + username,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            recipeList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonRecipe = response.getJSONObject(i);
                                Recipe recipe = new Recipe();
                                recipe.setName(jsonRecipe.getString("name"));
                                recipeList.add(recipe);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(UserRecipes.this, "Error parsing recipes", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserRecipes.this, "Error fetching recipes", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}

