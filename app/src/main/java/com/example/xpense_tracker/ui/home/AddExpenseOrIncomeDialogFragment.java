package com.example.xpense_tracker.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.xpense_tracker.data.CategoryDataSource;
import com.example.xpense_tracker.data.CategoryRepository;
import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.databinding.FragmentAddExpenseOrIncomeDialogListDialogBinding;
import com.example.xpense_tracker.databinding.FragmentFilterChipBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     AddExpenseOrIncomeDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class AddExpenseOrIncomeDialogFragment extends BottomSheetDialogFragment {

    private FragmentAddExpenseOrIncomeDialogListDialogBinding binding;
    private CategoryRepository categoryRepository;

    public static AddExpenseOrIncomeDialogFragment newInstance() {
        return new AddExpenseOrIncomeDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddExpenseOrIncomeDialogListDialogBinding.inflate(inflater, container, false);
        categoryRepository = CategoryRepository.getInstance(new CategoryDataSource(getContext()));

        List<Chip> incomeCategoryChips = createIncomeCategories();
        List<Chip> expenseCategoryChips = createExpenseCategories();
        binding.incomeChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.incomeOrExpenseCategoriesChipGroup.removeAllViews();
                incomeCategoryChips.forEach(chip -> binding.incomeOrExpenseCategoriesChipGroup.addView(chip));
            }
        });
        binding.expenseChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.incomeOrExpenseCategoriesChipGroup.removeAllViews();
                expenseCategoryChips.forEach(chip -> binding.incomeOrExpenseCategoriesChipGroup.addView(chip));
            }
        });
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<Chip> createIncomeCategories() {
        List<Category> incomeCategories = categoryRepository.getCategories(CategoryType.INCOME);
        return incomeCategories.stream().map(category -> createFilterChip(category.getId(), category.getName())).collect(Collectors.toList());
    }

    private List<Chip> createExpenseCategories() {
        List<Category> expenseCategories = categoryRepository.getCategories(CategoryType.EXPENSE);
        return expenseCategories.stream().map(category -> createFilterChip(category.getId(), category.getName())).collect(Collectors.toList());
    }

    private Chip createFilterChip(int id, String name) {
        Chip chip = FragmentFilterChipBinding.inflate(getLayoutInflater()).getRoot();
        chip.setId(id);
        chip.setText(name);
        return chip;
    }
}