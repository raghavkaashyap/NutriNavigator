package com.example.nutrinavigator;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;

public class AddPlan extends AppCompatActivity {
    private DatePicker datePicker;
    private AutoCompleteTextView autoCompleteRecipe;
    private Button btnSavePlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplan);

        datePicker = findViewById(R.id.datePicker);
        autoCompleteRecipe = findViewById(R.id.autoCompleteRecipe);
        btnSavePlan = findViewById(R.id.btnSavePlan);

        requestQueue = Volley.newRequestQueue(this);

        btnSavePlan.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Month is 0-based
            int year = datePicker.getYear();
            String date = year + "-" + month + "-" + day;

            String recipeId = autoCompleteRecipe.getText().toString();
            if (!recipeId.isEmpty()) {
                addPlan(date, recipeId);
            } else {
                Toast.makeText(this, "Please select a recipe!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPlan(String futureDate, String recipeId) {
        String url = "http://coms-3090-005.class.las.iastate.edu:8080/addToPlan";

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("futureDate", futureDate);
            requestBody.put("recipeId", recipeId);
            requestBody.put("userId", 1); // Replace with dynamic user ID

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                    response -> {
                        Toast.makeText(this, "Plan added successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error -> {
                        Toast.makeText(this, "Failed to add plan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            );

            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "An error occurred while creating the plan.", Toast.LENGTH_SHORT).show();
        }
    }
}
