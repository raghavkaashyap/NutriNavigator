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

/**
 * The UserStats class represents an activity in the application for displaying user statistics.
 * This activity shows various user metrics, including activity over different time frames,
 * follower and following counts, and average calories.
 *
 *  @author Aidan McGinnis
 */
public class UserStats extends AppCompatActivity {
    // TextViews to display user statistics
    private TextView yearToDateTextView, last6MonthsTextView, lastYearTextView;
    private TextView thisMonthTextView, thisWeekTextView, followerCountTextView;
    private TextView followingCountTextView, averageCaloriesTextView;

    // Buttons for navigation and refreshing statistics
    private Button backButton, refreshButton;

    /**
     * Called when the activity is created.
     * Initializes views, sets up button listeners, and fetches user statistics.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userstats);

        // Initialize Views
        yearToDateTextView = findViewById(R.id.yearToDateValue);
        last6MonthsTextView = findViewById(R.id.last6MonthsValue);
        lastYearTextView = findViewById(R.id.lastYearValue);
        thisMonthTextView = findViewById(R.id.thisMonthValue);
        thisWeekTextView = findViewById(R.id.thisWeekValue);
        followerCountTextView = findViewById(R.id.followerCountValue);
        followingCountTextView = findViewById(R.id.followingCountValue);
        averageCaloriesTextView = findViewById(R.id.averageCaloriesValue);

        backButton = findViewById(R.id.backButton);
        refreshButton = findViewById(R.id.refreshButton);

        // Refresh button click listener
        refreshButton.setOnClickListener(v -> fetchUserStatistics());

        // Back button listener
        backButton.setOnClickListener(v -> finish());

        // Fetch data when activity is created
        fetchUserStatistics();
    }

    /**
     * Fetches user statistics from the server and updates the corresponding TextViews.
     * Makes individual HTTP GET requests for each statistic.
     */
    private void fetchUserStatistics() {
        fetchStatistic("yearToDate", "http://coms-3090-005.class.las.iastate.edu:8080/statistics/year-to-date", yearToDateTextView);
        fetchStatistic("last6Months", "http://coms-3090-005.class.las.iastate.edu:8080/statistics/last-6-months", last6MonthsTextView);
        fetchStatistic("lastYear", "http://coms-3090-005.class.las.iastate.edu:8080/statistics/last-year", lastYearTextView);
        fetchStatistic("thisMonth", "http://coms-3090-005.class.las.iastate.edu:8080/statistics/this-month", thisMonthTextView);
        fetchStatistic("thisWeek", "http://coms-3090-005.class.las.iastate.edu:8080/statistics/this-week", thisWeekTextView);
        fetchStatistic("followerCount", "http://coms-3090-005.class.las.iastate.edu:8080/statistics/follower-count", followerCountTextView);
        fetchStatistic("followingCount", "http://coms-3090-005.class.las.iastate.edu:8080/statistics/following-count", followingCountTextView);
        fetchStatistic("averageCalories", "http://coms-3090-005.class.las.iastate.edu:8080/statistics/average-calories", averageCaloriesTextView);
    }

    /**
     * Sends an HTTP GET request to fetch a specific user statistic and updates the corresponding TextView.
     *
     * @param field    The key for the statistic in the server response.
     * @param url      The URL endpoint to fetch the statistic.
     * @param textView The TextView to update with the fetched statistic value.
     */
    private void fetchStatistic(String field, String url, TextView textView) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest statsRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Update the corresponding TextView with the fetched value
                        textView.setText(response.getString(field));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            // Handle error
            Toast.makeText(UserStats.this, "Error fetching " + field, Toast.LENGTH_SHORT).show();
        });

        queue.add(statsRequest);
    }
}

