package com.bisindoku;

import android.os.Bundle;

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
        EdgeToEdge.enable(this);
        binding = ActivityQuizPilihanGandaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvSkor.setText("" + skor);

//        setContentView(R.layout.activity_quiz_pilihan_ganda);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}