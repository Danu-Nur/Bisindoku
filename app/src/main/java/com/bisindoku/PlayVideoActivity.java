package com.bisindoku;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bisindoku.databinding.ActivityPlayVideoBinding;
import com.bisindoku.server.Server;

public class PlayVideoActivity extends AppCompatActivity {

    private ActivityPlayVideoBinding binding;
    private static final String URL_DOMAIN = Server.URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.customTollbar.imageButtonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        String menu = getIntent().getStringExtra("menu");

        if ("organ".equals(menu)) {
            binding.customTollbar.imageViewLogo.setImageResource(R.drawable.organ);
        } else if ("ekspresi".equals(menu)) {
            binding.customTollbar.imageViewLogo.setImageResource(R.drawable.ekspresi);
        }

        String fileId = getIntent().getStringExtra("name");
        String titleDetail = getIntent().getStringExtra("title");
        String videoUrl = URL_DOMAIN + getIntent().getStringExtra("videoLink");

        binding.textVideo.setText(fileId);
        binding.textViewTitleVideo.setText(titleDetail);

//        String videoUrl = "https://drive.google.com/uc?export=download&id=1iIZ9EhwCnaMAwW-ENLGZTMkCMq_OsrmP";
        Uri uri = Uri.parse(videoUrl);
        binding.videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        binding.videoView.setMediaController(mediaController);
        mediaController.setAnchorView(binding.videoView);

        binding.videoView.start();

        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String menu = getIntent().getStringExtra("menu");
                // Navigate to a specific activity when back button is pressed
                Intent intent = new Intent(PlayVideoActivity.this, ListMenu.class);  // Replace SpecificActivity with your target activity
                intent.putExtra("menu", menu);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_list, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_organ) {
                    Intent intent = new Intent(PlayVideoActivity.this, ListMenu.class);
                    intent.putExtra("menu", "organ");
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.action_ekspresi) {
                    Intent intent = new Intent(PlayVideoActivity.this, ListMenu.class);
                    intent.putExtra("menu", "ekspresi");
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.action_kuis) {
                    Intent intent = new Intent(PlayVideoActivity.this, QuizPilihanGandaActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
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