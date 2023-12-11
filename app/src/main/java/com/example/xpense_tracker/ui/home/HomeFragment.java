package com.example.xpense_tracker.ui.home;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpense_tracker.R;
import com.example.xpense_tracker.data.Currency;
import com.example.xpense_tracker.data.ExpenseDataSource;
import com.example.xpense_tracker.data.ExpenseRepository;
import com.example.xpense_tracker.data.SharedPreferenceService;
import com.example.xpense_tracker.databinding.FragmentHomeBinding;
import com.example.xpense_tracker.ui.UIUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ExpenseListAdapter adapter;
    private ExpenseRepository expenseRepository;
    private SharedPreferenceService sharedPreferenceService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.adapter = ExpenseListAdapter.getInstance(getContext());
        this.expenseRepository = ExpenseRepository.getInstance(ExpenseDataSource.getInstance(getContext()));
        this.sharedPreferenceService = SharedPreferenceService.getInstance(getContext());
        fillHomeWithExpenses(inflater, container);
        setCurrencySettings();
        setMonthlyState();
        addAddButtonListener(root);
        addCategoryTypeFilterListener();
        addDateRangeFilterListener();
        registerAdapterOnChangeListener();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void registerAdapterOnChangeListener() {
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            //showing correct value in case of filtering
            @Override
            public void onChanged() {
                super.onChanged();
                setMonthlyState();
            }

            //showing correct value in case of removing expenses
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onChanged();
                setMonthlyState();
            }
        });
    }

    private void setMonthlyState() {
        MaterialTextView monthlyIncome = binding.monthlyIncome;
        MaterialTextView monthlyExpense = binding.monthlyExpense;
        Pair<Integer, Integer> allFromCurrentMonth = expenseRepository.getAllFromCurrentMonth();
        monthlyIncome.setText(sharedPreferenceService.applySelectedCurrency(allFromCurrentMonth.first).toString());
        monthlyExpense.setText(sharedPreferenceService.applySelectedCurrency(allFromCurrentMonth.second).toString());
    }

    private void setCurrencySettings() {
        binding.currency.setText("[" + sharedPreferenceService.getCurrency() + "]");
    }

    private void addDateRangeFilterListener() {
        binding.filterChipDateRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Pair<Long, Long>> dateRangePicker = MaterialDatePicker.Builder
                        .dateRangePicker()
                        .setTitleText(R.string.date_range_picker_title)
                        .build();
                dateRangePicker.show(getChildFragmentManager(), "filter date range");
                dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        LocalDate from = LocalDate.parse(DateFormat.format("yyyy-MM-dd", new Date(selection.first)).toString());
                        LocalDate to = LocalDate.parse(DateFormat.format("yyyy-MM-dd", new Date(selection.second)).toString());
                        adapter.filter(from, to);
                    }
                });
                Toast.makeText(getContext(), "Calling Date range picker", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addCategoryTypeFilterListener() {
        binding.incomeOrExpenseChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                Chip chip = (Chip) group.findViewById(checkedIds.get(0));//single selection true -> 1 item in checkedIds
                adapter.filter(chip.getText().toString());
            }
        });
    }

    private void addAddButtonListener(View root) {
        FloatingActionButton addExpenseButton = root.findViewById(R.id.floatingActionButton);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddExpenseOrIncomeDialogFragment.newInstance().show(getChildFragmentManager(), "addExpenseOrIncomeDialog");
            }
        });
    }

    private void fillHomeWithExpenses(@NonNull LayoutInflater inflater, ViewGroup container) {
        RecyclerView transactionsListView =
                (RecyclerView) inflater.inflate(R.layout.fragment_transaction_list, container, false);

        adapter.addSwipeListener(transactionsListView);
        transactionsListView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionsListView.setAdapter(adapter);
        binding.transactionsMaterialCardView.addView(transactionsListView);
    }

}