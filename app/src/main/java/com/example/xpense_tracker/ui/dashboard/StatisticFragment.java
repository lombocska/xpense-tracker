package com.example.xpense_tracker.ui.dashboard;

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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Map;

public class StatisticFragment extends Fragment {

    public static final String INCOME = CategoryType.INCOME.name();
    public static final String EXPENSE = CategoryType.EXPENSE.name();
    private FragmentStatisticBinding binding;
    private ExpenseRepository expenseRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //issue is here, that ExpenseDataSource constructor drops table and initialize it again
        expenseRepository = ExpenseRepository.getInstance(ExpenseDataSource.getInstance(getContext()));

        PieChart incomePieChart = binding.incomePieChart;
        PieChart expensePieChart = binding.expensePeChart;

        ExpensePieChart.showPieChart(incomePieChart, getIncomeAmountPerCategoryType(), INCOME);
        ExpensePieChart.showPieChart(expensePieChart, getExpenseAmountPerCategoryType(), EXPENSE);
        return root;
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

    private static class ExpensePieChart {

        //    //Fake PieChart data for demo purpose
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