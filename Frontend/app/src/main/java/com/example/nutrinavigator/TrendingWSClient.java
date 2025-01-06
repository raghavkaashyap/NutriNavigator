package com.example.nutrinavigator;

import android.util.Log;

import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;

public class TrendingWSClient extends WebSocketClient {

    private static final String TAG = "WebSocket";
    private String username;
    private Trending activity;  // Reference to the activity to update the UI

    public TrendingWSClient(URI serverURI, String username, Trending activity) {
        super(serverURI, new Draft_6455());
        this.username = username;
        this.activity = activity;  // Pass the activity to update the UI
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d(TAG, "WebSocket opened: " + handshakedata);
        // You can send a message to the server here if necessary
    }

    @Override
    public void onMessage(String message) {
        Log.d(TAG, "Message received: " + message);

        // Update the UI with the incoming message
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Call the method in the activity to update the received messages
                activity.displayReceivedMessage(message);
            }
        });
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d(TAG, "WebSocket closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        Log.e(TAG, "WebSocket error: " + ex.getMessage());
    }

    // Send a message to the WebSocket server
    public void sendMessage(String message) {
        if (this.isOpen()) {
            this.send(message);
        } else {
            Log.w(TAG, "WebSocket is not open. Cannot send message.");
        }
    }

    // Close the WebSocket connection
    public void closeConnection() {
        this.close();
    }
}
