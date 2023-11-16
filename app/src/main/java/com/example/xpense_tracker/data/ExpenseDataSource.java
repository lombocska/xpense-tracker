package com.example.xpense_tracker.data;

import static com.example.xpense_tracker.data.QueryConstant.DATABASE;
import static com.example.xpense_tracker.data.model.ExpenseContract.TransactionContent.COLUMN_NAME_AMOUNT;
import static com.example.xpense_tracker.data.model.ExpenseContract.TransactionContent.COLUMN_NAME_CATEGORY;
import static com.example.xpense_tracker.data.model.ExpenseContract.TransactionContent.COLUMN_NAME_CREATED_AT;
import static com.example.xpense_tracker.data.model.ExpenseContract.TransactionContent.COLUMN_NAME_ID;
import static com.example.xpense_tracker.data.model.ExpenseContract.TransactionContent.COLUMN_NAME_NOTE;
import static com.example.xpense_tracker.data.model.ExpenseContract.TransactionContent.COLUMN_NAME_SUB_CATEGORY;
import static com.example.xpense_tracker.data.model.ExpenseContract.TransactionContent.COLUMN_NAME_TYPE;
import static com.example.xpense_tracker.data.model.ExpenseContract.TransactionContent.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.data.model.Expense;
import com.example.xpense_tracker.data.model.SubCategory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDataSource extends SQLiteOpenHelper {

    public ExpenseDataSource(@Nullable Context context) {
        super(context, DATABASE, null, 1);
        this.getWritableDatabase().execSQL(QueryConstant.DROP_EXPENSE_TABLE);
        this.getWritableDatabase().execSQL(QueryConstant.CREATE_EXPENSE_TABLE);
        List.of(
                new Expense("Food", "Restaurant", CategoryType.EXPENSE.name(), LocalDate.now(), "300"),
                new Expense("Food", "CoffeeShop", CategoryType.EXPENSE.name(), LocalDate.now(), "500"),
                new Expense("Income", "Rate", CategoryType.INCOME.name(), LocalDate.now(), "1000"),
                new Expense("Income", "Sale", CategoryType.INCOME.name(), LocalDate.now(),"200")
        ).forEach(this::saveExpense);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QueryConstant.CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(QueryConstant.DROP_EXPENSE_TABLE);
    }

    public List<Expense> getAllExpense() {
        List<Expense> expenses = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sortOrder = COLUMN_NAME_ID + " DESC";
        Cursor cursor = db.query(
                TABLE_NAME,        // The table to query
                null,              // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,              // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                sortOrder          // The sort order
        );
        if (cursor.moveToFirst()) {
            do {
                //id, category, sub_category, type, created_at, note, amount
                Expense expense = new Expense(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        LocalDate.parse(cursor.getString(4)),
                        cursor.getString(5),
                        cursor.getString(6));
                expenses.add(expense);
            } while (cursor.moveToNext());
        }
        return expenses;
    }

    public Expense createExpense(Category category, SubCategory subCategory, String amount) {
        //TODO createdAt, note
        Expense newExpense = new Expense(category.getName(), subCategory.getName(), category.getType(), LocalDate.now(), amount);
        saveExpense(newExpense);
        return newExpense;
    }

    private void saveExpense(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_CATEGORY, expense.getCategory());
        values.put(COLUMN_NAME_SUB_CATEGORY, expense.getSubCategory());
        values.put(COLUMN_NAME_TYPE, expense.getType());
        values.put(COLUMN_NAME_NOTE, expense.getNote());
        values.put(COLUMN_NAME_CREATED_AT, expense.getCreatedAt().toString());
        values.put(COLUMN_NAME_AMOUNT, expense.getAmount().toString());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
    }
}
