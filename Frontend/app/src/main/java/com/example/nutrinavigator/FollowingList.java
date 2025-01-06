package com.example.nutrinavigator;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FollowingList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FollowingAdapter adapter;
    private List<User> followingList;
    private String username;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followinglist);

        recyclerView = findViewById(R.id.recyclerViewFollowing);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize your list and adapter
        followingList = new ArrayList<>();
        adapter = new FollowingAdapter(this, followingList, username, token);
        recyclerView.setAdapter(adapter);

        // Call the method to fetch the following list
        fetchFollowingList();
    }

    private void fetchFollowingList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://coms-3090-005.class.las.iastate.edu:8080/social/listfollowed?username=" + username;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
            response -> {
                try {
                    JSONArray data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject userJson = data.getJSONObject(i);
                        String followingUsername = userJson.getString("username");
                        followingList.add(new User(followingUsername));
                    }
                    // Notify adapter of data change
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },
            error -> error.printStackTrace()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(request);
    }
}
