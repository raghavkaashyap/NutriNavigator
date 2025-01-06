/*package com.example.nutrinavigator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {
        private EditText etComment;
        private Button btnSubmitComment;
        private TextView tvComments;
        private RequestQueue requestQueue;
        private String commenterUsername = "test_user4"; // Change this to get the actual username
        private String recipeOwnerUsername = "test_user"; //Change this to get the actual recipe owner username
        private int recipeId = 13; // Change this to get the actual recipe ID

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_comments);
            etComment = findViewById(R.id.editComment);
            btnSubmitComment = findViewById(R.id.addComment);
            tvComments = findViewById(R.id.vComments);
            requestQueue = Volley.newRequestQueue(this);
            btnSubmitComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = etComment.getText().toString();
                    if (!comment.isEmpty()) {
                        submitComment(comment);
                    } else {
                        Toast.makeText(CommentsActivity.this, "Enter comment", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            loadComments();
        }

        private void submitComment(String comment) {
            String url = "http://coms-3090-005.class.las.iastate.edu:8080/comments?commenterUsername=" + commenterUsername + "&recipeOwnerUsername=" + recipeOwnerUsername + "&recipeId=" + recipeId;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(CommentsActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                    etComment.setText("");
                    loadComments();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CommentsActivity.this, "Failed to add comment: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public byte[] getBody() {
                    return comment.getBytes();
                }
            };
            requestQueue.add(stringRequest);
        }

        private void loadComments() {
            String url = "http://coms-3090-005.class.las.iastate.edu:8080/comments/recipeId?recipeId=" + recipeId;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray commentsArray = new JSONArray(response);
                        StringBuilder comments = new StringBuilder();
                        for (int i = 0; i < commentsArray.length(); i++) {
                            comments.append(commentsArray.getJSONObject(i).getString("comment")).append("\n\n");
                        }
                        tvComments.setText(comments.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CommentsActivity.this, "Failed to parse", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CommentsActivity.this, "Failed to load: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(stringRequest);
        }
    }


 */

package com.example.nutrinavigator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private EditText etComment;
    private Button btnSubmitComment, btnUpdateComment, btnDeleteComment, btnDeleteCommentsByRecipe, btnDeleteCommentsByUser;
    private RecyclerView rvComments;
    private RequestQueue requestQueue;
    private List<Comment> commentsList = new ArrayList<>();
    private String commenterUsername = "test_user4";
    private String recipeOwnerUsername = "test_user3";
    private int recipeId = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        etComment = findViewById(R.id.et_comment);
        btnSubmitComment = findViewById(R.id.btn_submit_comment);
        btnUpdateComment = findViewById(R.id.btn_update_comment);
        btnDeleteComment = findViewById(R.id.btn_delete_comment);
        btnDeleteCommentsByRecipe = findViewById(R.id.btn_delete_comments_by_recipe);
        btnDeleteCommentsByUser = findViewById(R.id.btn_delete_comments_by_user);
        rvComments = findViewById(R.id.rv_comments);

        requestQueue = Volley.newRequestQueue(this);

        rvComments.setLayoutManager(new LinearLayoutManager(this));
        CommentsFormat commentsAdapter = new CommentsFormat(commentsList);
        rvComments.setAdapter(commentsAdapter);
        loadComments();

        rvComments.setAdapter(commentsAdapter);
        loadComments();

        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etComment.getText().toString();
                if (!comment.isEmpty()) {
                    submitComment(comment);
                } else {
                    Toast.makeText(CommentsActivity.this, "Enter a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnUpdateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Here, we assume you have the commentId and newMessage from somewhere (like a dialog)
                int commentId = 1; // Replace with actual comment ID
                String newMessage = etComment.getText().toString(); // New message
                if (!newMessage.isEmpty()) {
                    updateComment(commentId, newMessage);
                } else {
                    Toast.makeText(CommentsActivity.this, "Enter a new comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Assume you get the commentId from somewhere (like a selected item)
                int commentId = 1; // Replace with actual comment ID
                deleteComment(commentId);
            }
        });
        btnDeleteCommentsByRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCommentsByRecipeId(recipeId);
            }
        });
        btnDeleteCommentsByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCommentsByUser(commenterUsername);
            }
        });
    }

private void submitComment(String comment) {
    String url = "http://coms-3090-005.class.las.iastate.edu:8080/comments";

    // Construct the full URL with query parameters
    String fullUrl = url + "?commenterUsername=" + commenterUsername +
            "&recipeOwnerUsername=" + recipeOwnerUsername +
            "&recipeId=" + recipeId;

    // Create the JSONObject for the comment body
    JSONObject commentJson = new JSONObject();
    try {
        commentJson.put("comment", comment);  // Add the comment text
    } catch (JSONException e) {
        e.printStackTrace();
    }

    // Create the JsonObjectRequest for POST
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, fullUrl, commentJson,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Handle the successful response
                    Toast.makeText(CommentsActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                    etComment.setText("");  // Clear the input field
                    loadComments();  // Reload the comments list
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error response
                    Toast.makeText(CommentsActivity.this, "Failed to add comment: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    // Add the request to the RequestQueue
    requestQueue.add(jsonObjectRequest);
}
    private void loadComments() {
        String url = "http://coms-3090-005.class.las.iastate.edu:8080/comments/recipeId?recipeId=" + recipeId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray commentsArray = new JSONArray(response);
                    commentsList.clear(); // Clear existing comments
                    for (int i = 0; i < commentsArray.length(); i++) {
                        String commenter = commentsArray.getJSONObject(i).getString("commenterUsername");
                        String commentText = commentsArray.getJSONObject(i).getString("comment");

                        // Add new Comment object to the list
                        commentsList.add(new Comment(commenter, commentText));
                    }

                    // Notify the adapter that the data has changed
                    CommentsFormat adapter = (CommentsFormat) rvComments.getAdapter();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CommentsActivity.this, "Failed to parse comments", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CommentsActivity.this, "Failed to load comments: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void deleteComment(int commentId) {
        String url = "http://coms-3090-005.class.las.iastate.edu:8080/comments/commentId?commentId=" + commentId;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CommentsActivity.this, "Comment deleted successfully", Toast.LENGTH_SHORT).show();
                        loadComments();  // Refresh comments after deletion
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CommentsActivity.this, "Failed to delete comment: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void updateComment(int commentId, String newMessage) {
        String url = "http://coms-3090-005.class.las.iastate.edu:8080/comments?commentId=" + commentId + "&newMessage=" + newMessage;

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CommentsActivity.this, "Comment updated successfully", Toast.LENGTH_SHORT).show();
                        loadComments();  // Refresh comments after update
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CommentsActivity.this, "Failed to update comment: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void deleteCommentsByRecipeId(int recipeId) {
        String url = "http://coms-3090-005.class.las.iastate.edu:8080/comments/recipeId?recipeId=" + recipeId;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CommentsActivity.this, "Comments deleted successfully", Toast.LENGTH_SHORT).show();
                        loadComments();  // Refresh comments after deletion
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CommentsActivity.this, "Failed to delete comments: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void deleteCommentsByUser(String username) {
        String url = "http://coms-3090-005.class.las.iastate.edu:8080/comments/username?username=" + username;

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CommentsActivity.this, "Comments deleted successfully", Toast.LENGTH_SHORT).show();
                        loadComments();  // Refresh comments after deletion
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CommentsActivity.this, "Failed to delete comments: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(stringRequest);
    }
}
