package com.example.xpense_tracker.data;

public final class QueryConstant {

    private QueryConstant() {}

    public static final String DATABASE = "ExpenseTracker.db";

    public static final String CREATE_USERS_TABLE = "create Table users(email TEXT primary key, password TEXT)";
    public static final String DROP_USERS_TABLE = "drop Table if exists users";

    //SQLite INT primary key means a different id than rowID,but INTEGER will be the same as the default rowid
    public static final String CREATE_CATEGORY_TABLE = "create Table category(id INTEGER primary key, name TEXT, type TEXT)";
    public static final String DROP_CATEGORY_TABLE = "drop Table if exists category";
    public static final String CREATE_SUB_CATEGORY_TABLE = "create Table sub_category(id INTEGER primary key, name TEXT, type TEXT, parent_id INT)";
    public static final String DROP_SUB_CATEGORY_TABLE = "drop Table if exists sub_category";

}
