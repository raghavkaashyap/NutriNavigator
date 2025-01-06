package com.example.nutrinavigator;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NotificationResponse extends AppCompatActivity {
    @SerializedName("notifications")
    private List<String> notifications;

    public List<String> getNotifications() {
        return notifications;
    }

    public String getNotificationsSummary() {
        return String.join("\n", notifications);
    }
}
