package com.example.xpense_tracker.data;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.Expense;
import com.example.xpense_tracker.data.model.SubCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    public void addExpense(Expense expense) {
        dataSource.createExpense(expense);
    }

    public Map<String, Integer> getExpensesByCategoryType(String name) {
        return getAllExpense().stream()
                .filter(e -> name.equals(e.getType()))
                .collect(groupingBy(Expense::getCategory, summingInt(e -> new BigDecimal(e.getAmount()).intValue())));
    }
}
