package com.example.xpense_tracker.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.annotation.NonNull;

import com.example.xpense_tracker.R;

public class ThreeLineItemViewHolder extends ViewHolder {

    public final ImageView icon;
    public final TextView text;
    public final TextView secondary;
    public final TextView tertiary;

    public ThreeLineItemViewHolder(@NonNull View view) {
        super(view);
        this.icon = itemView.findViewById(R.id.mtrl_list_item_icon);
        this.text = itemView.findViewById(R.id.mtrl_list_item_text);
        this.secondary = itemView.findViewById(R.id.mtrl_list_item_secondary_text);
        this.tertiary = itemView.findViewById(R.id.mtrl_list_item_tertiary_text);
    }

    @NonNull
    public static ThreeLineItemViewHolder create(@NonNull ViewGroup parent) {
        return new ThreeLineItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.material_list_item_three_line, parent, false));
    }
}
