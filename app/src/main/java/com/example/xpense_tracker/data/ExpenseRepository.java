package com.example.xpense_tracker.data;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import androidx.core.util.Pair;

import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.data.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public void addExpense(Expense expense) {
        dataSource.createExpense(expense);
    }

    public Map<String, Integer> getExpensesByCategoryType(String name) {
        return getAllExpense().stream()
                .filter(e -> name.equals(e.getType()))
                .collect(groupingBy(Expense::getCategory, summingInt(e -> new BigDecimal(e.getAmount()).intValue())));
    }

    public List<Expense> getExpenses() {
        return dataSource.getExpenses(CategoryType.EXPENSE);
    }

    public List<Expense> getIncomes() {
        return dataSource.getExpenses(CategoryType.INCOME);
    }

    public Pair<Integer, Integer> getAllFromCurrentMonth() {
        List<Expense> allOfCurrentMonth = dataSource.getAllExpense()
                .stream()
                .filter(e -> LocalDate.now().getMonth().equals(e.getCreatedAt().getMonth()))
                .collect(Collectors.toList());
        int allIncomesOfCurrentMonth = allOfCurrentMonth.stream()
                .filter(e -> CategoryType.INCOME.name().equals(e.getType()))
                .mapToInt(e -> new BigDecimal(e.getAmount()).intValue())
                .sum();
        int allExpensesOfCurrentMonth = allOfCurrentMonth.stream()
                .filter(e -> CategoryType.EXPENSE.name().equals(e.getType()))
                .mapToInt(e -> new BigDecimal(e.getAmount()).intValue())
                .sum();
        return Pair.create(allIncomesOfCurrentMonth, allExpensesOfCurrentMonth);
    }

}
