package com.example.ee317_menu;

import android.content.Context;
import android.graphics.Color;
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
    private int selectedPosition = 0; // Default selection is the first item
    private OnCategoryClickListener listener;

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

        // Handle selection background
        if (position == selectedPosition) {
            // Special case for "主食" (Main Course) - White background
            if (categoryName.equals(context.getString(R.string.category_main_course))) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.categoryNameTextView.setTextColor(ContextCompat.getColor(context, R.color.toolbar_background)); // Darker text for contrast on white
            } else {
                // Orange background for other selected categories
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.category_highlight_orange));
                holder.categoryNameTextView.setTextColor(ContextCompat.getColor(context, R.color.white)); // White text on orange
            }
        } else {
            // Default background for non-selected items
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.category_default_background));
            holder.categoryNameTextView.setTextColor(ContextCompat.getColor(context, R.color.black)); // Default text color
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int previousSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                listener.onCategoryClick(selectedPosition, categoryName);

                // Notify adapter to redraw the previous and current selected items
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);
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

    // Method to programmatically set selection (e.g., for initial state)
    public void setSelectedPosition(int position) {
        int previousSelectedPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousSelectedPosition);
        notifyItemChanged(selectedPosition);
    }
}