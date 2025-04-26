package com.example.ee317_menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Adapter for the left-side category RecyclerView.
 * Handles displaying category names and managing selection state with specific colors.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<String> categories;
    private final LayoutInflater inflater;
    private final Context context;
    private int selectedPosition = 0; // Default selection is the first item (index 0)
    private final OnCategoryClickListener listener;

    /**
     * Interface definition for a callback to be invoked when a category is clicked.
     */
    public interface OnCategoryClickListener {
        /**
         * Called when a category view is clicked.
         *
         * @param position The position of the clicked item in the adapter.
         * @param categoryName The name of the category that was clicked.
         */
        void onCategoryClick(int position, String categoryName);
    }

    /**
     * Constructor for CategoryAdapter.
     *
     * @param context      The context, used for inflating layouts and getting resources.
     * @param categories   The list of category names to display.
     * @param listener     The listener that will handle category click events.
     */
    public CategoryAdapter(Context context, List<String> categories, OnCategoryClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout defined in list_item_category.xml for each item
        View view = inflater.inflate(R.layout.list_item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        String categoryName = categories.get(position);
        // Set item views based on your views and data model
        holder.categoryNameTextView.setText(categoryName);

        // --- Apply Color Styling based on Selection State ---
        if (position == selectedPosition) {
            // *** SELECTED item style ***
            // Background: White
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            // Text: Black (for contrast on white)
            holder.categoryNameTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            // *** UNSELECTED item style ***
            // Background: Orange
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.category_highlight_orange));
            // Text: White
            holder.categoryNameTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        // --- End of Color Styling ---

        // --- Set OnClickListener for the Item ---
        holder.itemView.setOnClickListener(v -> {
            // Ensure the listener is set and the position is valid
            if (listener != null) {
                int currentPosition = holder.getAdapterPosition(); // Get current position safely
                if (currentPosition != RecyclerView.NO_POSITION) {
                    // Only trigger updates if a *different* item is clicked
                    if (currentPosition != selectedPosition) {
                        int previousSelectedPosition = selectedPosition; // Remember the previously selected item
                        selectedPosition = currentPosition; // Update the selected position

                        // Notify the activity/fragment (listener) about the click
                        listener.onCategoryClick(selectedPosition, categories.get(selectedPosition));

                        // Crucial: Notify the adapter that item views need to be redrawn
                        // Redraw the item that was *previously* selected to apply unselected style
                        notifyItemChanged(previousSelectedPosition);
                        // Redraw the item that is *now* selected to apply selected style
                        notifyItemChanged(selectedPosition);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return categories.size();
    }

    /**
     * ViewHolder class to hold references to the views for each category item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView; // The TextView displaying the category name

        ViewHolder(View itemView) {
            super(itemView);
            // Find the TextView within the inflated item layout
            categoryNameTextView = itemView.findViewById(R.id.textViewCategoryName);
        }
    }

    /**
     * Allows setting the selected category programmatically from outside the adapter.
     *
     * @param position The new position to select.
     */
    public void setSelectedPosition(int position) {
        // Check if the position is valid within the bounds of the category list
        if (position >= 0 && position < categories.size()) {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = position;
            // Redraw the previously selected and newly selected items
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
            // Consider notifying the listener as well if external selection should trigger actions
            // if (listener != null) {
            //     listener.onCategoryClick(selectedPosition, categories.get(selectedPosition));
            // }
        }
    }
}