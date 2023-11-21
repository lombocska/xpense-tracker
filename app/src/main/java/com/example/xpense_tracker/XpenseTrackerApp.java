//package com.example.xpense_tracker;
//
//
//import android.app.Application;
//
//import com.example.xpense_tracker.data.Currency;
//import com.example.xpense_tracker.data.SharedPreferenceService;
//
//public class XpenseTrackerApp extends Application {
//
//
//    private SharedPreferenceService sharedPreferenceService;
//
//    @Override
//    public void onCreate(){
//        super.onCreate();
//        this.sharedPreferenceService = SharedPreferenceService.getInstance(getBaseContext());
//        initMethod();
//    }
//
//    public void initMethod(){
//        sharedPreferenceService.setCurrency(Currency.HUF.name());
//    }
//}
