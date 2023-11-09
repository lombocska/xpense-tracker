package com.example.xpense_tracker.data.model;

public final class UserContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserContract() {}

    /* Inner class that defines the table contents */
    public static class UserContent {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";

    }
}
