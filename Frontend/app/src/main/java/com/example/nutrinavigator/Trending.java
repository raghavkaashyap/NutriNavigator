package com.example.nutrinavigator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;
import java.net.URISyntaxException;

public class Trending extends AppCompatActivity {

    private EditText etMessage;
    private Button btnSendMessage;
    private TextView tvReceivedMessages;
    private TrendingWSClient webSocketClientHandler;
    private String username = "test_user4";  // Replace with actual username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

        // UI elements
        etMessage = findViewById(R.id.et_message);
        btnSendMessage = findViewById(R.id.btn_send_message);
        tvReceivedMessages = findViewById(R.id.tv_received_messages);

        // Initialize WebSocket client with username and activity reference
        try {
            URI serverURI = new URI("ws://coms-3090-005.class.las.iastate.edu:8080/livestats/" + username);
            webSocketClientHandler = new TrendingWSClient(serverURI, username, this);
            webSocketClientHandler.connect();  // Connect to the WebSocket server
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid WebSocket URI", Toast.LENGTH_SHORT).show();
        }

        // Set up the send message button
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                if (!message.isEmpty()) {
                    webSocketClientHandler.sendMessage(message);  // Send the message to the server
                    etMessage.setText("");  // Clear the input field
                } else {
                    Toast.makeText(Trending.this, "Enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to update the UI with the received message
    public void displayReceivedMessage(String message) {
        // Update the TextView with the new message
        String currentText = tvReceivedMessages.getText().toString();
        String updatedText = currentText + "\n" + message;  // Append the new message
        tvReceivedMessages.setText(updatedText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketClientHandler != null) {
            webSocketClientHandler.closeConnection();  // Close the WebSocket when the activity is destroyed
        }
    }
}
