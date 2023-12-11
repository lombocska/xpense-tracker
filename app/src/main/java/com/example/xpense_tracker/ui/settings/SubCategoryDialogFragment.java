package com.example.xpense_tracker.ui.settings;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.DEFAULT_CATEGORY;

import android.app.Dialog;
import android.content.DialogInterface;
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
import com.example.xpense_tracker.databinding.FragmentSubcategoryDialogListDialogBinding;
import com.example.xpense_tracker.ui.UIUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubCategoryDialogFragment extends BottomSheetDialogFragment {

    private FragmentSubcategoryDialogListDialogBinding binding;
    private ChipGroup incomeOrExpenseChipGroup;
    private ChipGroup categoriesChipGroup;
    private TextInputEditText subcategoryNameText;
    private ExtendedFloatingActionButton addSubcategoryButton;
    private Chip incomeChip;
    private Chip expenseChip;
    private CategoryRepository categoryRepository;

    private List<Category> incomeCategories = new ArrayList<>();
    private List<Category> expenseCategories = new ArrayList<>();

    private SubCategoryDialogFragment() {
    }

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
        addIncomeListener(getIncomeCategoryChips());
        addExpenseListener(getExpenseCategoryChips());
        addSubCategoryAddButtonListener();

        this.incomeChip.callOnClick();
        this.incomeChip.setChecked(true);
        return binding.getRoot();
    }

    //dialog opens fully aligning with all of its view items
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        SettingsFragment parentFragment = (SettingsFragment )getParentFragment();
        parentFragment.showExistingCategories();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.getDialog().getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initialize() {
        this.incomeOrExpenseChipGroup = binding.incomeOrExpenseChipGroup;
        this.categoriesChipGroup = binding.categoriesChipGroup;
        this.incomeChip = binding.incomeChip;
        this.expenseChip = binding.expenseChip;
        this.subcategoryNameText = binding.subcategoryNameText;
        this.addSubcategoryButton = binding.addFabButton;
    }

    private void addIncomeListener(List<Chip> categoryChips) {
        incomeChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDialogBelowMainChips();
                categoryChips.get(0).setChecked(true);
                categoryChips.forEach(categoriesChipGroup::addView);
            }
        });
    }

    private void addExpenseListener(List<Chip> categoryChips) {
        expenseChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDialogBelowMainChips();
                categoryChips.get(0).setChecked(true);
                categoryChips.forEach(categoriesChipGroup::addView);
            }
        });
    }

    private Chip getCategoryChip(Category category) {
        Chip categoryChip = UIUtil.createFilterChip(getLayoutInflater(),category.getId(), category.getName());
        return categoryChip;
    }

    private List<Chip> getIncomeCategoryChips() {
        this.incomeCategories = categoryRepository.getCategories(CategoryType.INCOME);
        return getCategoryChipsWithSubCategoryChips(incomeCategories);
    }

    private List<Chip> getExpenseCategoryChips() {
        this.expenseCategories = categoryRepository.getCategories(CategoryType.EXPENSE);
        return getCategoryChipsWithSubCategoryChips(expenseCategories);
    }

    private void clearDialogBelowMainChips() {
        categoriesChipGroup.removeAllViews();
    }

    @NonNull
    private List<Chip> getCategoryChipsWithSubCategoryChips(List<Category> categories) {
        return categories.stream()
                .map(this::getCategoryChip)
                .collect(Collectors.toList());
    }

    private void addSubCategoryAddButtonListener() {
        this.addSubcategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subcategoryName = subcategoryNameText.getText().toString();
                if (subcategoryName.trim().isEmpty()) {
                    return;
                }
                int checkedChipId = incomeOrExpenseChipGroup.getCheckedChipId();
                Chip selectedChip = (Chip) incomeOrExpenseChipGroup.findViewById(checkedChipId);
                String categoryType = selectedChip.getText().toString();

                int checkedCategoryChipId = categoriesChipGroup.getCheckedChipId();
                String selectedCategory =
                        Optional.ofNullable((Chip) categoriesChipGroup.findViewById(checkedCategoryChipId))
                        .orElse(UIUtil.createFilterChip(getLayoutInflater(),0, DEFAULT_CATEGORY))
                                .getText().toString();

                categoryRepository.saveSubCategory(Map.of(selectedCategory, List.of(subcategoryName)), CategoryType.valueOf(categoryType));
                dismiss();
            }
        });
    }

}