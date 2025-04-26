package com.yourcompany.shoppingappdemo; // Replace with your package name

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Import ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration; // Import DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable; // Import Drawable
import android.os.Bundle;

import java.util.ArrayList;
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

    // --- Define the number of items per category ---
    // Change this value if you want more/fewer items per category (e.g., 30 for scrolling)
    private static final int ITEMS_PER_CATEGORY = 10; // As per original image req.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main.xml);

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


        // 4. Setup Items RecyclerView
        String firstCategory = categoryNames.get(0);
        itemAdapter = new ItemAdapter(this, Objects.requireNonNull(foodData.get(firstCategory)));
        LinearLayoutManager itemsLayoutManager = new LinearLayoutManager(this); // Create manager instance
        recyclerViewItems.setLayoutManager(itemsLayoutManager);
        recyclerViewItems.setAdapter(itemAdapter);

        // --- START: Add Divider Decoration ---
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerViewItems.getContext(),
                itemsLayoutManager.getOrientation() // Use orientation from layout manager (VERTICAL)
        );

        // Optional: Set the custom drawable you created (assuming res/drawable/divider_line.xml exists)
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_line);
        if (dividerDrawable != null) {
            dividerItemDecoration.setDrawable(dividerDrawable);
        } else {
            // Optional: Log a warning or handle the case where the drawable is missing
            // Log.w("MainActivity", "Divider drawable not found. Using default.");
        }

        // Add the decoration to the RecyclerView
        recyclerViewItems.addItemDecoration(dividerItemDecoration);
        // --- END: Add Divider Decoration ---


        // 5. Set initial selection state (handled by adapter's default selectedPosition=0)
        // Ensure the first category's style is applied correctly on launch
        // No explicit call needed here if adapter default works, but can force redraw if needed:
        // categoryAdapter.notifyItemChanged(0);
    }

    // Generates the data according to the requirements (ITEMS_PER_CATEGORY items)
    private void prepareFoodData() {
        foodData = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order

        String mainCourse = getString(R.string.category_main_course);
        String stirFry = getString(R.string.category_stir_fry);
        String malatang = getString(R.string.category_malatang);
        String halal = getString(R.string.category_halal);

        // --- Use the ITEMS_PER_CATEGORY constant ---
        foodData.put(mainCourse, generateItems(mainCourse, ITEMS_PER_CATEGORY));
        foodData.put(stirFry, generateItems(stirFry, ITEMS_PER_CATEGORY));
        foodData.put(malatang, generateItems(malatang, ITEMS_PER_CATEGORY));
        foodData.put(halal, generateItems(halal, ITEMS_PER_CATEGORY));
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
        // Note: The selection background change is handled within the CategoryAdapter itself
    }
}