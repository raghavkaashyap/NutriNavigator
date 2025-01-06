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

public class UserMessages extends AppCompatActivity {
    private EditText messageEdit, usernameEdit, subjectEdit;
    private RequestQueue requestQueue;
    private static final String MESSAGE_URL = "http://coms-3090-005.class.las.iastate.edu:8080/messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermessages);

        requestQueue = Volley.newRequestQueue(this);

        usernameEdit = findViewById(R.id.usernameText);
        subjectEdit = findViewById(R.id.subjectText);
        messageEdit = findViewById(R.id.messageText);
        Button sendButton = findViewById(R.id.sendButton);
        Button editButton = findViewById(R.id.editButton);

        sendButton.setOnClickListener(v -> send());
        editButton.setOnClickListener(v -> edit());
    }

    private void send(){
        String username = usernameEdit.getText().toString();
        String message = messageEdit.getText().toString();
        String subject = subjectEdit.getText().toString();

        JSONObject messageData = new JSONObject();
        try {
            messageData.put("username", username);
            messageData.put("subject", subject);
            messageData.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = MESSAGE_URL + "/submit";

            /*JsonObjectRequest messageRequest = new JsonObjectRequest(Request.Method.POST, url, messageData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //try{

                    Toast.makeText(UserMessages.this, "message sent", Toast.LENGTH_SHORT).show();
               // }
                //catch (JSONException error){
                 //   Toast.makeText(UserMessages.this, "Server not responding. Try again shortly", Toast.LENGTH_LONG).show();
                //}
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error);
            }
        });
        requestQueue.add(messageRequest); */

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, messageData,
                response -> {
                    // Handle successful response
                    //Intent intent = new Intent(this, HomePage);
                    //startActivity(intent);
                    Toast.makeText(UserMessages.this, "message sent!", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    String errorMessage = "message failed: ";
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
                    Toast.makeText(UserMessages.this, errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("message Error", errorMessage);
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

    private void edit(){
        String username = usernameEdit.getText().toString();
        String message = messageEdit.getText().toString();
        String subject = subjectEdit.getText().toString();

        JSONObject updateData = new JSONObject();
        try {
            updateData.put("username", username);
            updateData.put("subject", subject);
            updateData.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = MESSAGE_URL + "/update";

     /*   JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.POST, url, updateData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //try{

                Toast.makeText(UserMessages.this, "update sent", Toast.LENGTH_SHORT).show();
                // }
                //catch (JSONException error){
                //   Toast.makeText(UserMessages.this, "Server not responding. Try again shortly", Toast.LENGTH_LONG).show();
                //}
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error);
            }
        });
        requestQueue.add(updateRequest); */

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, updateData,
                response -> {
                    // Handle successful response
                    //Intent intent = new Intent(this, HomePage);
                    //startActivity(intent);
                    Toast.makeText(UserMessages.this, "update sent!", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    String errorMessage = "update failed: ";
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
                    Toast.makeText(UserMessages.this, errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("update Error", errorMessage);
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
