package com.example.xpense_tracker.data.model;

public final class ExpenseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ExpenseContract() {}

    /* Inner class that defines the table contents */
    public static class TransactionContent {
        public static final String TABLE_NAME = "expense";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_SUB_CATEGORY = "sub_category";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_CREATED_AT = "created_at";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }

}
