package com.example.nutrinavigator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

/**
 * The DietaryPreferences class allows the user to input
 * and save their dietary preferences and restrictions.
 * This activity gets user input and returns it to the calling activity.
 *
 * @author Colin Doranski
 */
public class DietaryPreferences extends AppCompatActivity{
    //editViews for user to edit dietary restrictions and preferences
    private EditText editDietaryRestrictions, editDietaryPreferences;

    /**
     * Called when the activity is created. This method sets up the UI components
     * and initializes event listeners.
     *
     * @param savedInstanceState Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietarypreferences);

        //Initializes the EditText fields
        editDietaryPreferences = findViewById(R.id.et_dietarypreferences);
        editDietaryRestrictions = findViewById(R.id.et_dietaryrestrictions);

        //Sets up the save button to trigger the savePreferences method
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                savePreferences();
            }
        });
    }

    /**
     * Collects the user's dietary preferences and restrictions from the users input and
     * packages them into an Intent, and returns the data to the calling activity.
     */
    private void savePreferences() {
        //Gets input data from the EditText fields
        String dietaryRestrictions = editDietaryRestrictions.getText().toString();
        String dietaryPreferences = editDietaryPreferences.getText().toString();

        //Creates an Intent to send data back
        Intent intent = new Intent();
        intent.putExtra("dietaryPreferences", dietaryPreferences);
        intent.putExtra("dietaryRestrictions", dietaryRestrictions);

        //indicates success and finishes the activity
        setResult(RESULT_OK, intent);
        finish();
    }
}
