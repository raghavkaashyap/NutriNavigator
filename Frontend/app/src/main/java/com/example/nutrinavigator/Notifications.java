package com.example.nutrinavigator;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.Response;
import okio.ByteString;

/**
 * Notifications class to handle and display WebSocket-based notifications.
 * Establishes a WebSocket connection to receive real-time updates.
 *
 * @author Aidan McGinnis
 */
public class Notifications extends AppCompatActivity {
    private TextView messageTextView, timestampTextView;
    private OkHttpClient client;
    private WebSocket webSocket;

    /**
     * Called when the activity is first created. Initializes views and starts WebSocket connection.
     *
     * @param savedInstanceState A bundle containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Initialize Views
        messageTextView = findViewById(R.id.messageTextView);
        timestampTextView = findViewById(R.id.timestampTextView);

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Start the WebSocket connection
        startWebSocketConnection();
    }

    /**
     * Starts the WebSocket connection to receive notifications.
     */
    private void startWebSocketConnection() {
        String username = "test_user";
        String url = "ws://coms-3090-005.class.las.iastate.edu:8080/notifications/" + username;  // WebSocket URL

        Request request = new Request.Builder().url(url).build();
        webSocket = client.newWebSocket(request, new NotificationWebSocketListener());
    }

    /**
     * Inner class to handle WebSocket events, such as connection open, messages received, and failures.
     */
    private class NotificationWebSocketListener extends WebSocketListener {
        /**
         * Called when the WebSocket connection is successfully opened.
         *
         * @param webSocket The WebSocket instance.
         * @param response  The server's response.
         */
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            runOnUiThread(() -> Toast.makeText(Notifications.this, "Connected to notifications", Toast.LENGTH_SHORT).show());
        }

        /**
         * Called when a text message is received from the WebSocket.
         *
         * @param webSocket The WebSocket instance.
         * @param text      The received message as a string.
         */
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            runOnUiThread(() -> {
                messageTextView.setText(text);  // Display the message
                timestampTextView.setText("Timestamp: " + System.currentTimeMillis());  // Set timestamp
            });
        }

        /**
         * Called when a binary message is received from the WebSocket.
         *
         * @param webSocket The WebSocket instance.
         * @param bytes     The received message as binary data.
         */
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            // Handle binary messages (if any)
        }

        /**
         * Called when the WebSocket connection is closed.
         *
         * @param webSocket The WebSocket instance.
         * @param code      The closure code.
         * @param reason    The reason for closure.
         */
        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            runOnUiThread(() -> Toast.makeText(Notifications.this, "Disconnected", Toast.LENGTH_SHORT).show());
        }

        /**
         * Called when the WebSocket connection fails.
         *
         * @param webSocket The WebSocket instance.
         * @param t         The cause of the failure.
         * @param response  The server's response, if available.
         */
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            runOnUiThread(() -> Toast.makeText(Notifications.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Called when the activity is destroyed. Closes the WebSocket connection to release resources.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "Goodbye!");  // Close WebSocket connection when activity is destroyed
        }
    }
}


