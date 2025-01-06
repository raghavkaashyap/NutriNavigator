package com.example.nutrinavigator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomePage extends AppCompatActivity {

    private Button btnMenu;
    private Button btnAddRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);

        // Initialize buttons
        btnMenu = findViewById(R.id.btnMenu);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);

        // Set up click listeners
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle menu button click
                Toast.makeText(WelcomePage.this, "Menu clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle add recipe button click
                Toast.makeText(WelcomePage.this, "Add Recipe clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
