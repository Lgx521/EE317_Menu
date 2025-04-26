package com.example.ee317_menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final List<String> items;
    private final LayoutInflater inflater;

    public ItemAdapter(Context context, List<String> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = new ArrayList<>(items); // Create a mutable copy
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ensure you are using the layout designed for taller items
        View view = inflater.inflate(R.layout.list_item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itemName = items.get(position);
        holder.itemNameTextView.setText(itemName);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Method to update the list of items when a new category is selected
    public void updateData(List<String> newItems) {
        items.clear();
        if (newItems != null) { // Add null check for safety
            items.addAll(newItems);
        }
        notifyDataSetChanged(); // Notify adapter that the data set has changed
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.textViewItemName);
        }
    }
}