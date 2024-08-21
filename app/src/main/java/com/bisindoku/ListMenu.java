package com.bisindoku;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bisindoku.databinding.ActivityListMenuBinding;
import com.bisindoku.model.MenuItems;
import com.bisindoku.server.Server;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ListMenu extends AppCompatActivity {

    private ActivityListMenuBinding binding;
    private ArrayList<MenuItems> menuItemsArrayList;
    private ArrayAdapter<String> adapter;
    private static final String API_URL_EKSPRESI = Server.URL + "api/ekspresi";
    private static final String API_URL_ORGAN = Server.URL + "api/organs";

    private String detailMenu = "";
    private String menuType = "";
    private static final String TAG = "ListMenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SwipeRefreshLayout swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setDistanceToTriggerSync(300);
        Intent intent = getIntent();
        String menu = intent.getStringExtra("menu");
//        String detailMenu = "";
        menuItemsArrayList = new ArrayList<>();

        selectMenu(menu);

        binding.customTollbar.imageButtonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        // Set up the adapter with the custom layout
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.text1, new ArrayList<String>());
        binding.lv1.setAdapter(adapter);

        // Set up SwipeRefreshLayout listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selectMenu(menu);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Change the divider color
//        binding.lv1.setDivider(new ColorDrawable(Color.BLACK));
//        binding.lv1.setDividerHeight(1);

        // Set up the SearchView listener
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (menuItemsArrayList.contains(query)) {
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
//        String finalDetailMenu = detailMenu;
        binding.lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItems selectedItem = menuItemsArrayList.get(position);
                Intent intent = new Intent(ListMenu.this, PlayVideoActivity.class);
                intent.putExtra("name", selectedItem.getNamaItem());
                intent.putExtra("videoLink", selectedItem.getLinkVideo());
                intent.putExtra("menu", menu);
                intent.putExtra("title", detailMenu);
                startActivity(intent);
            }
        });
    }

    public void selectMenu(String menuType) {
        if ("organ".equals(menuType)) {
            fetchDataFromApi(API_URL_ORGAN, menuType);
            detailMenu = "Detail Nama Organ Tubuh";
            binding.customTollbar.imageViewLogo.setImageResource(R.drawable.organ);
        } else if ("ekspresi".equals(menuType)) {
            fetchDataFromApi(API_URL_EKSPRESI, menuType);
            detailMenu = "Detail Ekspresi Wajah";
            binding.customTollbar.imageViewLogo.setImageResource(R.drawable.ekspresi);
        }
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
                    detailMenu = "Detail Nama Organ Tubuh";
                    binding.customTollbar.imageViewLogo.setImageResource(R.drawable.organ);
                    fetchDataFromApi(API_URL_ORGAN, "organ");
                    return true;
                } else if (itemId == R.id.action_ekspresi) {
                    detailMenu = "Detail Ekspresi Wajah";
                    binding.customTollbar.imageViewLogo.setImageResource(R.drawable.ekspresi);
                    fetchDataFromApi(API_URL_EKSPRESI, "ekspresi");
                    return true;
                } else if (itemId == R.id.action_kuis) {
                    Intent intent = new Intent(ListMenu.this, QuizPilihanGandaActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
    }

    private void fetchDataFromApi(String apiUrl, String menuType) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            menuItemsArrayList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String namaItem = "";
                                if ("organ".equals(menuType)) {
                                    namaItem = jsonObject.getString("nama_organ");
                                } else if ("ekspresi".equals(menuType)) {
                                    namaItem = jsonObject.getString("nama_ekspresi");
                                }
                                String id = jsonObject.getString("id");
                                String linkVideo = jsonObject.getString("link_video");

                                MenuItems menuItems = new MenuItems(id, namaItem, linkVideo);
                                menuItemsArrayList.add(menuItems);
                            }
                            updateAdapter();
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing JSON data: " + e.getMessage());
                            e.printStackTrace();
                            Snackbar.make(binding.lv1, "Error parsing JSON data", Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching data: " + error.getMessage());
                        Snackbar.make(binding.lv1, "Error fetching data", Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    // Function to add data to the list
    private void updateAdapter() {
        ArrayList<String> names = new ArrayList<>();
        for (MenuItems menuItems : menuItemsArrayList) {
            names.add(menuItems.getNamaItem());
        }
        adapter.clear();
        adapter.addAll(names);
        adapter.notifyDataSetChanged();
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
