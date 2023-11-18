package com.example.xpense_tracker.data;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingInt;
import static java.util.stream.Collectors.summingInt;

import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.data.model.Expense;
import com.example.xpense_tracker.data.model.SubCategory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<String, Integer> getExpensesByCategoryType(String name) {
        return getAllExpense().stream()
                .filter(e -> name.equals(e.getType()))
                .collect(groupingBy(Expense::getCategory, summingInt(e -> Integer.parseInt(e.getAmount()))));
    }
}
