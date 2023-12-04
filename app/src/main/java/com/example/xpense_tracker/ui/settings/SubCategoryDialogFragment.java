package com.example.xpense_tracker.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xpense_tracker.data.CategoryDataSource;
import com.example.xpense_tracker.data.CategoryRepository;
import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.databinding.FragmentFilterChipBinding;
import com.example.xpense_tracker.databinding.FragmentSubcategoryDialogListDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     CategoryDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class SubCategoryDialogFragment extends BottomSheetDialogFragment {

    private FragmentSubcategoryDialogListDialogBinding binding;
    private ChipGroup incomeOrExpenseChipGroup;
    private ChipGroup incomeOrExpenseCategoriesChipGroup;
    private TextInputEditText subcategoryNameText;
    private ExtendedFloatingActionButton addButton;
    private CategoryRepository categoryRepository;
    private Chip incomeChip;
    private Chip expenseChip;
    private List<Category> incomeCategories = new ArrayList<>();
    private List<Category> expenseCategories = new ArrayList<>();
    public static SubCategoryDialogFragment newInstance() {
        return new SubCategoryDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSubcategoryDialogListDialogBinding.inflate(inflater, container, false);
        this.categoryRepository = CategoryRepository.getInstance(CategoryDataSource.getInstance(getContext()));

        initialize();
        List<Chip> incomeCategoryChips = getIncomeCategoryChips();
        List<Chip> expenseCategoryChips = getExpenseCategoryChips();

        addIncomeListener(incomeCategoryChips);
        addExpenseListener(expenseCategoryChips);
        addAddButtonListener();

        initialSetup();
        return binding.getRoot();
    }

    private void initialSetup() {
        incomeChip.callOnClick();
        incomeChip.setChecked(true);
        ((Chip) incomeOrExpenseCategoriesChipGroup.getChildAt(0)).setChecked(true);
    }

    private void addIncomeListener(List<Chip> categoryChips) {
        incomeChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDialogBelowMainChips();
                categoryChips.forEach(incomeOrExpenseCategoriesChipGroup::addView);
            }
        });
    }

    private void addExpenseListener(List<Chip> categoryChips) {
        expenseChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDialogBelowMainChips();
                categoryChips.forEach(incomeOrExpenseCategoriesChipGroup::addView);
            }
        });
    }

    private void addAddButtonListener() {
        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subcategoryName = subcategoryNameText.getText().toString();
                if (subcategoryName == null || subcategoryName.trim().isEmpty()) {
                    return;
                }
                int checkedChipId = incomeOrExpenseChipGroup.getCheckedChipId();
                Chip selectedChip = (Chip) incomeOrExpenseChipGroup.findViewById(checkedChipId);
                int checkedCategoryChipId = incomeOrExpenseCategoriesChipGroup.getCheckedChipId();
                Chip selectedCategoryChip = (Chip) incomeOrExpenseCategoriesChipGroup.findViewById(checkedCategoryChipId);

                String selectedCategoryName = selectedCategoryChip.getText().toString();
                String categoryType = selectedChip.getText().toString();

                categoryRepository.saveSubCategory(Map.of(selectedCategoryName, List.of(subcategoryName)), CategoryType.valueOf(categoryType));
                dismiss();
            }
        });
    }

    private void initialize() {
        this.incomeOrExpenseChipGroup = binding.incomeOrExpenseChipGroup;
        this.incomeOrExpenseCategoriesChipGroup = binding.incomeOrExpenseCategoriesChipGroup;
        this.subcategoryNameText = binding.subcategoryNameText;
        this.addButton = binding.addFabButton;
        this.incomeChip = binding.incomeChip;
        this.expenseChip = binding.expenseChip;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<Chip> getIncomeCategoryChips() {
        this.incomeCategories = categoryRepository.getCategories(CategoryType.INCOME);
        return getCategoryChipsWithSubCategoryChips(incomeCategories);
    }

    private List<Chip> getExpenseCategoryChips() {
        this.expenseCategories = categoryRepository.getCategories(CategoryType.EXPENSE);
        return getCategoryChipsWithSubCategoryChips(expenseCategories);
    }

    @NonNull
    private List<Chip> getCategoryChipsWithSubCategoryChips(List<Category> categories) {
        return categories.stream()
                .map(this::getCategoryChip)
                .collect(Collectors.toList());
    }

    private Chip getCategoryChip(Category category) {
        Chip categoryChip = createFilterChip(category.getId(), category.getName());
        return categoryChip;
    }

    private Chip createFilterChip(int id, String name) {
        Chip chip = FragmentFilterChipBinding.inflate(getLayoutInflater()).getRoot();
        chip.setId(id);
        chip.setText(name);
        return chip;
    }
    private void clearDialogBelowMainChips() {
        incomeOrExpenseCategoriesChipGroup.removeAllViews();
    }
}