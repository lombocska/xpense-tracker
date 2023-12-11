package com.example.xpense_tracker.data;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.data.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
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

    public List<Integer> getLast6MonthExpensesByMonth() {
        LocalDate now = LocalDate.now().withDayOfMonth(1);
        LocalDate from = now.minusMonths(5);
        LocalDate currentMonth = from;
        Map<Month, Integer> sumIncomesPerMonths = getAllSummingByMonth(from, CategoryType.EXPENSE);

        List<Integer> incomesMonthByMonth = new ArrayList<>();
        while (currentMonth.isBefore(now) || currentMonth.equals(now)) {
            if (sumIncomesPerMonths.containsKey(currentMonth.getMonth())) {
                incomesMonthByMonth.add(sumIncomesPerMonths.get(currentMonth.getMonth()));
            } else {
                incomesMonthByMonth.add(BigDecimal.ZERO.intValue());
            }

            currentMonth = currentMonth.plusMonths(1);
        }

        return incomesMonthByMonth;
    }

    public List<Integer> getLast6MonthIncomesByMonth() {
        LocalDate now = LocalDate.now().withDayOfMonth(1);
        LocalDate from = now.minusMonths(5);
        LocalDate currentMonth = from;
        Map<Month, Integer> sumIncomesPerMonths = getAllSummingByMonth(from, CategoryType.INCOME);

        List<Integer> incomesMonthByMonth = new ArrayList<>();
        while (currentMonth.isBefore(now) || currentMonth.equals(now)) {
            if (sumIncomesPerMonths.containsKey(currentMonth.getMonth())) {
                incomesMonthByMonth.add(sumIncomesPerMonths.get(currentMonth.getMonth()));
            } else {
                incomesMonthByMonth.add(BigDecimal.ZERO.intValue());
            }

            currentMonth = currentMonth.plusMonths(1);
        }

        return incomesMonthByMonth;
    }

    public List<Integer> getCurrentMonthIncomesDayByDay() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = start.plusMonths(1);

        Map<LocalDate, Integer> sumIncomesPerDays = getAllSummingByDay(firstDayOfMonth,CategoryType.INCOME);

        List<Integer> incomesDayByDay = new ArrayList<>();
        while (start.isBefore(end)) {
            if (sumIncomesPerDays.containsKey(start)) {
                incomesDayByDay.add(sumIncomesPerDays.get(start));
            } else {
                incomesDayByDay.add(BigDecimal.ZERO.intValue());
            }

            start = start.plusDays(1);
        }

        return incomesDayByDay;
    }

    public List<Integer> getCurrentMonthExpensesDayByDay() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = start.plusMonths(1);

        Map<LocalDate, Integer> sumExpensesPerDays = getAllSummingByDay(firstDayOfMonth,CategoryType.EXPENSE);

        List<Integer> expensesDayByDay = new ArrayList<>();
        while (start.isBefore(end)) {
            if (sumExpensesPerDays.containsKey(start)) {
                expensesDayByDay.add(sumExpensesPerDays.get(start));
            } else {
                expensesDayByDay.add(BigDecimal.ZERO.intValue());
            }

            start = start.plusDays(1);
        }

        return expensesDayByDay;
    }

    @NonNull
    private Map<LocalDate, Integer> getAllSummingByDay(LocalDate from, CategoryType type) {
        return dataSource.getAll(from)
                .stream()
                .filter(e -> type.name().equals(e.getType()))
                .collect(groupingBy(Expense::getCreatedAt,
                        summingInt(e -> new BigDecimal(e.getAmount()).intValue())));
    }

    @NonNull
    private Map<Month, Integer> getAllSummingByMonth(LocalDate from, CategoryType type) {
        return dataSource.getAll(from)
                .stream()
                .filter(e -> type.name().equals(e.getType()))
                .collect(groupingBy(e -> e.getCreatedAt().getMonth(),
                        summingInt(e -> new BigDecimal(e.getAmount()).intValue())));
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

    public void deleteExpense(int id) {
        dataSource.delete(id);
    }
}
