package com.bisindoku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bisindoku.databinding.ActivityQuizPilihanGandaBinding;
import com.bisindoku.model.Kuis;
import com.bisindoku.server.Server;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizPilihanGandaActivity extends AppCompatActivity {

    private ActivityQuizPilihanGandaBinding binding;
    private static final String API_URL_QUIZ = Server.URL + "api/kuis";
    private static final String URL_DOMAIN = Server.URL;

    private List<Kuis> questionList;
    private int currentQuestionIndex = 0;
    private Random random;
    private static final String TAG = "Kuis";

    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizPilihanGandaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.customTollbar.imageButtonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        binding.customTollbar.imageViewLogo.setImageResource(R.drawable.quiz);

        random = new Random();
        questionList = new ArrayList<>();
        fetchDataFromApi(API_URL_QUIZ);

        binding.rbPilihanJawaban1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(binding.rbPilihanJawaban1);
            }
        });

        binding.rbPilihanJawaban2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(binding.rbPilihanJawaban2);
            }
        });

        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizPilihanGandaActivity.this, MenuBisindo.class);
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
                    Intent intent = new Intent(QuizPilihanGandaActivity.this, ListMenu.class);
                    intent.putExtra("menu", "organ");
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.action_ekspresi) {
                    Intent intent = new Intent(QuizPilihanGandaActivity.this, ListMenu.class);
                    intent.putExtra("menu", "ekspresi");
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.action_kuis) {
                    Intent intent = new Intent(QuizPilihanGandaActivity.this, QuizPilihanGandaActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
    }

    private void fetchDataFromApi(String apiUrl) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            questionList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String pertanyaan = jsonObject.getString("pertanyaan");
                                String linkVideo = jsonObject.getString("link_video");
                                String benar = jsonObject.getString("benar");
                                String salah = jsonObject.getString("salah");

                                Kuis kuis = new Kuis(id, pertanyaan, linkVideo, benar, salah);
                                questionList.add(kuis);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON data: " + e.getMessage());
                            e.printStackTrace();
                            Snackbar.make(binding.videoView, "Error parsing JSON data", Snackbar.LENGTH_LONG).show();
                        }
                        Collections.shuffle(questionList);
                        totalQuestions = questionList.size(); // Set total questions
                        showNextQuestion();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching data: " + error.getMessage());
                        Snackbar.make(binding.videoView, "Error fetching data", Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void showNextQuestion() {
        if (currentQuestionIndex >= totalQuestions) {
            showFinalScore(); // Show final score at the end
            return;
        }

        Kuis kuis = questionList.get(currentQuestionIndex);
        binding.videoView.setVideoURI(Uri.parse(URL_DOMAIN + kuis.getLink_video()));

        MediaController mediaController = new MediaController(this);
        binding.videoView.setMediaController(mediaController);
        mediaController.setAnchorView(binding.videoView);

        binding.videoView.start();

        // Randomize answer positions
        if (random.nextBoolean()) {
            binding.rbPilihanJawaban1.setText(kuis.getBenar());
            binding.rbPilihanJawaban2.setText(kuis.getSalah());
        } else {
            binding.rbPilihanJawaban1.setText(kuis.getSalah());
            binding.rbPilihanJawaban2.setText(kuis.getBenar());
        }

        updateScoreInHeader(); // Update score in the header
    }

    private void checkAnswer(View selectedButton) {
        Kuis kuis = questionList.get(currentQuestionIndex);
        boolean isCorrect = ((TextView) selectedButton).getText().equals(kuis.getBenar());

        String message;
        if (isCorrect) {
            correctAnswers++;
            selectedButton.setBackgroundColor(Color.GREEN);
            message = "Selamat jawaban anda benar!<br> Apakah Anda Ingin Lanjut Ke Soal Berikutnya?";
        } else {
            incorrectAnswers++;
            selectedButton.setBackgroundColor(Color.RED);
            if (binding.rbPilihanJawaban1.getText().equals(kuis.getBenar())) {
                binding.rbPilihanJawaban1.setBackgroundColor(Color.GREEN);
            } else {
                binding.rbPilihanJawaban2.setBackgroundColor(Color.GREEN);
            }
            message = "Jawaban anda salah. <br/> Jawaban yang benar adalah <b>" + kuis.getBenar() + "</b>.<br> Apakah Anda Ingin Lanjut Ke Soal Berikutnya?";
        }

        new AlertDialog.Builder(this)
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentQuestionIndex++;
                        resetButtons();
                        showNextQuestion();
                    }
                })
                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(QuizPilihanGandaActivity.this, MenuBisindo.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void updateScoreInHeader() {
        int totalAnswered = correctAnswers + incorrectAnswers;
        int score = totalAnswered == 0 ? 0 : (correctAnswers * 100) / totalQuestions;
        binding.textViewScore.setText("Skor: " + score);
    }

    private void showFinalScore() {
        int finalScore = (correctAnswers * 100) / totalQuestions;

        String message = "Kuis Selesai!<br/>Benar: " + correctAnswers + "<br/>Salah: " + incorrectAnswers + "<br/>Skor Akhir: " + finalScore;

        new AlertDialog.Builder(this)
                .setTitle("Skor Akhir")
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("KEMBALI KE MENU", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(QuizPilihanGandaActivity.this, MenuBisindo.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void resetButtons() {
        binding.rbPilihanJawaban1.setBackground(ContextCompat.getDrawable(this, R.drawable.background_button_menu));
        binding.rbPilihanJawaban2.setBackground(ContextCompat.getDrawable(this, R.drawable.background_button_menu));
    }

    @Override
    public void onBackPressed() {
        // Navigate to a specific activity when back button is pressed
        Intent intent = new Intent(QuizPilihanGandaActivity.this, MenuBisindo.class);
        startActivity(intent);
        finish();  // Optional: Call finish() if you don't want the current activity to remain in the back stack
        super.onBackPressed();
    }
}

