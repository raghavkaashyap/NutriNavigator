package com.example.nutrinavigator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The RecipeSearch class provides functionality for users to search for recipes
 * based on different things such as ingredients, calories, dates, and dietary preferences.
 * It communicates with a remote server to get and display search results.
 *
 * @author Colin Doranski
 */
public class RecipeSearch extends AppCompatActivity {
    private static final int SAVED_PREFERENCES= 1;
    private EditText edit_UploadUser, edit_Ingredients, edit_CaloriesHigh, edit_CaloriesLow, edit_DateStart, edit_DateEnd;
    private RecyclerView rView;
    private RequestQueue requestQueue;
    private String dietaryRestrictions, dietaryPreferences;

    /**
     * Initializes the activity, sets up UI components, and prepares event listeners.
     *
     * @param savedInstanceState Bundle has the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipesearch);

        //Initializes UI parts
        edit_UploadUser = findViewById(R.id.editUploadUser);
        edit_Ingredients = findViewById(R.id.editIngredients);
        edit_CaloriesHigh = findViewById(R.id.editCaloriesHigh);
        edit_CaloriesLow = findViewById(R.id.editCaloriesLow);
        edit_DateStart = findViewById(R.id.editDateStart);
        edit_DateEnd = findViewById(R.id.editDateEnd);
        rView = findViewById(R.id.rv);
        requestQueue = Volley.newRequestQueue(this);

        //Sets up button to open DietaryPreferences activity
        findViewById(R.id.btn_dietaryPreferences).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(RecipeSearch.this, DietaryPreferences.class);
                startActivityForResult(intent, SAVED_PREFERENCES);
            }
        });

        //Sets up button to search
        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Search();
            }
        });

        rView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Gets the result from the DietaryPreferences activity.
     * Updates the dietary restrictions and preferences based on user input.
     *
     * @param requestCode Request code originally given to startActivityForResult.
     * @param resultCode  Result code returned by the child activity.
     * @param data        Returned data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAVED_PREFERENCES && resultCode == RESULT_OK && data != null){
            dietaryRestrictions = data.getStringExtra("dietaryRestrictions");
            dietaryPreferences = data.getStringExtra("dietaryPreferences");
        }
    }

    /**
     * Constructs and sends a search query to the server based on user input.
     * Parses the JSON response and updates the view with the list of recipes.
     */
    private void Search() {
        //Gets input values from EditText fields
        String uploadUser = edit_UploadUser.getText().toString();
        String ingredients = edit_Ingredients.getText().toString();
        int caloriesLow = edit_CaloriesLow.getText().toString().isEmpty() ? -1 : Integer.parseInt(edit_CaloriesLow.getText().toString());
        int caloriesHigh = edit_CaloriesHigh.getText().toString().isEmpty() ? -1 :Integer.parseInt(edit_CaloriesHigh.getText().toString());
        String dateStart = edit_DateStart.getText().toString();
        String dateEnd = edit_DateEnd.getText().toString();


        StringBuilder urlBuilder = new StringBuilder("http://coms-3090-005.class.las.iastate.edu:8080/search?");

        //Append parameters only if they are not empty
        if (!uploadUser.isEmpty()) {
            urlBuilder.append("uploaduser=").append(uploadUser).append("&");
        }
        if (!ingredients.isEmpty()) {
            urlBuilder.append("ingredients=").append(ingredients).append("&");
        }
        if (caloriesLow != -1) {
            urlBuilder.append("calorieslow=").append(caloriesLow).append("&");
        }
        if (caloriesHigh != -1) {
            urlBuilder.append("calorieshigh=").append(caloriesHigh).append("&");
        }
        if (!dateStart.isEmpty()) {
            urlBuilder.append("datestart=").append(dateStart).append("&");
        }
        if (!dateEnd.isEmpty()) {
            urlBuilder.append("dateend=").append(dateEnd).append("&");
        }
        if (dietaryRestrictions != null && !dietaryRestrictions.isEmpty()) {
            urlBuilder.append("dietaryrestrictions=").append(dietaryRestrictions).append("&");
        }
        if (dietaryPreferences != null && !dietaryPreferences.isEmpty()) {
            urlBuilder.append("dietarypreferences=").append(dietaryPreferences);
        }

        //Remove last "&"
        if (urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        //Convert String for the final URL
        String url = urlBuilder.toString();

        //Sends the GET request using Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Search", "Response: " + response.toString());
                    String message = response.getString("errorMessage");
                    if (message.equals("Recipes successfully searched")) {
                        JSONArray dataArray = response.getJSONArray("payload");
                        List<Recipe> recipes = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject recipeObj = dataArray.getJSONObject(i);
                            Recipe recipe = new Recipe();
                            recipe.setName(recipeObj.getString("name"));
                            recipe.setIngredients(recipeObj.getString("ingredients"));
                            recipe.setCalories(recipeObj.getInt("calories"));
                            recipes.add(recipe);
                        }
                        rView.setAdapter(new RecipeFormat(recipes));
                    } else {
                        Toast.makeText(RecipeSearch.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RecipeSearch.this, "Failed to parse", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RecipeSearch.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

}
