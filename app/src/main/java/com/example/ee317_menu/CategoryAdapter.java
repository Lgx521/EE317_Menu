package com.yourcompany.shoppingappdemo; // Replace with your package name

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<String> categories;
    private final LayoutInflater inflater;
    private final Context context;
    private int selectedPosition = 0; // Default selection is the first item ("主食")
    private final OnCategoryClickListener listener;

    // Interface for click events
    public interface OnCategoryClickListener {
        void onCategoryClick(int position, String categoryName);
    }

    public CategoryAdapter(Context context, List<String> categories, OnCategoryClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String categoryName = categories.get(position);
        holder.categoryNameTextView.setText(categoryName);

        // Handle selection background and text color
        if (position == selectedPosition) {
            // Special case for "主食" (Main Course) - White background
            if (categoryName.equals(context.getString(R.string.category_main_course))) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                // Use a contrasting text color for white background (e.g., dark blue or black)
                holder.categoryNameTextView.setTextColor(ContextCompat.getColor(context, R.color.toolbar_background));
            } else {
                // Orange background for other selected categories
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.category_highlight_orange));
                holder.categoryNameTextView.setTextColor(ContextCompat.getColor(context, R.color.white)); // White text on orange
            }
        } else {
            // Default background for non-selected items (e.g., light grey)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.category_default_background));
            holder.categoryNameTextView.setTextColor(ContextCompat.getColor(context, R.color.black)); // Default text color
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            // Check if listener is set and if the clicked position is different from current selection
            if (listener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != selectedPosition) { // Only update if a different item is clicked
                    int previousSelectedPosition = selectedPosition;
                    selectedPosition = clickedPosition;
                    listener.onCategoryClick(selectedPosition, categories.get(selectedPosition));

                    // Notify adapter to redraw the previous and current selected items
                    notifyItemChanged(previousSelectedPosition);
                    notifyItemChanged(selectedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.textViewCategoryName);
        }
    }

    // Optional: Method to programmatically set selection if needed externally
    public void setSelectedPosition(int position) {
        if (position >= 0 && position < categories.size()) {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
        }
    }
}