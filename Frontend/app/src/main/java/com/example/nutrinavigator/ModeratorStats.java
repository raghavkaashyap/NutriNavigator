package com.example.nutrinavigator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class ModeratorStats extends AppCompatActivity {

    private TextView regularUserCountTextView, recipesPerUserTextView;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modstats);

        // Initialize Views
        regularUserCountTextView = findViewById(R.id.userCountTextView);
        recipesPerUserTextView = findViewById(R.id.recipesPerUserTextView);
        refreshButton = findViewById(R.id.refreshButton);

        // Refresh button click listener
        refreshButton.setOnClickListener(v -> fetchModeratorStatistics());

        // Fetch data when activity is created
        fetchModeratorStatistics();
    }

    private void fetchModeratorStatistics() {
        // Fetch regular user count stats
        fetchRegularUserCount();

        // Fetch recipes per user stats
        fetchRecipesPerUser();
    }

    private void fetchRegularUserCount() {
        String url = "\n" +
                "http://coms-3090-005.class.las.iastate.edu:8080/statistics/regular-users";  // Replace with correct URL for regular user stats

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest statsRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Parse the response and update the UI
                        regularUserCountTextView.setText(response.getString("regularUserCount"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ModeratorStats.this, "Error parsing regular user count", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    error.printStackTrace();
                    Toast.makeText(ModeratorStats.this, "Error fetching regular user stats", Toast.LENGTH_SHORT).show();
                });

        queue.add(statsRequest);
    }

    private void fetchRecipesPerUser() {
        String url = "\n" +
                "http://coms-3090-005.class.las.iastate.edu:8080/statistics/recipes-per-user";  // Replace with correct URL for recipes per user stats

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest statsRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Parse the response and update the UI
                        recipesPerUserTextView.setText(response.getString("recipesPerUser"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ModeratorStats.this, "Error parsing recipes per user", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    error.printStackTrace();
                    Toast.makeText(ModeratorStats.this, "Error fetching recipes per user stats", Toast.LENGTH_SHORT).show();
                });

        queue.add(statsRequest);
    }
}


