//package com.example.nutrinavigator;
//
//import android.content.Context;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.List;
//
//public class SocialAPI {
//
//    private static final String BASE_URL = "http://coms-3090-005.class.las.iastate.edu:8080/";
//
//    // Method to follow a user
//    public static void followUser(Context context, String username, String userToFollow, String token, VolleyCallback callback) {
//        String url = BASE_URL + "social/follow?username=" + username + "&usertofollow=" + userToFollow;
//
//        JsonObjectRequest followRequest = new JsonObjectRequest(Request.Method.POST, url, null,
//                response -> {
//                    // Handle success
//                    callback.onSuccess(response);
//                },
//                error -> {
//                    // Handle error
//                    error.printStackTrace();
//                    Toast.makeText(context, "Error following user", Toast.LENGTH_SHORT).show();
//                }) {
//            @Override
//            public void getHeaders() {
//                headers.put("Authorization", token);
//                return headers;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//        queue.add(followRequest);
//    }
//
//    // Method to unfollow a user
//    public static void unfollowUser(Context context, String username, String userToUnfollow, String token, VolleyCallback callback) {
//        String url = BASE_URL + "social/unfollow?username=" + username + "&usertounfollow=" + userToUnfollow;
//
//        JsonObjectRequest unfollowRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
//                response -> {
//                    // Handle success
//                    callback.onSuccess(response);
//                },
//                error -> {
//                    // Handle error
//                    error.printStackTrace();
//                    Toast.makeText(context, "Error unfollowing user", Toast.LENGTH_SHORT).show();
//                }) {
//            @Override
//            public void getHeaders() {
//                headers.put("Authorization", token);
//                return headers;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//        queue.add(unfollowRequest);
//    }
//
//    // Method to list followed users
//    public static void listFollowed(Context context, String username, String token, VolleyListCallback callback) {
//        String url = BASE_URL + "social/listfollowed?username=" + username;
//
//        JsonObjectRequest listFollowedRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                response -> {
//                    try {
//                        JSONArray followedArray = response.getJSONArray("data");
//                        // Parse the list of followed users
//                        List<User> followedUsers = parseUserList(followedArray);
//                        callback.onSuccess(followedUsers);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(context, "Error parsing followed list", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                error -> {
//                    // Handle error
//                    error.printStackTrace();
//                    Toast.makeText(context, "Error fetching followed users", Toast.LENGTH_SHORT).show();
//                }) {
//            @Override
//            public void getHeaders() {
//                headers.put("Authorization", token);
//                return headers;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//        queue.add(listFollowedRequest);
//    }
//
//    // Method to list followers
//    public static void listFollowers(Context context, String username, String token, VolleyListCallback callback) {
//        String url = BASE_URL + "social/listfollowers?username=" + username;
//
//        JsonObjectRequest listFollowersRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                response -> {
//                    try {
//                        JSONArray followersArray = response.getJSONArray("data");
//                        // Parse the list of followers
//                        List<User> followers = parseUserList(followersArray);
//                        callback.onSuccess(followers);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(context, "Error parsing followers list", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                error -> {
//                    // Handle error
//                    error.printStackTrace();
//                    Toast.makeText(context, "Error fetching followers", Toast.LENGTH_SHORT).show();
//                }) {
//            @Override
//            public void getHeaders() {
//                headers.put("Authorization", token);
//                return headers;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//        queue.add(listFollowersRequest);
//    }
//
//    // Helper method to parse a list of users from a JSON array
//    private static List<User> parseUserList(JSONArray userArray) {
//        List<User> userList = new ArrayList<>();
//        for (int i = 0; i < userArray.length(); i++) {
//            try {
//                JSONObject userObject = userArray.getJSONObject(i);
//                String username = userObject.getString("username");
//                // Add more user fields as needed
//                userList.add(new User(username));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return userList;
//    }
//
//    // Callback interface for Volley response handling
//    public interface VolleyCallback {
//        void onSuccess(JSONObject result);
//    }
//
//    // Callback interface for handling lists of users
//    public interface VolleyListCallback {
//        void onSuccess(List<User> users);
//    }
//}
