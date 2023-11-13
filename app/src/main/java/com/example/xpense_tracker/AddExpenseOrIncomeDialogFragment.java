package com.example.xpense_tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.xpense_tracker.databinding.FragmentAddExpenseOrIncomeDialogListDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     AddExpenseOrIncomeDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class AddExpenseOrIncomeDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private FragmentAddExpenseOrIncomeDialogListDialogBinding binding;

    // TODO: Customize parameters
    public static AddExpenseOrIncomeDialogFragment newInstance() {
        return new AddExpenseOrIncomeDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddExpenseOrIncomeDialogListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}