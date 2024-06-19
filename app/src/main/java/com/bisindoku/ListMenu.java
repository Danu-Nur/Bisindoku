package com.bisindoku;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
//        EdgeToEdge.enable(this);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Intent intent = getIntent();
        String menu = intent.getStringExtra("menu");
        list = new ArrayList<>();
        String[] organ = {"Otak", "Telinga", "Mulut", "Ginjal", "Paru-paru", "Kerongkongan", "Usus", "Pankreas", "Hati", "Lambung"};
        String[] ekspresi = {"Senang", "Marah", "Sedih", "Malu", "Menangis", "Tersenyum", "Tertawa", "Kaget", "Takut", "Bingung"};

        if ("organ".equals(menu)) {
            addDataToList(organ);
        } else if ("ekspresi".equals(menu)) {
            addDataToList(ekspresi);
        }

        // Set up the adapter with the custom layout
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.text1, list);
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

        // Set up item click listener for ListView
        binding.lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position);
                Intent intent = new Intent(ListMenu.this, PlayVideoActivity.class);
                intent.putExtra("fileId", selectedItem);
                intent.putExtra("menu", menu); // Assuming 'menu' refers to the 'key' value passed from the previous activity
                startActivity(intent);
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
