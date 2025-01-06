package com.example.nutrinavigator;


//import static com.example.nutrinavigator.R.layout.activity_main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import java.util.Map;
//import java.util.concurrent.Executors;
//import android.text.TextUtils;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.activity.EdgeToEdge;
//import android.widget.Spinner;


//new user registration page
/**
 * MainActivity class handles the user registration process for the application.
 * It validates user input, handles network requests to register users,
 * and navigates between different application screens.
 *
 *  @author Aidan McGinnis
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Email input field for user registration.
     */
    private EditText email;

    /**
     * Username input field for user registration.
     */
    private EditText username;

    /**
     * Password input field for user registration.
     */
    private EditText password;

    /**
     * Confirm password input field to verify password correctness.
     */
    private EditText confirmPassword;

    /**
     * Intent used for switching between application screens.
     */
    private Intent switchScreens;

    /**
     * URL endpoint for user registration.
     */
    private static final String REGISTER_URL = "http://coms-3090-005.class.las.iastate.edu:8080/login/register";

    /**
     * RecyclerView instance (not used in current implementation).
     */
    private RecyclerView recyclerView;

    /**
     * Handler used for periodic tasks (e.g., polling server).
     */
    private Handler handler;

    /**
     * Polling interval in milliseconds.
     */
    private static final long POLL_INTERVAL = 30000; // 30 seconds

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Volley request queue.
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Initialize UI elements.
        email = findViewById(R.id.emailEditText);
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.confirmPasswordEditText);
        Button confirmButton = findViewById(R.id.confirmButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        // Set up the cancel button to return to the login screen.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles cancel button click to navigate back to the login screen.
             *
             * @param view The clicked view.
             */
            @Override
            public void onClick(View view) {
                switchScreens = new Intent(MainActivity.this, LoginPage.class);
                startActivity(switchScreens);
            }
        });

        // Set up the confirm button to validate input and register the user.
        confirmButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles confirm button click to validate and register the user.
             *
             * @param view The clicked view.
             */
            @Override
            public void onClick(View view) {
                switchScreens = new Intent(MainActivity.this, RecipeCreationPage.class);

                if (checkPassword(password.getText().toString(), confirmPassword.getText().toString())) {
                    registerUser();
                } else {
                    confirmPassword.setText("");
                    confirmPassword.setHint("Incorrect! Please try again.");
                }
            }
        });
    }

    /**
     * Registers the user by sending their data to the server.
     */
    private void registerUser() {
        JSONObject userData = new JSONObject();
        try {
            userData.put("email", email.getText().toString());
            userData.put("username", username.getText().toString());
            userData.put("password", password.getText().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        registrationRoundtrip(userData);
    }

    /**
     * Validates that two password strings match.
     *
     * @param password      The original password.
     * @param passwordCheck The confirmation password.
     * @return True if the passwords match, false otherwise.
     */
    private boolean checkPassword(String password, String passwordCheck) {
        if (password.length() != passwordCheck.length()) {
            return false;
        }

        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) != passwordCheck.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sends a registration request to the server using a JSON object.
     *
     * @param registrationInfo The user's registration data.
     */
    private void registrationRoundtrip(JSONObject registrationInfo) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, registrationInfo,
                response -> {
                    try {
                        if (response.getString("message").equals("user exists")) {
                            username.setText("");
                            username.setHint("Username already exists");
                        } else {
                            // Save user information in shared preferences.
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("email", response.getString("email"));
                            editor.putString("username", response.getString("username"));
                            editor.putString("password", response.getString("password"));

                            editor.apply();

                            Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            startActivity(switchScreens);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error processing server response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.getCause() instanceof UnknownHostException) {
                        Toast.makeText(MainActivity.this, "Unable to reach the server. Please check your internet connection.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Registration failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(registerRequest);
    }
}