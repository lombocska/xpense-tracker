package com.example.xpense_tracker.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.xpense_tracker.data.LoginDataSource;
import com.example.xpense_tracker.data.LoginRepository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final LoginActivity activity;

    public LoginViewModelFactory(LoginActivity loginActivity) {
        this.activity = loginActivity;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(LoginDataSource.getInstance(activity)));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}