package com.example.xpense_tracker.data;

public final class QueryConstant {

    private QueryConstant() {}

    public static final String DATABASE = "ExpenseTracker.db";

    public static final String CREATE_USERS_TABLE = "create Table if not exists users(email TEXT primary key, password TEXT)";
    public static final String DROP_USERS_TABLE = "drop Table if exists users";

    //SQLite INT primary key means a different id than rowID,but INTEGER will be the same as the default rowid
    public static final String CREATE_CATEGORY_TABLE = "create Table if not exists  category(id INTEGER primary key, name TEXT, type TEXT)";
    public static final String DROP_CATEGORY_TABLE = "drop Table if exists category";
    public static final String CREATE_SUB_CATEGORY_TABLE = "create Table if not exists  sub_category(id INTEGER primary key, name TEXT, type TEXT, parent_id INT)";
    public static final String DROP_SUB_CATEGORY_TABLE = "drop Table if exists sub_category";
    //SQLite does not handle date as a db type, but it has a date() built-in function
    //transaction isa keyword in SQLite, do not use it as a table name
    public static final String CREATE_EXPENSE_TABLE = "create Table if not exists expense(id INTEGER primary key, category TEXT, sub_category TEXT, type TEXT, created_at TEXT, amount TEXT, note TEXT)";
    public static final String DROP_EXPENSE_TABLE = "drop Table if exists expense";

}
