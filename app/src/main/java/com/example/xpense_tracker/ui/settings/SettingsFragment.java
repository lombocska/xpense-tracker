package com.example.xpense_tracker.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.xpense_tracker.R;
import com.example.xpense_tracker.data.Currency;
import com.example.xpense_tracker.data.SharedPreferenceService;
import com.example.xpense_tracker.databinding.FragmentSettingsBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

// https://developer.android.com/training/data-storage/shared-preferences
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SharedPreferenceService sharedPreferenceService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.sharedPreferenceService = SharedPreferenceService.getInstance(getContext());
        addCurrencyChipListener();
        checkSelectedChip();
        return root;
    }

    private void checkSelectedChip() {
        String currency = sharedPreferenceService.getCurrency();
        ChipGroup currencyChipGroup = binding.currencyChip;
        for (int i = 0; i < currencyChipGroup.getChildCount(); i++) {
            Chip selectedChip = (Chip) currencyChipGroup.getChildAt(i);
            if (currency.equals(selectedChip.getText())) {
                currencyChipGroup.check(selectedChip.getId());
                return;
            }
        }
    }


    private void addCurrencyChipListener() {
        ChipGroup chipGroup = binding.currencyChip;
        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                Chip chip = (Chip) group.findViewById(checkedIds.get(0));
                if (Arrays.stream(Currency.values()).anyMatch(currency -> currency.name().contentEquals(chip.getText()))) {
                    sharedPreferenceService.setCurrency(chip.getText().toString());
                } else {
                    Log.e("currency settings", "Error occurred during currency state changing process.");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}