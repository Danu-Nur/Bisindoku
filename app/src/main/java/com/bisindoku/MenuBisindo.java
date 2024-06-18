package com.bisindoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bisindoku.databinding.ActivityMainBinding;
import com.bisindoku.databinding.ActivityMenuBisindoBinding;

public class MenuBisindo extends AppCompatActivity {
    private ActivityMenuBisindoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBisindoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.btnOrganTubuh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuBisindo.this, ListMenu.class);
                intent.putExtra("menu", "organ");
                startActivity(intent);
                finish();
            }
        });

        binding.ekspresiWajah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuBisindo.this, ListMenu.class);
                intent.putExtra("menu", "ekspresi");
                startActivity(intent);
                finish();
            }
        });

//        binding.kuis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MenuBisindo.this, ListMenu.class);
//
//                startActivity(intent);
//                finish();
//            }
//        });

    }
}