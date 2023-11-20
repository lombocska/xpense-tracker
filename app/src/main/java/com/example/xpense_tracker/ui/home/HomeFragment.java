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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpense_tracker.R;
import com.example.xpense_tracker.databinding.FragmentHomeBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView transactionsListView =
                (RecyclerView) inflater.inflate(R.layout.fragment_transaction_list, container, false);

        transactionsListView.setLayoutManager(new LinearLayoutManager(getContext()));
        ExpenseListAdapter adapter = ExpenseListAdapter.getInstance(getContext());
        transactionsListView.setAdapter(adapter);
        binding.transactionsMaterialCardView.addView(transactionsListView);

        FloatingActionButton addExpenseButton = root.findViewById(R.id.floatingActionButton);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddExpenseOrIncomeDialogFragment.newInstance().show(getChildFragmentManager(), "addExpenseOrIncomeDialog");
            }
        });

        binding.incomeOrExpenseChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                Chip chip = (Chip) group.findViewById(checkedIds.get(0));//single selection true -> 1 item in checkedIds
                adapter.filter(chip.getText().toString());
            }
        });

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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}