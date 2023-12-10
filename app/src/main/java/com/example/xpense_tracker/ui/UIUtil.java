package com.example.xpense_tracker.ui;

import android.view.LayoutInflater;

import com.example.xpense_tracker.databinding.FragmentFilterChipBinding;
import com.google.android.material.chip.Chip;

public class UIUtil {

    private UIUtil() {
    }

    public static Chip createFilterChip(LayoutInflater inflater, int id, String name) {
        Chip chip = FragmentFilterChipBinding.inflate(inflater).getRoot();
        chip.setId(id);
        chip.setText(name);
        return chip;
    }

}
