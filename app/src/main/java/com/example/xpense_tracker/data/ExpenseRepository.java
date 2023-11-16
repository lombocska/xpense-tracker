package com.example.xpense_tracker.data;

import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.Expense;
import com.example.xpense_tracker.data.model.SubCategory;

import java.util.List;

public class ExpenseRepository {

    private static volatile ExpenseRepository instance;
    private ExpenseDataSource dataSource;

    private ExpenseRepository(ExpenseDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ExpenseRepository getInstance(ExpenseDataSource dataSource) {
        if (instance == null) {
            instance = new ExpenseRepository(dataSource);
        }
        return instance;
    }

    public List<Expense> getAllExpense() {
        return dataSource.getAllExpense();
    }

    public Expense addExpense(Category category, SubCategory subCategory, String amount) {
        return dataSource.createExpense(category, subCategory, amount);
    }
}
