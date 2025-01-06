package com.example.nutrinavigator;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class GroceryList extends AppCompatActivity {
        private static final String BASE_URL = "http://coms-3090-005.class.las.iastate.edu:8080/grocerylist";
        private RecyclerView recyclerView;
        private ProgressBar progressBar;
        private RequestQueue requestQueue;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_grocerylist);

            recyclerView = findViewById(R.id.recyclerView);
            progressBar = findViewById(R.id.progressBar);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            requestQueue = Volley.newRequestQueue(this);

            fetchGroceryList("testUser", "Bearer your_jwt_token");
        }

        private void fetchGroceryList(String username, String token) {
            progressBar.setVisibility(View.VISIBLE);

            String url = BASE_URL + "?username=" + username;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressBar.setVisibility(View.GONE);
                            try {
                                String message = response.getString("message");
                                if ("Success".equals(message)) {
                                    JSONArray data = response.getJSONArray("data");
                                    List<String> groceryList = new ArrayList<>();
                                    for (int i = 0; i < data.length(); i++) {
                                        groceryList.add(data.getString(i));
                                    }
                                    recyclerView.setAdapter(new GroceryListAdapter(groceryList));
                                } else {
                                    Toast.makeText(GroceryList.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(GroceryList.this, "Parsing error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            Log.e("VOLLEY_ERROR", error.toString());
                            Toast.makeText(GroceryList.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", token);
                    return headers;
                }
            };

            requestQueue.add(jsonObjectRequest);
        }
    }
