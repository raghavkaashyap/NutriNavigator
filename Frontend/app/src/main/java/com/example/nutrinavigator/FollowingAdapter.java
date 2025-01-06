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
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {
    private List<User> followingList;
    private String username;
    private String token;
    private Context context;

    public FollowingAdapter(Context context, List<User> followingList, String username, String token) {
        this.context = context;
        this.followingList = followingList;
        this.username = username;
        this.token = token;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User followingUser = followingList.get(position);
        holder.followerUsername.setText(followingUser.getUsername());

        holder.unfollowButton.setOnClickListener(v -> {
            String url = "https://your-api-url.com/social/unfollow?username=" + username + "&usertounfollow=" + followingUser.getUsername();

            StringRequest unfollowRequest = new StringRequest(Request.Method.DELETE, url,
                    response -> {
                        followingList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, followingList.size());
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
        return followingList.size();
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

