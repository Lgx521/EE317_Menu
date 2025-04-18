package com.example.ee317_menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewItems;
    private CategoryAdapter categoryAdapter;
    private ItemAdapter itemAdapter;

    private Map<String, List<String>> foodData;
    private List<String> categoryNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Prepare Data
        prepareFoodData();

        // 2. Find Views
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        // 3. Setup Categories RecyclerView
        categoryNames = new ArrayList<>(foodData.keySet());
        categoryAdapter = new CategoryAdapter(this, categoryNames, this);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategories.setAdapter(categoryAdapter);

        // 4. Setup Items RecyclerView (Initially show items for the first category)
        String firstCategory = categoryNames.get(0);
        itemAdapter = new ItemAdapter(this, Objects.requireNonNull(foodData.get(firstCategory)));
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setAdapter(itemAdapter);

        // 5. Set initial selection state in Category Adapter (handled by default selectedPosition=0 in adapter)
        // categoryAdapter.setSelectedPosition(0); // Explicitly setting if needed, but default is 0
    }

    // Generates the data according to the requirements (10 items per category)
    private void prepareFoodData() {
        foodData = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order

        String mainCourse = getString(R.string.category_main_course);
        String stirFry = getString(R.string.category_stir_fry);
        String malatang = getString(R.string.category_malatang);
        String halal = getString(R.string.category_halal);

        foodData.put(mainCourse, generateItems(mainCourse, 10));
        foodData.put(stirFry, generateItems(stirFry, 10));
        foodData.put(malatang, generateItems(malatang, 10));
        foodData.put(halal, generateItems(halal, 10));
    }

    // Helper method to generate item names
    private List<String> generateItems(String categoryPrefix, int count) {
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            items.add(categoryPrefix + i);
        }
        return items;
    }

    // Callback from CategoryAdapter when a category is clicked
    @Override
    public void onCategoryClick(int position, String categoryName) {
        // Update the ItemAdapter with the items for the selected category
        List<String> items = foodData.get(categoryName);
        if (items != null) {
            itemAdapter.updateData(items);
        }
    }
}