package com.example.xpense_tracker.ui.home;

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
import com.example.xpense_tracker.data.model.SubCategory;
import com.example.xpense_tracker.databinding.FragmentAddExpenseOrIncomeDialogListDialogBinding;
import com.example.xpense_tracker.databinding.FragmentFilterChipBinding;
import com.example.xpense_tracker.databinding.FragmentMaterialDividerBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.divider.MaterialDivider;

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
        ChipGroup incomeOrExpenseCategoriesChipGroup = binding.incomeOrExpenseCategoriesChipGroup;
        ChipGroup incomeOrExpenseSubCategoriesChipGroup = binding.incomeOrExpenseSubCategoriesChipGroup;
        binding.incomeChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeOrExpenseCategoriesChipGroup.removeAllViews();
                incomeOrExpenseSubCategoriesChipGroup.removeAllViews();

                incomeCategoryChips.forEach(incomeOrExpenseCategoriesChipGroup::addView);
            }
        });
        binding.expenseChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeOrExpenseCategoriesChipGroup.removeAllViews();
                incomeOrExpenseSubCategoriesChipGroup.removeAllViews();

                expenseCategoryChips.forEach(incomeOrExpenseCategoriesChipGroup::addView);
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
        return getCategoryChipsWithSubCategoryChips(incomeCategories);
    }

    @NonNull
    private List<Chip> getCategoryChipsWithSubCategoryChips(List<Category> incomeCategories) {
        return incomeCategories.stream()
                .map(category -> {
                    Chip categoryFilterChip = createFilterChip(category.getId(), category.getName());
                    categoryFilterChip.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            List<SubCategory> subCategories = categoryRepository.getSubCategories(category.getId());
                            List<Chip> subCategoryChips = subCategories.stream()
                                    .map(subCategory -> createFilterChip(subCategory.getId(), subCategory.getName()))
                                    .collect(Collectors.toList());

                            ChipGroup subCategoryChipGroup = binding.incomeOrExpenseSubCategoriesChipGroup;
                            subCategoryChipGroup.removeAllViews();
                            subCategoryChipGroup.addView(createMaterialDivider());
                            subCategoryChips.forEach(subCategoryChipGroup::addView);
                        }
                    });

                    return categoryFilterChip;
                })
                .collect(Collectors.toList());
    }

    private List<Chip> createExpenseCategories() {
        List<Category> expenseCategories = categoryRepository.getCategories(CategoryType.EXPENSE);
        return getCategoryChipsWithSubCategoryChips(expenseCategories);
    }

    private Chip createFilterChip(int id, String name) {
        Chip chip = FragmentFilterChipBinding.inflate(getLayoutInflater()).getRoot();
        chip.setId(id);
        chip.setText(name);
        return chip;
    }

    private MaterialDivider createMaterialDivider() {
        return FragmentMaterialDividerBinding.inflate(getLayoutInflater()).getRoot();
    }

}