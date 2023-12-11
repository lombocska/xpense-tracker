package com.example.xpense_tracker.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.xpense_tracker.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SharedPreferenceService {
    private static volatile SharedPreferenceService instance;
    private Context context;

    private SharedPreferenceService(Context context) {
        this.context = context;
    }

    public static SharedPreferenceService getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceService(context);
        }
        return instance;
    }

    public void setCurrency(String currency) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(getString(R.string.shared_preference_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.shared_preference_currency_key), currency);
        editor.apply();
    }

    public String getCurrency() {
        SharedPreferences pref =
                context
                        .getSharedPreferences(getString(R.string.shared_preference_file_name), Context.MODE_PRIVATE);
        String defaultCurrency = pref.getString(getString(R.string.shared_preference_currency_key), Currency.HUF.name());
        return defaultCurrency;
    }

    public  BigDecimal applySelectedCurrency(Integer amount) {
        Double changedAmount = Currency.valueOf(getCurrency()).getChangingNum() * amount;
        return BigDecimal.valueOf(changedAmount).setScale(2, RoundingMode.HALF_UP);

    }

    private String getString(int resId) {
        return context.getResources().getString(resId);
    }

}
