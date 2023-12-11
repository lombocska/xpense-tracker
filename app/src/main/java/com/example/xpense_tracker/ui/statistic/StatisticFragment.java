package com.example.xpense_tracker.ui.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.xpense_tracker.data.ExpenseDataSource;
import com.example.xpense_tracker.data.ExpenseRepository;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.databinding.FragmentStatisticBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StatisticFragment extends Fragment {

    public static final String INCOME = CategoryType.INCOME.name();
    public static final String EXPENSE = CategoryType.EXPENSE.name();
    private FragmentStatisticBinding binding;
    private ExpenseRepository expenseRepository;
    private PieChart incomePieChart;
    private PieChart expensePieChart;
    private BarChart last6MonthBarChart;
    private BarChart lastMonthBarChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        expenseRepository = ExpenseRepository.getInstance(ExpenseDataSource.getInstance(getContext()));

        initialize();

        ExpensePieChart.showPieChart(incomePieChart, getIncomeAmountPerCategoryType(), INCOME);
        ExpensePieChart.showPieChart(expensePieChart, getExpenseAmountPerCategoryType(), EXPENSE);
        GroupedBarChart.showLastSixMonthBarChart(last6MonthBarChart, getLast6MonthIncome(), getLast6MonthExpense());
        GroupedBarChart.showLastMonthBarChart(lastMonthBarChart, getLastMonthIncome(), getLastMonthExpense());
        return binding.getRoot();
    }

    private void initialize() {
        incomePieChart = binding.incomePieChart;
        expensePieChart = binding.expensePeChart;
        last6MonthBarChart = binding.last6MonthBarChart;
        lastMonthBarChart = binding.lastMonthBarChart;
    }

    private List<Integer> getLast6MonthExpense() {
        return expenseRepository.getLast6MonthExpensesByMonth();
    }

    private List<Integer> getLast6MonthIncome() {
        return expenseRepository.getLast6MonthIncomesByMonth();
    }

    private List<Integer> getLastMonthIncome() {
        return expenseRepository.getCurrentMonthIncomesDayByDay();
    }

    private List<Integer> getLastMonthExpense() {
        return expenseRepository.getCurrentMonthExpensesDayByDay();
    }

    private Map<String, Integer> getIncomeAmountPerCategoryType() {
        return expenseRepository.getExpensesByCategoryType(INCOME);
    }

    private Map<String, Integer> getExpenseAmountPerCategoryType() {
        return expenseRepository.getExpensesByCategoryType(EXPENSE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class GroupedBarChart {

        //source: https://medium.com/@clyeung0714/using-mpandroidchart-for-android-application-barchart-540a55b4b9ef
        private static void showLastSixMonthBarChart(BarChart expenseBarChartByMonth,
                                                     List<Integer> incomeByMonth,
                                                     List<Integer> expenseByMonth) {
            List<BarEntry> incomesFor6Month = IntStream.range(0, incomeByMonth.size())
                    .mapToObj(i -> new BarEntry(i, incomeByMonth.get(i).floatValue()))
                    .collect(Collectors.toList());

            List<BarEntry> expensesFor6Month = IntStream.range(0, expenseByMonth.size())
                    .mapToObj(i -> new BarEntry(i, expenseByMonth.get(i).floatValue()))
                    .collect(Collectors.toList());
            String incomeTitle = "Income";
            String expenseTitle = "Expense";

            //grouped barchart
            BarDataSet barDataSet = new BarDataSet(incomesFor6Month, incomeTitle);
            barDataSet.setColors(Color.parseColor("#7b1113"));
            BarDataSet barDataSet2 = new BarDataSet(expensesFor6Month, expenseTitle);
            barDataSet2.setColors(Color.parseColor("#6E8B3D"));

            float groupSpace = 0.06f;
            float barSpace = 0.02f; // x2 dataset
            float barWidth = 0.45f; // x2 dataset
            // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
            BarData data = new BarData(barDataSet, barDataSet2);
            expenseBarChartByMonth.setData(data);
            data.setBarWidth(barWidth); // set the width of each bar
            expenseBarChartByMonth.groupBars(0f, groupSpace, barSpace); // perform the "explicit" grouping

            expenseBarChartByMonth.getXAxis().setValueFormatter(new IndexAxisValueFormatter(getLastSixMonthShortName()));
            Description desc = new Description();
            desc.setText("Last 6 month incomes and expenses.");
            expenseBarChartByMonth.setDescription(desc);

            expenseBarChartByMonth.invalidate();
        }

        private static void showLastMonthBarChart(BarChart expenseBarChartByMonth,
                                                  List<Integer> incomeOfLastMonthDays,
                                                  List<Integer> expenseOfLastMonthDays) {
            List<BarEntry> incomesForMonth = IntStream.range(0, incomeOfLastMonthDays.size())
                    .mapToObj(i -> new BarEntry(i+1, incomeOfLastMonthDays.get(i).floatValue()))
                    .collect(Collectors.toList());

            List<BarEntry> expensesForMonth = IntStream.range(0, expenseOfLastMonthDays.size())
                    .mapToObj(i -> new BarEntry(i+1, expenseOfLastMonthDays.get(i).floatValue()))
                    .collect(Collectors.toList());
            String incomeTitle = "Income";
            String expenseTitle = "Expense";

            //grouped barchart
            BarDataSet barDataSet = new BarDataSet(incomesForMonth, incomeTitle);
            barDataSet.setColors(Color.parseColor("#7b1113"));
            BarDataSet barDataSet2 = new BarDataSet(expensesForMonth, expenseTitle);
            barDataSet2.setColors(Color.parseColor("#6E8B3D"));

            float groupSpace = 0.06f;
            float barSpace = 0.02f; // x2 dataset
            float barWidth = 0.45f; // x2 dataset
            // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
            BarData data = new BarData(barDataSet, barDataSet2);
            expenseBarChartByMonth.setData(data);
            data.setBarWidth(barWidth); // set the width of each bar
            expenseBarChartByMonth.groupBars(0f, groupSpace, barSpace); // perform the "explicit" grouping
            Description desc = new Description();
            desc.setText("Incomes and expenses of the current month.");
            expenseBarChartByMonth.setDescription(desc);

            expenseBarChartByMonth.invalidate();
        }

        private static String[] getLastSixMonthShortName() {
            String[] xAxisLabels = new String[6];
            for (int i = 5; i >= 0; i--) {
                YearMonth date = YearMonth.now().minusMonths(i);
                String monthName = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                xAxisLabels[5-i] = monthName;
            }
            return xAxisLabels;
        }

    }

        private static class ExpensePieChart {

        //Fake PieChart data for demo purpose
        //credit: https://medium.com/@clyeung0714/using-mpandroidchart-for-android-application-piechart-123d62d4ddc0
        private static void showPieChart(PieChart pieChart, Map<String, Integer> typeAmountMap, String categoryType) {
            PieData pieData = createPieData(typeAmountMap, categoryType);
            customizePieChartAppearance(pieChart, pieData);
        }

        private static void customizePieChartAppearance(PieChart pieChart, PieData pieData) {
            //using percentage as values instead of amount
            pieChart.setUsePercentValues(true);
            //remove the description label on the lower left corner, default true if not set
            pieChart.getDescription().setEnabled(false);
            //enabling the user to rotate the chart, default true
            pieChart.setRotationEnabled(false);
            //adding friction when rotating the pie chart
            pieChart.setDragDecelerationFrictionCoef(0.9f);
            //setting the first entry start from right hand side, default starting from top
            pieChart.setRotationAngle(0);
            //highlight the entry when it is tapped, default true if not set
            pieChart.setHighlightPerTapEnabled(true);

            pieChart.setData(pieData);
            pieChart.invalidate();
            pieChart.getLegend().setEnabled(false);
            pieChart.getDescription().setEnabled(false);
        }

        @NonNull
        private static PieData createPieData(Map<String, Integer> typeAmountMap, String categoryType) {
            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            String label = "type";

            //input data and fit data into pie chart entry
            for (String type : typeAmountMap.keySet()) {
                pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
            }

            //collecting the entries with label name
            PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
            //setting text size of the value
            pieDataSet.setValueTextSize(12f);
            //providing color list for coloring different entries
            pieDataSet.setColors(getColors(categoryType));
            //grouping the data set from entry to chart
            PieData pieData = new PieData(pieDataSet);
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(true);
            return pieData;
        }

        @NonNull
        private static ArrayList<Integer> getColors(String categoryType) {
            ArrayList<Integer> colors = new ArrayList<>();
            if (CategoryType.INCOME.name().equals(categoryType)) {
                colors.add(Color.parseColor("#7b1113"));
                colors.add(Color.parseColor("#560C0E"));
                colors.add(Color.parseColor("#FFC8CA"));
                colors.add(Color.parseColor("#FF9194"));
                colors.add(Color.parseColor("#CC5500"));
                colors.add(Color.parseColor("#8f4600"));
                colors.add(Color.parseColor("#ffbe80"));
            } else {
                colors.add(Color.parseColor("#6E8B3D"));
                colors.add(Color.parseColor("#51612B"));
                colors.add(Color.parseColor("#F4FFDB"));
                colors.add(Color.parseColor("#EAFFB7"));
                colors.add(Color.parseColor("#008080"));
                colors.add(Color.parseColor("#008080"));
                colors.add(Color.parseColor("#00807E"));
            }
            return colors;
        }
    }
}