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

/**
 * The EditMessage class provides functionality for editing and updating messages
 * in the application. It communicates with a remote server to submit changes to a message.
 *
 * @author Colin Doranski
 */
public class EditMessage extends AppCompatActivity {
    private EditText messageEdit, idEdit;
    private RequestQueue requestQueue;
    private static final String MESSAGE_URL = "http://coms-3090-005.class.las.iastate.edu:8080/messages";

    /**
     * Initializes the activity, sets up UI components, and defines the edit button behavior.
     *
     * @param savedInstanceState Bundle has the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmessage);

        requestQueue = Volley.newRequestQueue(this);

        idEdit = findViewById(R.id.editMessageID);
        messageEdit = findViewById(R.id.editMessage);
        Button editButton = findViewById(R.id.sendEditButton);

        editButton.setOnClickListener(v -> edit());
    }

    /**
     * Collects the user input and builds a JSON object,
     * and sends an update request to the server.
     */
    private void edit() {
//Gets input values from the EditText fields
        String message = messageEdit.getText().toString();
        String messageId = idEdit.getText().toString();

        //Create a JSON object with the updated message data
        JSONObject messageData = new JSONObject();
        try {
            messageData.put("messageId", messageId); // Add the message ID
            messageData.put("message", message); // Add the new message text
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = MESSAGE_URL + "/update";

        // Create a JSON object request to send the update
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, messageData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle a successful server response
                        try {
                            String message = response.getString("message");
                            Toast.makeText(EditMessage.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditMessage.this, "Response parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle server or network errors
                        Toast.makeText(EditMessage.this, "Error updating message", Toast.LENGTH_SHORT).show();
                        Log.e("EditMessage", "Error: " + error.getMessage());
                    }
                }
        );

        //Add the request to the Volley RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
