package com.example.nutrinavigator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class FollowersList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FollowersAdapter adapter;
    private ArrayList<User> followersList;
    private Button backButton;
    private String username;
    private static final String BASE_URL = "http://coms-3090-005.class.las.iastate.edu:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followerslist);

        recyclerView = findViewById(R.id.recyclerViewFollowers);
        backButton = findViewById(R.id.backButton);
        followersList = new ArrayList<>();
        adapter = new FollowersAdapter(this, followersList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        username = "test_user2";

        fetchFollowersList();

        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void fetchFollowersList() {
        String url = BASE_URL + "/social/listfollowers?username=" + username;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray followersArray = response.getJSONArray("data");  // Assuming response key is "data"
                        followersList.clear();
                        for (int i = 0; i < followersArray.length(); i++) {
                            JSONObject userObject = followersArray.getJSONObject(i);
                            int id = userObject.getInt("id");
                            String username = userObject.getString("username");
                            String userType = userObject.getString("userType");

                            followersList.add(new User(id, username, userType));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(FollowersList.this, "Error fetching followers list", Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FollowersList.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        requestQueue.add(jsonObjectRequest);
    }

    // Unfollow functionality
    public void unfollowUser(int userIdToUnfollow) {
        String url = BASE_URL + "/social/unfollow?username=" + username + "&usertounfollow=" + userIdToUnfollow;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(FollowersList.this, "Successfully unfollowed", Toast.LENGTH_SHORT).show();
                    fetchFollowersList();  // Refresh the followers list
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FollowersList.this, "Error unfollowing user", Toast.LENGTH_SHORT).show();
                }
            });

        requestQueue.add(jsonObjectRequest);
    }
}
