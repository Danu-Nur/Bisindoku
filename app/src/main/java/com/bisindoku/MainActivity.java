package com.bisindoku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bisindoku.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//
//        EdgeToEdge.enable(this);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        // Optional: Set the splash screen layout (if created)
        // setContentView(R.layout.activity_splash);

        // Display the splash screen for a specific duration
        int splashTime = 3000; // milliseconds (3 seconds)
        new Handler().postDelayed(() -> {
            // Start the main activity after the delay
            Intent intent = new Intent(MainActivity.this, MenuBisindo.class);
            startActivity(intent);
            finish();
        }, splashTime);

//        binding.idText.setText("testing streaming");
    }
}