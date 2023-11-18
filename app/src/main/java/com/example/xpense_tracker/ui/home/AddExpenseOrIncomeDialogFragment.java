package com.example.xpense_tracker.ui.home;

import static android.view.View.GONE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;

import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    private ExpenseRepository expenseRepository;

    public static AddExpenseOrIncomeDialogFragment newInstance() {
        return new AddExpenseOrIncomeDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.getDialog().getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        binding = FragmentAddExpenseOrIncomeDialogListDialogBinding.inflate(inflater, container, false);
        categoryRepository = CategoryRepository.getInstance(new CategoryDataSource(getContext()));
        expenseRepository = ExpenseRepository.getInstance(ExpenseDataSource.getInstance(getContext()));

        List<Chip> incomeCategoryChips = createIncomeCategories();
        List<Chip> expenseCategoryChips = createExpenseCategories();
        ChipGroup incomeOrExpenseCategoriesChipGroup = binding.incomeOrExpenseCategoriesChipGroup;
        ChipGroup incomeOrExpenseSubCategoriesChipGroup = binding.incomeOrExpenseSubCategoriesChipGroup;
        binding.incomeChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCategoryChips(incomeOrExpenseCategoriesChipGroup);
                clearCategoryChips(incomeOrExpenseSubCategoriesChipGroup);
                getClearAmountLinearLayout();
                hideAddButton();

                incomeCategoryChips.forEach(incomeOrExpenseCategoriesChipGroup::addView);
            }
        });
        binding.expenseChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCategoryChips(incomeOrExpenseCategoriesChipGroup);
                clearCategoryChips(incomeOrExpenseSubCategoriesChipGroup);
                getClearAmountLinearLayout();
                hideAddButton();

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

    private static void clearCategoryChips(ChipGroup incomeOrExpenseCategoriesChipGroup) {
        incomeOrExpenseCategoriesChipGroup.removeAllViews();
    }

    private List<Chip> createIncomeCategories() {
        List<Category> incomeCategories = categoryRepository.getCategories(CategoryType.INCOME);
        return getCategoryChipsWithSubCategoryChips(incomeCategories);
    }

    @NonNull
    private List<Chip> getCategoryChipsWithSubCategoryChips(List<Category> categories) {
        return categories.stream()
                .map(this::getCategoryChipWithListener)
                .collect(Collectors.toList());
    }

    private Chip getCategoryChipWithListener(Category category) {
        Chip categoryChip = createFilterChip(category.getId(), category.getName(), category.getType());
        categoryChip.setOnClickListener(new View.OnClickListener() {
            final ChipGroup subCategoryChipGroup = binding.incomeOrExpenseSubCategoriesChipGroup;

            @Override
            public void onClick(View v) {
                clearCategoryChips(subCategoryChipGroup);
                getClearAmountLinearLayout();
                hideAddButton();

                List<SubCategory> subCategories = categoryRepository.getSubCategories(category.getId());
                List<Chip> subCategoryChips = subCategories.stream()
                        .map(this::addSubCategoryListener)
                        .collect(Collectors.toList());
                subCategoryChips.forEach(subCategoryChipGroup::addView);
            }

            private Chip addSubCategoryListener(SubCategory subCategory) {
                Chip subCategoryFilterChip = createFilterChip(subCategory.getId(), subCategory.getName(), subCategory.getType());
                subCategoryFilterChip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addAmountInputEditText(category, subCategory);
                    }
                });
                return subCategoryFilterChip;
            }
        });
        return categoryChip;
    }

    private void addAmountInputEditText(Category category, SubCategory subCategory) {
        LinearLayout amountLinearLayout = getClearAmountLinearLayout();
        hideAddButton();

        //layout
        TextInputLayout textInputLayout = new TextInputLayout(getContext());
        LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
        textInputLayout.setLayoutParams(textInputLayoutParams);
        textInputLayout.setHint(R.string.amount);
        textInputLayout.requestFocus(); //avoiding keyboard overlap

        //text field
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextInputEditText editText = new TextInputEditText(textInputLayout.getContext());
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE); //adding checkmark to keyboard to close it
        editText.setSingleLine(); //avoiding multiline input field
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    binding.addFabButton.setVisibility(View.VISIBLE);
                    binding.addFabButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Expense expense = expenseRepository.addExpense(category, subCategory, editText.getText().toString());// TODO note field is missing from dialog with datepicker
                            ExpenseListAdapter.getInstance(getContext()).addExpense(expense);
                            dismiss();
                            Toast.makeText(getContext(), "Added new expense with amount " + editText.getText(), Toast.LENGTH_LONG).show(); // testing purpose
                        }
                    });

                } else{
                    hideAddButton();
                }
                return false;
            }
        });

        textInputLayout.addView(editText, editTextParams);

        //binding
        amountLinearLayout.addView(textInputLayout);
    }

    private void hideAddButton() {
        binding.addFabButton.setVisibility(GONE);
    }

    @NonNull
    private LinearLayout getClearAmountLinearLayout() {
        LinearLayout amountLinearLayout = binding.amountLinearLayout;
        amountLinearLayout.removeAllViews();
        return amountLinearLayout;
    }

    private List<Chip> createExpenseCategories() {
        List<Category> expenseCategories = categoryRepository.getCategories(CategoryType.EXPENSE);
        return getCategoryChipsWithSubCategoryChips(expenseCategories);
    }

    private Chip createFilterChip(int id, String name, String categoryTypeName) {
        Chip chip = FragmentFilterChipBinding.inflate(getLayoutInflater()).getRoot();
        chip.setId(id);
        chip.setText(name);
        return chip;
    }

}