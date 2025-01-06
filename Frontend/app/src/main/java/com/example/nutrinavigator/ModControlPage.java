package com.example.nutrinavigator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModControlPage extends AppCompatActivity {
    private Button btnViewListing, btnHideListing, btnPromoteUser, btnBanUser, btnUnbanUser, btnViewAllUsers, btnRecipesPendingRevision;
    private TextView textResponse;
    private RequestQueue requestQueue;
    private static final String BASE_URL = "http://coms-3090-005.class.las.iastate.edu:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modcontrol);

        // Initialize UI elements
        btnViewListing = findViewById(R.id.btnViewListing);
        btnHideListing = findViewById(R.id.btnHideListing);
        btnPromoteUser = findViewById(R.id.btnPromoteUser);
        btnBanUser = findViewById(R.id.btnBanUser);
        btnUnbanUser = findViewById(R.id.btnUnbanUser);
        btnViewAllUsers = findViewById(R.id.btnViewAllUsers);
        btnRecipesPendingRevision = findViewById(R.id.btnRecipesPendingRevision);
        textResponse = findViewById(R.id.textResponse);

        // Initialize Volley RequestQueue using Singleton
        requestQueue = Volley.newRequestQueue(this);

        // Set click listeners
        btnViewListing.setOnClickListener(v -> performAction("/view-listing"));
        btnHideListing.setOnClickListener(v -> performAction("/hide-listing"));
        btnPromoteUser.setOnClickListener(v -> performAction("/admin/promote"));
        btnBanUser.setOnClickListener(v -> performAction("/moderator/banuser"));
        btnUnbanUser.setOnClickListener(v -> performAction("/moderator/unbanuser"));
        btnViewAllUsers.setOnClickListener(v -> performAction("/view-all-users"));
        btnRecipesPendingRevision.setOnClickListener(v -> performAction("/recipes-pending-revision"));
    }

    private void performAction(String endpoint) {
        String url = BASE_URL + endpoint;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    // Parse the response to display a user-friendly message
                    try {
                        if (response.has("message")) {
                            String message = response.getString("message");
                            textResponse.setText("Success: " + message);
                        } else {
                            textResponse.setText("Action completed successfully.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        textResponse.setText("Unexpected response format.");
                    }
                },
                error -> {
                    // Handle error and provide user feedback
                    String errorMessage = "Error: ";
                    if (error.networkResponse != null) {
                        errorMessage += " Status code: " + error.networkResponse.statusCode;
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            errorMessage += " Response: " + responseBody;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        errorMessage += error.getMessage();
                    }
                    textResponse.setText(errorMessage);
                });


        requestQueue.add(jsonObjectRequest);
    }
}

