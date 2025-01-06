package com.example.nutrinavigator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    private EditText loginUsername, loginPassword;
    private RequestQueue requestQueue;
    private static final String LOGIN_URL = "http://coms-3090-005.class.las.iastate.edu:8080/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

        loginUsername = findViewById(R.id.editUsername);
        loginPassword = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> userLogin());
    }

    private void userLogin() {
        String username = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();

        JSONObject loginData = new JSONObject();
        try {
            loginData.put("username", username);
            loginData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, loginData,
                response -> {
                    // Handle successful response
                    Toast.makeText(LoginPage.this, "login successful!", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    String errorMessage = "login failed: ";
                    if (error.networkResponse != null) {
                        errorMessage += " Status code: " + error.networkResponse.statusCode;
                        try {
                            String responseBody = new String(error.networkResponse.data, "UTF-8");
                            errorMessage += " Response: " + responseBody;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        errorMessage += error.getMessage();
                    }
                    Toast.makeText(LoginPage.this, errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("login Error", errorMessage);
                }) {
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
