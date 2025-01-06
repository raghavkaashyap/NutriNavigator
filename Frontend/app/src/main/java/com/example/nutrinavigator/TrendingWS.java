package com.example.nutrinavigator;

import org.java_websocket.handshake.ServerHandshake;

public interface TrendingWS {
    void onWebSocketOpen(ServerHandshake data);
    void onWebSocketMessage(String message);
    void onWebSocketClose(int code, String s, boolean b);
    void onWebSocketError(Exception e);
}
