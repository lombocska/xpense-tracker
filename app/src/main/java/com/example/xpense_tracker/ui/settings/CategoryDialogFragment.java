package com.example.xpense_tracker.ui.settings;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xpense_tracker.data.CategoryDataSource;
import com.example.xpense_tracker.data.CategoryRepository;
import com.example.xpense_tracker.data.ExpenseDataSource;
import com.example.xpense_tracker.data.ExpenseRepository;
import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.databinding.FragmentCategoryDialogListDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     CategoryDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class CategoryDialogFragment extends BottomSheetDialogFragment {

    private FragmentCategoryDialogListDialogBinding binding;
    private ChipGroup incomeOrExpenseChipGroup;
    private TextInputEditText categoryNameText;
    private ExtendedFloatingActionButton addButton;
    private CategoryRepository categoryRepository;

    public static CategoryDialogFragment newInstance() {
        return new CategoryDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCategoryDialogListDialogBinding.inflate(inflater, container, false);
        this.categoryRepository = CategoryRepository.getInstance(CategoryDataSource.getInstance(getContext()));

        initialize();
        addAddButtonListener();
        return binding.getRoot();
    }

    private void addAddButtonListener() {
        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedChipId = incomeOrExpenseChipGroup.getCheckedChipId();
                Chip selectedChip = (Chip) incomeOrExpenseChipGroup.findViewById(checkedChipId);
                String categoryType = selectedChip.getText().toString();
                String categoryName = categoryNameText.getText().toString();

                categoryRepository.saveCategory(categoryName, CategoryType.valueOf(categoryType));
                dismiss();
            }
        });
    }

    private void initialize() {
        this.incomeOrExpenseChipGroup = binding.incomeOrExpenseChipGroup;
        this.categoryNameText = binding.categoryNameText;
        this.addButton = binding.addFabButton;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}