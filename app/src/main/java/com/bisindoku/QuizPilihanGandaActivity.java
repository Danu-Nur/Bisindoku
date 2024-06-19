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

import com.bisindoku.databinding.ActivityQuizPilihanGandaBinding;

public class QuizPilihanGandaActivity extends AppCompatActivity {

    private ActivityQuizPilihanGandaBinding binding;
    SoalPilihanGanda soalPG = new SoalPilihanGanda();
    int skor = 0;
    int arr; //untuk menampung nilai panjang array
    int x;   //menunjukkan konten sekarang
    String jawaban; //menampung jawaban benar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        binding = ActivityQuizPilihanGandaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvSkor.setText("" + skor);

        String videoUrl = "https://drive.google.com/uc?export=download&id=1iIZ9EhwCnaMAwW-ENLGZTMkCMq_OsrmP";
        Uri uri = Uri.parse(videoUrl);
        binding.videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        binding.videoView.setMediaController(mediaController);
        mediaController.setAnchorView(binding.videoView);

        binding.videoView.start();

//        setContentView(R.layout.activity_quiz_pilihan_ganda);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    @Override
    public void onBackPressed() {
        // Navigate to a specific activity when back button is pressed
        Intent intent = new Intent(QuizPilihanGandaActivity.this, MenuBisindo.class);  // Replace SpecificActivity with your target activity
        startActivity(intent);
        finish();  // Optional: Call finish() if you don't want the current activity to remain in the back stack
        super.onBackPressed();
    }
}