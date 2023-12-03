package com.example.xpense_tracker.ui.home;

import static android.view.View.GONE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xpense_tracker.R;
import com.example.xpense_tracker.data.CategoryDataSource;
import com.example.xpense_tracker.data.CategoryRepository;
import com.example.xpense_tracker.data.ExpenseDataSource;
import com.example.xpense_tracker.data.ExpenseRepository;
import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.data.model.Expense;
import com.example.xpense_tracker.data.model.SubCategory;
import com.example.xpense_tracker.databinding.FragmentAddExpenseOrIncomeDialogListDialogBinding;
import com.example.xpense_tracker.databinding.FragmentFilterChipBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    private ExpenseRepository expenseRepository;

    private ChipGroup incomeOrExpenseCategoriesChipGroup;
    private ChipGroup incomeOrExpenseSubCategoriesChipGroup;
    private Chip incomeChip;
    private Chip expenseChip;
    private ChipGroup subCategoryChipGroup;
    private LinearLayout amountLinearLayout;
    private LinearLayout noteLinearLayout;
    private TextInputEditText amountEditText;
    private FloatingActionButton calendarButton;
    private ExtendedFloatingActionButton addButton;
    private TextInputEditText noteEditText;
    private Expense expense;
    private boolean isUpdate = false;
    private List<Category> incomeCategories = new ArrayList<>();
    private List<Category> expenseCategories = new ArrayList<>();
    List<SubCategory> subCategories = new ArrayList<>();

    private AddExpenseOrIncomeDialogFragment(Expense expense) {
        this.expense = expense;
        this.isUpdate = true;
    }

    private AddExpenseOrIncomeDialogFragment() {
    }

    public static AddExpenseOrIncomeDialogFragment newInstance() {
        return new AddExpenseOrIncomeDialogFragment();
    }

    public static AddExpenseOrIncomeDialogFragment newInstanceWithUpdate(Expense expense) {
        return new AddExpenseOrIncomeDialogFragment(expense);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        refreshBottomWindow();
        binding = FragmentAddExpenseOrIncomeDialogListDialogBinding.inflate(inflater, container, false);
        initialize();

        List<Chip> incomeCategoryChips = getIncomeCategoryChips();
        List<Chip> expenseCategoryChips = getExpenseCategoryChips();

        addIncomeListener(incomeCategoryChips);
        addExpenseListener(expenseCategoryChips);

        addCalendarButtonListener();
        addAddButtonListener();

        //update
        if (this.isUpdate) {
            fillWithInitData();
        }
        return binding.getRoot();
    }

    private void fillWithInitData() {
        if (CategoryType.INCOME.name().equals(this.expense.getType())) {
            this.incomeChip.setChecked(true);
            this.incomeChip.callOnClick();

            Optional<Category> categoryOptional = incomeCategories.stream()
                    .filter(category -> category.getName().equals(expense.getCategory()))
                    .findFirst();
            if (!categoryOptional.isPresent()) {
                return;
            }
            Chip selectedIncomeCategoryChip = (Chip) this.incomeOrExpenseCategoriesChipGroup.findViewById(categoryOptional.get().getId());
            selectedIncomeCategoryChip.setChecked(true);
            selectedIncomeCategoryChip.callOnClick();

            Optional<SubCategory> subCategoryOptional = subCategories.stream()
                    .filter(subCategory -> subCategory.getName().equals(expense.getSubCategory()))
                    .findFirst();
            if (!subCategoryOptional.isPresent()) {
                return;
            }
            Chip selectedSubIncomeCategoryChip = (Chip) this.incomeOrExpenseSubCategoriesChipGroup.findViewById(subCategoryOptional.get().getId());
            selectedSubIncomeCategoryChip.setChecked(true);
            selectedSubIncomeCategoryChip.callOnClick();

        } else {
            this.expenseChip.setChecked(true);
            this.expenseChip.callOnClick();

            Optional<Category> categoryOptional = expenseCategories.stream()
                    .filter(category -> category.getName().equals(expense.getCategory()))
                    .findFirst();
            if (!categoryOptional.isPresent()) {
                return;
            }
            Chip selectedExpenseCategoryChip = (Chip) this.incomeOrExpenseCategoriesChipGroup.findViewById(categoryOptional.get().getId());
            selectedExpenseCategoryChip.setChecked(true);
            selectedExpenseCategoryChip.callOnClick();

            Optional<SubCategory> subCategoryOptional = subCategories.stream()
                    .filter(subCategory -> subCategory.getName().equals(expense.getSubCategory()))
                    .findFirst();
            if (!subCategoryOptional.isPresent()) {
                return;
            }
            Chip selectedSubIncomeCategoryChip = (Chip) this.incomeOrExpenseSubCategoriesChipGroup.findViewById(subCategoryOptional.get().getId());
            selectedSubIncomeCategoryChip.setChecked(true);
            selectedSubIncomeCategoryChip.callOnClick();
        }
        noteEditText.setText(expense.getNote());
        amountEditText.setText(expense.getAmount());
        addButton.setText(R.string.update);
    }

    private void initialize() {
        this.categoryRepository = CategoryRepository.getInstance(CategoryDataSource.getInstance(getContext()));
        this.expenseRepository = ExpenseRepository.getInstance(ExpenseDataSource.getInstance(getContext()));

        this.incomeOrExpenseCategoriesChipGroup = binding.incomeOrExpenseCategoriesChipGroup;
        this.incomeOrExpenseSubCategoriesChipGroup = binding.incomeOrExpenseSubCategoriesChipGroup;
        this.incomeChip = binding.incomeChip;
        this.expenseChip = binding.expenseChip;
        this.subCategoryChipGroup = binding.incomeOrExpenseSubCategoriesChipGroup;
        this.amountLinearLayout = binding.amountLinearLayout;
        this.amountEditText = binding.amountInputText;
        this.calendarButton = binding.calendarButton;
        this.addButton = binding.addFabButton;
        this.noteEditText = binding.noteInputText;
        this.noteLinearLayout = binding.noteLinearLayout;

        if (this.expense == null) {
            this.expense = new Expense();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
        addCategoryChipListener(category, categoryChip);
        return categoryChip;
    }

    private void addCategoryChipListener(Category category, Chip categoryChip) {
        categoryChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDialogBelowCategoryChips();
                expense.setCategory(category.getName());
                expense.setType(category.getType());

                subCategories = categoryRepository.getSubCategories(category.getId());
                List<Chip> subCategoryChips = subCategories.stream()
                        .map(subCategory -> {
                            Chip subCategoryFilterChip = createFilterChip(subCategory.getId(), subCategory.getName());
                            addSubCategoryListener(subCategoryFilterChip);
                            return subCategoryFilterChip;
                        })
                        .collect(Collectors.toList());
                subCategoryChips.forEach(subCategoryChipGroup::addView);
            }


        });
    }

    private void addSubCategoryListener(Chip subCategoryFilterChip) {

        subCategoryFilterChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense.setSubCategory(subCategoryFilterChip.getText().toString());
                refreshBottomWindow();

                calendarButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
                amountLinearLayout.setVisibility(View.VISIBLE);
                noteLinearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addCalendarButtonListener() {

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                        .datePicker().setTitleText(R.string.date_picker_title)
                        .build();
                datePicker.show(getChildFragmentManager(), "expenseDatePicker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        String dateString = DateFormat.format("yyyy-MM-dd", new Date(selection)).toString();
                        expense.setCreatedAt(LocalDate.parse(dateString));
                    }
                });
            }
        });
    }

    private void addAddButtonListener() {

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if (amountEditText.getText() == null) {
                    amountEditText.setError("Amount can't be empty.");
                } else {
                    expense.setAmount(amountEditText.getText().toString());
                    expense.setNote(Optional.ofNullable(noteEditText.getText()).map(Object::toString).orElseGet(() -> "N/A"));
                    expense.setCreatedAt(Optional.ofNullable(expense.getCreatedAt()).orElseGet(LocalDate::now));

                    expenseRepository.addExpense(expense);
                    ExpenseListAdapter.getInstance(getContext()).addExpense(expense);
                    dismiss();
                    Toast.makeText(getContext(), "Added new expense with amount " + amountEditText.getText(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void clearDialogBelowMainChips() {
        incomeOrExpenseCategoriesChipGroup.removeAllViews();
        clearDialogBelowCategoryChips();
    }

    private void clearDialogBelowCategoryChips() {
        incomeOrExpenseSubCategoriesChipGroup.removeAllViews();
        hideCalendarAndNote();
        hideAddButton();
    }

    private void refreshBottomWindow() {
        this.getDialog().getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void hideAddButton() {
        addButton.setVisibility(GONE);
    }

    private void hideCalendarAndNote() {
        calendarButton.setVisibility(GONE);
        amountLinearLayout.setVisibility(GONE);
        noteLinearLayout.setVisibility(GONE);
    }

    private Chip createFilterChip(int id, String name) {
        Chip chip = FragmentFilterChipBinding.inflate(getLayoutInflater()).getRoot();
        chip.setId(id);
        chip.setText(name);
        return chip;
    }

}