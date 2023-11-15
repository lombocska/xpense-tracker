package com.example.xpense_tracker.ui.home;

import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.xpense_tracker.R;

/**
 * An Adapter that shows Single, Two, and Three line list items
 * https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/lists/ListsMainDemoFragment.java
 * https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/lists/res/layout/material_list_item_three_line.xml
 */
public class TransactionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return ThreeLineItemViewHolder.create(parent);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        bind((ThreeLineItemViewHolder) viewHolder);
        viewHolder.itemView.setOnClickListener(
                v -> Toast.makeText(v.getContext(), R.string.mtrl_list_item_clicked, Toast.LENGTH_SHORT).show());
    }

    private void bind(ThreeLineItemViewHolder vh) {
        vh.text.setText(R.string.mtrl_list_item_three_line);
        vh.secondary.setText(R.string.mtrl_list_item_secondary_text);
        vh.tertiary.setText(R.string.mtrl_list_item_tertiary_text);
        vh.icon.setImageResource(R.drawable.logo_avatar_anonymous_40dp);
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
