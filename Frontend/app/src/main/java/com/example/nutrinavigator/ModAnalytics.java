package com.example.nutrinavigator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ModAnalytics extends AppCompatActivity {

    private TextView tvUserCount;
    private TextView tvRecipeCount;
    private TextView tvRecipesUnderRevision;
    private TextView tvAverageRecipesPerUser;
    private TextView tvBannedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modanalytics);

        // Initialize views
        tvUserCount = findViewById(R.id.tvUserCount);
        tvRecipeCount = findViewById(R.id.tvRecipeCount);
        tvRecipesUnderRevision = findViewById(R.id.tvRecipesUnderRevision);
        tvAverageRecipesPerUser = findViewById(R.id.tvAverageRecipesPerUser);
        tvBannedUsers = findViewById(R.id.tvBannedUsers);

        // Load and display analytics data
        loadAnalyticsData();
    }

    private void loadAnalyticsData() {

        int userCount = 1000;
        int recipeCount = 5000;
        int recipesUnderRevision = 50;
        double averageRecipesPerUser = 5.0;
        int bannedUsers = 10;

        // Display the data
        tvUserCount.setText(String.valueOf(userCount));
        tvRecipeCount.setText(String.valueOf(recipeCount));
        tvRecipesUnderRevision.setText(String.valueOf(recipesUnderRevision));
        tvAverageRecipesPerUser.setText(String.format("%.2f", averageRecipesPerUser));
        tvBannedUsers.setText(String.valueOf(bannedUsers));
    }
}
