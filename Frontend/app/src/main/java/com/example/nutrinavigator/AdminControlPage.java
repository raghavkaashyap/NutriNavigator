package com.example.nutrinavigator;



import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;


public class AdminControlPage extends AppCompatActivity {

    private RequestQueue requestQueue;
    private static final String BASE_URL = "http://coms-3090-005.class.las.iastate.edu:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincontrol);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Initialize UI elements
        Button btnPromoteUser = findViewById(R.id.btnPromoteUser);
        Button btnDemoteModerator = findViewById(R.id.btnDemoteModerator);
        Button btnDeleteUser = findViewById(R.id.btnDeleteUser);
        Button btnBanUser = findViewById(R.id.btnBanUser);
        Button btnUnbanUser = findViewById(R.id.btnUnbanUser);
        Button btnGetUser = findViewById(R.id.btnGetUser);
        Button btnBack = findViewById(R.id.btnBack);

        // Set click listeners
        btnPromoteUser.setOnClickListener(v -> promoteUser());
        btnDemoteModerator.setOnClickListener(v -> demoteModerator());
        btnDeleteUser.setOnClickListener(v -> deleteUser());
        btnBanUser.setOnClickListener(v -> banUser());
        btnUnbanUser.setOnClickListener(v -> unbanUser());
        btnGetUser.setOnClickListener(v -> getUser());
        btnBack.setOnClickListener(v -> finish());
    }

    private void promoteUser() {
        String url = BASE_URL + "/admin/promote";
        makeApiCall(url, "User promoted successfully", "Failed to promote user");
    }

    private void demoteModerator() {
        String url = BASE_URL + "/admin/demote";
        makeApiCall(url, "Moderator demoted successfully", "Failed to demote moderator");
    }

    private void deleteUser() {
        String url = BASE_URL + "/admin/deleteuser";
        makeApiCall(url, "User deleted successfully", "Failed to delete user");
    }

    private void banUser() {
        String url = BASE_URL + "/admin/banuser";
        makeApiCall(url, "User banned successfully", "Failed to ban user");
    }

    private void unbanUser() {
        String url = BASE_URL + "/admin/unbanuser";
        makeApiCall(url, "User unbanned successfully", "Failed to unban user");
    }

    private void getUser() {
        String url = BASE_URL + "/admin/getuser";
        makeApiCall(url, "User retrieved successfully", "Failed to retrieve user");
    }

    private void makeApiCall(String url, String successMessage, String errorMessage) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> Toast.makeText(AdminControlPage.this, successMessage, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(AdminControlPage.this, errorMessage + ": " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}