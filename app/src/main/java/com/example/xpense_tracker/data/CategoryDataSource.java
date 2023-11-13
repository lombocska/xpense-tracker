package com.example.xpense_tracker.data;

import static com.example.xpense_tracker.data.QueryConstant.DATABASE;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.COLUMN_NAME_ID;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.COLUMN_NAME_NAME;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.COLUMN_NAME_TYPE;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryType;

import java.util.ArrayList;
import java.util.List;

public class CategoryDataSource extends SQLiteOpenHelper {

    public CategoryDataSource(@Nullable Context context) {
        super(context, DATABASE, null, 1);
        this.getWritableDatabase().execSQL(QueryConstant.DROP_CATEGORY_TABLE);
        this.getWritableDatabase().execSQL(QueryConstant.CREATE_CATEGORY_TABLE);
        addInitialIncomeCategory("income", "investment", "other");
        addInitialExpenseCategory("Food", "Tech");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QueryConstant.CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(QueryConstant.DROP_CATEGORY_TABLE);
    }

    public List<Category> getCategory(CategoryType type) {
        List<Category> categories = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        // Filter results WHERE "type" = 'INCOME'
        String selection = COLUMN_NAME_TYPE + " = ?";
        String[] selectionArgs = {type.name()};
        String sortOrder = COLUMN_NAME_ID + " ASC";
        Cursor cursor = db.query(
                TABLE_NAME,     // The table to query
                null,              // The array of columns to return (pass null to get all)
                selection,         // The columns for the WHERE clause
                selectionArgs,     // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                sortOrder          // The sort order
        );
        if(cursor.moveToFirst()) {
            do {
                Category category = new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        return categories;
    }

    private void addInitialIncomeCategory(String... names) {
        for (String name : names) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_NAME, name);
            values.put(COLUMN_NAME_TYPE, CategoryType.INCOME.name());

            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_NAME, null, values);
        }
    }

    private void addInitialExpenseCategory(String ... names) {
        for (String name : names) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_NAME, name);
            values.put(COLUMN_NAME_TYPE, CategoryType.EXPENSE.name());

            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_NAME, null, values);
        }
    }
}
