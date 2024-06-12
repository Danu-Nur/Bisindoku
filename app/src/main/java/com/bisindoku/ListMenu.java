package com.bisindoku;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bisindoku.databinding.ActivityListMenuBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class ListMenu extends AppCompatActivity {

    private ActivityListMenuBinding binding;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        list = new ArrayList<>();
        String[] organ = {"Otak", "Telinga", "Mulut", "Ginjal", "Paru-paru", "Kerongkongan", "Usus", "Pankreas", "Hati", "Lambung"};
        String[] ekspresi = {"Senang", "Marah", "Sedih", "Malu", "Menangis", "Tersenyum", "Tertawa", "Kaget", "Takut", "Bingung"};

        if ("organ".equals(value)) {
            addDataToList(organ);
        } else if ("ekspresi".equals(value)) {
            addDataToList(ekspresi);
        }

        // Set up the adapter with the custom layout
        adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
        binding.lv1.setAdapter(adapter);

        // Change the divider color
        binding.lv1.setDivider(new ColorDrawable(Color.BLACK));
        binding.lv1.setDividerHeight(1);

        // Set up the SearchView listener
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (list.contains(query)) {
                    adapter.getFilter().filter(query);
                } else {
                    Snackbar.make(binding.lv1, "Item not found", Snackbar.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    // Function to add data to the list
    private void addDataToList(String[] dataArray) {
        list.clear();
        Collections.addAll(list, dataArray);
    }

    @Override
    public void onBackPressed() {
        // Navigate to a specific activity when back button is pressed
        Intent intent = new Intent(ListMenu.this, MenuBisindo.class);  // Replace SpecificActivity with your target activity
        startActivity(intent);
        finish();  // Optional: Call finish() if you don't want the current activity to remain in the back stack
        super.onBackPressed();
    }
}
