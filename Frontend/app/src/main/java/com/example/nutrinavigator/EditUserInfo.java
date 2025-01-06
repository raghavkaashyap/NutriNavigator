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

public class EditUserInfo extends AppCompatActivity {
    private EditText editUsername, editEmail;
    private RequestQueue requestQueue;
    private static final String EDIT_USER_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituserinfo);

        requestQueue = Volley.newRequestQueue(this);

        editUsername = findViewById(R.id.editUser);
        editEmail = findViewById(R.id.editEmail);
        Button submitButton = findViewById(R.id.changeButton);

        submitButton.setOnClickListener(v -> userEdit());
    }

    private void userEdit() {
        String username = editUsername.getText().toString();
        String email = editEmail.getText().toString();

        JSONObject editData = new JSONObject();
        try {
            editData.put("username", username);
            editData.put("newemail", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EDIT_USER_URL, editData,
                response -> {
                    // Handle successful response
                    //Intent intent = new Intent(this, HomePage);
                    //startActivity(intent);
                    Toast.makeText(EditUserInfo.this, "edit successful!", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    String errorMessage = "edit failed: ";
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
                    Toast.makeText(EditUserInfo.this, errorMessage, Toast.LENGTH_LONG).show();
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
