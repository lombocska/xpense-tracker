package com.example.xpense_tracker.ui.settings;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.DEFAULT_CATEGORY;
import static com.example.xpense_tracker.data.model.CategoryContract.SubCategoryContent.DEFAULT_SUBCATEGORY;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xpense_tracker.R;
import com.example.xpense_tracker.data.CategoryDataSource;
import com.example.xpense_tracker.data.CategoryRepository;
import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.databinding.FragmentCategoryDialogListDialogBinding;
import com.example.xpense_tracker.ui.UIUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDialogFragment extends BottomSheetDialogFragment {

    private Category category;
    private boolean isUpdate = false;
    private FragmentCategoryDialogListDialogBinding binding;
    private ChipGroup incomeOrExpenseChipGroup;
    private ChipGroup subCategoriesChipGroup;
    private TextInputEditText categoryNameText;
    private ExtendedFloatingActionButton addCategoryButton;
    private Chip incomeChip;
    private Chip expenseChip;
    private CategoryRepository categoryRepository;
    private ExtendedFloatingActionButton deleteCatButton;

    private ExtendedFloatingActionButton deleteSubCatButton;

    private CategoryDialogFragment(Category category) {
        this.category = category;
        this.isUpdate = true;
    }

    private CategoryDialogFragment() {
    }

    public static CategoryDialogFragment newInstance() {
        return new CategoryDialogFragment();
    }

    public static CategoryDialogFragment newInstanceWithUpdate(Category category) {
        return new CategoryDialogFragment(category);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCategoryDialogListDialogBinding.inflate(inflater, container, false);
        this.categoryRepository = CategoryRepository.getInstance(CategoryDataSource.getInstance(getContext()));

        initialize();
        addCategoryAddButtonListener();
        addCategoryDeleteButtonListener();
        addSubCategoryDeleteButtonListener();

        //update
        if (this.isUpdate) {
            fillWithInitData();
        }

        return binding.getRoot();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        SettingsFragment parentFragment = (SettingsFragment) getParentFragment();
        parentFragment.showExistingCategories();
    }

    private void initialize() {
        this.incomeOrExpenseChipGroup = binding.incomeOrExpenseChipGroup;
        this.subCategoriesChipGroup = binding.subCategoriesChipGroup;
        this.incomeChip = binding.incomeChip;
        this.expenseChip = binding.expenseChip;
        this.categoryNameText = binding.categoryNameText;
        this.addCategoryButton = binding.addFabButton;
        this.deleteCatButton = binding.deleteFabButton;
        this.deleteSubCatButton = binding.deleteSubCategoriesFabButton;

        this.incomeChip.callOnClick();
        this.incomeChip.setChecked(true);
    }

    private void fillWithInitData() {
        if (CategoryType.INCOME.name().equals(this.category.getType())) {
            this.incomeChip.setChecked(true);
            this.expenseChip.setEnabled(false);
            this.incomeChip.callOnClick();
            this.categoryNameText.setText(category.getName());
            getAllSubCategoriesChips(category.getId()).forEach(income -> binding.subCategoriesChipGroup.addView(income));

        } else {
            this.expenseChip.setChecked(true);
            this.incomeChip.setEnabled(false);
            this.expenseChip.callOnClick();
            this.categoryNameText.setText(category.getName());
            getAllSubCategoriesChips(category.getId()).forEach(expense -> binding.subCategoriesChipGroup.addView(expense));

        }
        addCategoryButton.setText(R.string.update);
        deleteCatButton.setVisibility(View.VISIBLE);
        deleteSubCatButton.setVisibility(View.VISIBLE);
    }

    private List<Chip> getAllSubCategoriesChips(int parentCategoryId) {
        return categoryRepository.getSubCategories(parentCategoryId)
                .stream()
                .map(subCategory -> UIUtil.createFilterChip(getLayoutInflater(), subCategory.getId(), subCategory.getName()))
                .collect(Collectors.toList());
    }

    private void addCategoryAddButtonListener() {
        this.addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = categoryNameText.getText().toString();
                if (categoryName.trim().isEmpty()) {
                    return;
                }
                int categoryId = incomeOrExpenseChipGroup.getCheckedChipId();
                Chip selectedChip = (Chip) incomeOrExpenseChipGroup.findViewById(categoryId);
                String categoryType = selectedChip.getText().toString();
                if (isUpdate) {
                    categoryRepository.update(category.getId(), categoryName);
                } else {
                    categoryRepository.saveCategory(categoryName, CategoryType.valueOf(categoryType));
                }
                dismiss();
            }
        });
    }

    private void addCategoryDeleteButtonListener() {
        this.deleteCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DEFAULT_CATEGORY.equals(category.getName())) {
                    Toast.makeText(getContext(), "Cannot delete default category!", Toast.LENGTH_LONG).show();
                } else {
                    categoryRepository.deleteCategory(category);
                }
                dismiss();
            }
        });
    }

    private void addSubCategoryDeleteButtonListener() {
        this.deleteSubCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DEFAULT_CATEGORY.equals(category.getName())) {
                    Toast.makeText(getContext(), "Cannot delete default (sub)category!", Toast.LENGTH_LONG).show();
                } else {
                    subCategoriesChipGroup.getCheckedChipIds()
                            .stream()
                            .filter(selectedSubCatChipId -> !((Chip) subCategoriesChipGroup.findViewById(selectedSubCatChipId)).getText().toString().equals(DEFAULT_SUBCATEGORY))
                            .forEach(selectedSubCatChipId -> {
                                String subCategoryName = ((Chip) subCategoriesChipGroup.findViewById(selectedSubCatChipId)).getText().toString();
                                categoryRepository.deleteSubCategory(subCategoryName);
                            });
                }
                dismiss();
            }
        });
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


}