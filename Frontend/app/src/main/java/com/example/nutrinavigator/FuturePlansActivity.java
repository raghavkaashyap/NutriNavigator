package com.example.nutrinavigator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FuturePlansActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPlans;
    private FloatingActionButton fabAddPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futurelisting);

        recyclerViewPlans = findViewById(R.id.recyclerViewPlans);
        fabAddPlan = findViewById(R.id.fabAddPlan);
        recyclerViewPlans.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = Volley.newRequestQueue(this);

        fabAddPlan.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddPlan.class);
            startActivity(intent);
        });

        fetchPlans();
    }

    private void fetchPlans() {
        String url = "http://coms-3090-005.class.las.iastate.edu:8080/listPlans";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<FuturePlan> plans = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            FuturePlan plan = new FuturePlan(
                                    obj.getInt("id"),
                                    obj.getInt("userId"),
                                    obj.getInt("recipeId"),
                                    obj.getString("futureDate")
                            );
                            plans.add(plan);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    FuturePlansAdapter adapter = new FuturePlansAdapter(plans, plan -> {
                        Intent intent = new Intent(this, PlanDetailsActivity.class);
                        intent.putExtra("planId", plan.getId());
                        intent.putExtra("futureDate", plan.getFutureDate());
                        intent.putExtra("recipe", plan.getRecipeId());
                        startActivity(intent);
                    });
                    recyclerViewPlans.setAdapter(adapter);
                },
                error -> {
                    Toast.makeText(this, "Failed to fetch plans.", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }

}
