package com.example.xpense_tracker.data;

import static com.example.xpense_tracker.data.QueryConstant.DATABASE;
import static com.example.xpense_tracker.data.model.UserContract.UserContent.COLUMN_NAME_EMAIL;
import static com.example.xpense_tracker.data.model.UserContract.UserContent.COLUMN_NAME_PASSWORD;
import static com.example.xpense_tracker.data.model.UserContract.UserContent.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.xpense_tracker.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource extends SQLiteOpenHelper {

    private static volatile LoginDataSource instance;

    public static LoginDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new LoginDataSource(context);
        }
        return instance;
    }

    private LoginDataSource(@Nullable Context context) {
        super(context, DATABASE, null, 1);
        this.getWritableDatabase().execSQL(QueryConstant.CREATE_USERS_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QueryConstant.CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(QueryConstant.DROP_USERS_TABLE);
    }

    public Result<LoggedInUser> login(String username, String password) {
        try {
            String editedUsername = username.contains("@") ? username.substring(0, username.indexOf("@")) : username;
            if (hasAlreadyRegistered(username)) {
                if(isPasswordValid(password)) {
                    LoggedInUser loggedInUser = new LoggedInUser(username, editedUsername);
                    return new Result.Success<>(loggedInUser);
                }
            } else {
                registerUser(username, password);
                LoggedInUser loggedInUser = new LoggedInUser(username, editedUsername);
                return new Result.Success<>(loggedInUser);
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
        return new Result.Error(new IOException("Error logging in"));
    }

    private void registerUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_EMAIL, username);
        values.put(COLUMN_NAME_PASSWORD, password); //encrypt it in prod code, it's just demo purpose

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
    }

    private boolean hasAlreadyRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Filter results WHERE "title" = 'My Title'
        String selection = COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs = {email};
        String sortOrder = COLUMN_NAME_EMAIL + " DESC";
        Cursor cursor = db.query(
                TABLE_NAME,     // The table to query
                null,              // The array of columns to return (pass null to get all)
                selection,         // The columns for the WHERE clause
                selectionArgs,     // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                sortOrder          // The sort order
        );
        return cursor.getCount() > 0;
    }

    private boolean isPasswordValid(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Filter results WHERE "title" = 'My Title'
        String selection = COLUMN_NAME_PASSWORD + " = ?";
        String[] selectionArgs = {password};
        String sortOrder = COLUMN_NAME_PASSWORD + " DESC";
        Cursor cursor = db.query(
                TABLE_NAME,     // The table to query
                null,              // The array of columns to return (pass null to get all)
                selection,         // The columns for the WHERE clause
                selectionArgs,     // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                sortOrder          // The sort order
        );
        return cursor.getCount() > 0;
    }

    public void logout() {
        // TODO: revoke authentication
    }
}