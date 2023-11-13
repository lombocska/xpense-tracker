package com.example.xpense_tracker.data.model;

public final class CategoryContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CategoryContract() {}

    /* Inner class that defines the table contents */
    public static class CategoryContent {
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";

    }
}
