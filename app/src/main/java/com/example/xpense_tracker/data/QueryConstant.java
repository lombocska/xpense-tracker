package com.example.xpense_tracker.data;

public final class QueryConstant {

    private QueryConstant() {}

    public static final String CREATE_USERS_TABLE = "create Table users(email TEXT primary key, password TEXT)";
    public static final String DROP_USERS_TABLE = "drop Table if exists users";

}
