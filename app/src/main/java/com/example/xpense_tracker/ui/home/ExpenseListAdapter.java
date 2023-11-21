package com.example.xpense_tracker.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpense_tracker.R;
import com.example.xpense_tracker.data.ExpenseDataSource;
import com.example.xpense_tracker.data.ExpenseRepository;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.data.model.Expense;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Adapter that shows Single, Two, and Three line list items
 * https://github.com/material-components/material-components-android/blob/master/catalog/java/io/material/catalog/lists/ListsMainDemoFragment.java
 * https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/lists/res/layout/material_list_item_three_line.xml
 */
public class ExpenseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

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
        List<Expense> allExpense = getAllExpense();
        mItem = allExpense;
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
        mItem.add(0, expense);
        mItem.sort(sortComparatorByDate());
        notifyDataSetChanged();
    }


    public void filter(String filterCategoryName) {
        List<Expense> allExpense = getAllExpense();
        if ("ALL".equals(filterCategoryName)) {
            mItem = allExpense;
            notifyDataSetChanged();
            return;
        }

        mItem = allExpense.stream()
                .filter(e -> e.getType().equals(filterCategoryName))
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    private void bind(ThreeLineItemViewHolder vh, Expense expense) {
        vh.text.setText(String.format(expense.getAmount().toString()));
        vh.secondary.setText(String.format("%s, %s, %s", expense.getCategory(), expense.getSubCategory(), expense.getNote()));
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

    public void filter(LocalDate from, LocalDate to) {
        mItem = getAllExpense().stream().filter(e -> !e.getCreatedAt().isBefore(from) && !e.getCreatedAt().isAfter(to)).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    private List<Expense> getAllExpense() {
        List<Expense> allExpense = expenseRepository.getAllExpense();
        allExpense.sort(sortComparatorByDate());
        return allExpense;
    }

    @NonNull
    public static Comparator<Expense> sortComparatorByDate() {
        return new Comparator<Expense>() {
            @Override
            public int compare(Expense o1, Expense o2) {
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        };
    }
}
