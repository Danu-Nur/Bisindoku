package com.bisindoku;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bisindoku.databinding.ActivityPlayVideoBinding;

public class PlayVideoActivity extends AppCompatActivity {

    private ActivityPlayVideoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        EdgeToEdge.enable(this);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        String fileId = getIntent().getStringExtra("fileId");
        binding.textVideo.setText(fileId);

        String videoUrl = "https://drive.google.com/uc?export=download&id=1iIZ9EhwCnaMAwW-ENLGZTMkCMq_OsrmP";
        Uri uri = Uri.parse(videoUrl);
        binding.videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        binding.videoView.setMediaController(mediaController);
        mediaController.setAnchorView(binding.videoView);

        binding.videoView.start();

    }

    @Override
    public void onBackPressed() {
        String menu = getIntent().getStringExtra("menu");
        // Navigate to a specific activity when back button is pressed
        Intent intent = new Intent(PlayVideoActivity.this, ListMenu.class);  // Replace SpecificActivity with your target activity
        intent.putExtra("menu", menu);
        startActivity(intent);
        finish();  // Optional: Call finish() if you don't want the current activity to remain in the back stack
        super.onBackPressed();
    }
}