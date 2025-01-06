package com.example.nutrinavigator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlanDetails extends AppCompatActivity {
    private TextView tvPlanDate, tvPlanRecipe;
    private Button btnDeletePlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plandetails);

        tvPlanDate = findViewById(R.id.tvPlanDate);
        tvPlanRecipe = findViewById(R.id.tvPlanRecipe);
        btnDeletePlan = findViewById(R.id.btnDeletePlan);

        // TODO: Retrieve plan details via Intent or API call

        btnDeletePlan.setOnClickListener(v -> {
            // TODO: Implement API call to delete the plan
            finish();
        });
    }
}
