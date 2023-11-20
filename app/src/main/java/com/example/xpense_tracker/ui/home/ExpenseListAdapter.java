package com.example.xpense_tracker.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.xpense_tracker.R;
import com.example.xpense_tracker.data.ExpenseDataSource;
import com.example.xpense_tracker.data.ExpenseRepository;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.data.model.Expense;

import java.util.List;

/**
 * An Adapter that shows Single, Two, and Three line list items
 * https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/lists/ListsMainDemoFragment.java
 * https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/lists/res/layout/material_list_item_three_line.xml
 */
public class ExpenseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //in a prod code, ExpenseDTO should be used here
    List<Expense> mItem;
    private static volatile ExpenseListAdapter instance;
    private ExpenseRepository expenseRepository;

    public static ExpenseListAdapter getInstance(Context context) {
        if (instance == null) {
            instance = new ExpenseListAdapter(context);
        }
        return instance;
    }

    private ExpenseListAdapter(Context context) {
        expenseRepository = ExpenseRepository.getInstance(ExpenseDataSource.getInstance(context));
        mItem = expenseRepository.getAllExpense();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return ThreeLineItemViewHolder.create(parent);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        bind((ThreeLineItemViewHolder) viewHolder, mItem.get(position));

        //hinder list clicking action
        viewHolder.itemView.setOnTouchListener((view, motionEvent) -> {
            view.onTouchEvent(motionEvent);
            return true;
        });
}

    public void addExpense(Expense expense) {
        //TODO add expense based on createdAt
        mItem.add(0, expense);
        notifyItemInserted(0);
    }

    private void bind(ThreeLineItemViewHolder vh, Expense expense) {
        vh.text.setText(String.format(expense.getAmount().toString()));
        vh.secondary.setText(expense.getCategory() + ", " + expense.getSubCategory());
        vh.tertiary.setText(expense.getCreatedAt().toString());
        if(CategoryType.INCOME.name().equals(expense.getType())) {
            vh.icon.setImageResource(R.drawable.revenue);
        } else {
            vh.icon.setImageResource(R.drawable.earning);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

}
