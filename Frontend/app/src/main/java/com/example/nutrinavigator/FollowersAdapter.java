package com.example.nutrinavigator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {
    private List<User> followersList;
    private String username;
    private String token;
    private Context context;

    public FollowersAdapter(Context context, List<User> followersList, String username, String token) {
        this.context = context;
        this.followersList = followersList;
        this.username = username;
        this.token = token;
    }

    public FollowersAdapter(FollowersList context, ArrayList<User> followersList) {
        this.context = context;
        this.followersList = followersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User follower = followersList.get(position);
        holder.followerUsername.setText(follower.getUsername());

        holder.unfollowButton.setOnClickListener(v -> {
            String url = "http://coms-3090-005.class.las.iastate.edu:8080/social/unfollow?username=" + username + "&usertounfollow=" + follower.getUsername();

            StringRequest unfollowRequest = new StringRequest(Request.Method.DELETE, url,
                    response -> {
                        followersList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, followersList.size());
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

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(unfollowRequest);
        });
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView followerUsername;
        Button unfollowButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            followerUsername = itemView.findViewById(R.id.followerUsername);
            unfollowButton = itemView.findViewById(R.id.unfollowButton);
        }
    }
}

