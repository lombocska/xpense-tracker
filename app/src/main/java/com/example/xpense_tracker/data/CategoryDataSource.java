package com.example.xpense_tracker.data;

import static com.example.xpense_tracker.data.QueryConstant.DATABASE;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.COLUMN_NAME_ID;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.COLUMN_NAME_NAME;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.COLUMN_NAME_TYPE;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.DEFAULT_CATEGORY;
import static com.example.xpense_tracker.data.model.CategoryContract.CategoryContent.TABLE_NAME;
import static com.example.xpense_tracker.data.model.CategoryContract.SubCategoryContent.DEFAULT_SUBCATEGORY;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryContract;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.data.model.ExpenseContract;
import com.example.xpense_tracker.data.model.SubCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryDataSource extends SQLiteOpenHelper {

    private static volatile CategoryDataSource instance;

    public static CategoryDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new CategoryDataSource(context);
        }
        return instance;
    }

    private CategoryDataSource(@Nullable Context context) {
        super(context, DATABASE, null, 1);
        this.getWritableDatabase().execSQL(QueryConstant.DROP_CATEGORY_TABLE);
        this.getWritableDatabase().execSQL(QueryConstant.CREATE_CATEGORY_TABLE);
        this.getWritableDatabase().execSQL(QueryConstant.DROP_SUB_CATEGORY_TABLE);
        this.getWritableDatabase().execSQL(QueryConstant.CREATE_SUB_CATEGORY_TABLE);

        addInitialIncomeCategory("Income", "Investment", DEFAULT_CATEGORY);
        addInitialExpenseCategory("Food", "Housing", "Shopping", "Transport", "Entertainment", DEFAULT_CATEGORY);

        saveSubCategory(Map.of("Income", List.of("Rate", "Rental", "Sale")), CategoryType.INCOME);
        saveSubCategory(Map.of("Food", List.of("Restaurant", "CoffeeShop", "Grocery")), CategoryType.EXPENSE);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QueryConstant.CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(QueryConstant.CREATE_SUB_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(QueryConstant.DROP_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(QueryConstant.DROP_SUB_CATEGORY_TABLE);
    }

    public List<Category> getCategory(CategoryType type) {
        List<Category> categories = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        // Filter results WHERE "type" = 'INCOME'
        String selection = COLUMN_NAME_TYPE + " = ?";
        String[] selectionArgs = {type.name()};
        String sortOrder = COLUMN_NAME_ID + " ASC";
        Cursor cursor = db.query(
                TABLE_NAME,        // The table to query
                null,              // The array of columns to return (pass null to get all)
                selection,         // The columns for the WHERE clause
                selectionArgs,     // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                sortOrder          // The sort order
        );
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        return categories;
    }


    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Filter results WHERE "type" = 'INCOME'
        String selection = COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        String sortOrder = COLUMN_NAME_ID + " ASC";
        Cursor cursor = db.query(
                TABLE_NAME,        // The table to query
                null,              // The array of columns to return (pass null to get all)
                selection,         // The columns for the WHERE clause
                selectionArgs,     // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                sortOrder          // The sort order
        );
        if (cursor.moveToFirst()) {
            return new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        } else {
            throw new IllegalStateException("Category not found with id: " + id );
        }
    }

    public List<SubCategory> getSubCategories(int parentCategoryId) {
        List<SubCategory> categories = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        // Filter results WHERE "parentId" = 'something'
        String selection = CategoryContract.SubCategoryContent.COLUMN_NAME_PARENT_CATEGORY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(parentCategoryId)};
        String sortOrder = CategoryContract.SubCategoryContent.COLUMN_NAME_ID + " ASC";
        Cursor cursor = db.query(
                CategoryContract.SubCategoryContent.TABLE_NAME,        // The table to query
                null,              // The array of columns to return (pass null to get all)
                selection,         // The columns for the WHERE clause
                selectionArgs,     // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                sortOrder          // The sort order
        );
        if (cursor.moveToFirst()) {
            do {
                SubCategory category = new SubCategory(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        return categories;
    }

    public List<SubCategory> getSubCategories(CategoryType type) {
        List<SubCategory> categories = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        // Filter results WHERE "parentId" = 'something'
        String selection = CategoryContract.SubCategoryContent.COLUMN_NAME_TYPE + " = ?";
        String[] selectionArgs = {type.name()};
        String sortOrder = CategoryContract.SubCategoryContent.COLUMN_NAME_ID + " ASC";
        Cursor cursor = db.query(
                CategoryContract.SubCategoryContent.TABLE_NAME,        // The table to query
                null,              // The array of columns to return (pass null to get all)
                selection,         // The columns for the WHERE clause
                selectionArgs,     // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                sortOrder          // The sort order
        );
        if (cursor.moveToFirst()) {
            do {
                SubCategory category = new SubCategory(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        return categories;
    }

    public void save(String name, CategoryType categoryType) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, name);
        values.put(COLUMN_NAME_TYPE, categoryType.name());

        SQLiteDatabase db = this.getWritableDatabase();
        long categoryId = db.insert(TABLE_NAME, null, values);

        //add initial subcategory
        if (getSubCategories((int) categoryId).size() == 0) {
            saveSubCategory(Map.of(name, List.of(DEFAULT_SUBCATEGORY)), categoryType);
        }
    }

    private void addInitialIncomeCategory(String... names) {
        for (String name : names) {
            save(name, CategoryType.INCOME);
        }
    }

    private void addInitialExpenseCategory(String... names) {
        for (String name : names) {
            save(name, CategoryType.EXPENSE);
        }
    }

    public void saveSubCategory(Map<String, List<String>> categoryWithSubCategories, CategoryType type) {
        categoryWithSubCategories.entrySet().forEach(entry -> {
            SQLiteDatabase db = this.getWritableDatabase();
            // Filter results WHERE "name" = 'something'
            String selection = COLUMN_NAME_NAME + " = ?";
            String parentCategoryName = entry.getKey();
            String[] selectionArgs = {parentCategoryName};
            Cursor cursor = db.query(
                    TABLE_NAME,        // The table to query
                    null,              // The array of columns to return (pass null to get all)
                    selection,         // The columns for the WHERE clause
                    selectionArgs,     // The values for the WHERE clause
                    null,              // don't group the rows
                    null,              // don't filter by row groups
                    null          // The sort order
            );
            if (cursor.getCount() == 0) {
                Log.w("SQL Error", "Error occurred, no Category had been found with name: " + parentCategoryName);
                return;
            }
            cursor.moveToFirst();
            entry.getValue()
                    .stream()
                    .forEach(subCategory -> {
                        ContentValues values = new ContentValues();
                        values.put(CategoryContract.SubCategoryContent.COLUMN_NAME_NAME, subCategory);
                        values.put(CategoryContract.SubCategoryContent.COLUMN_NAME_TYPE, type.name());
                        values.put(CategoryContract.SubCategoryContent.COLUMN_NAME_PARENT_CATEGORY_ID, cursor.getInt(0));

                        db.insert(CategoryContract.SubCategoryContent.TABLE_NAME, null, values);
                    });
        });
    }

    public void delete(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        //update expenses with Other (sub)category
        String whereExpensesClause = ExpenseContract.TransactionContent.COLUMN_NAME_CATEGORY + " = ?";
        String[] whereExpensesArgs = {String.valueOf(category.getName())};
        ContentValues values = new ContentValues();
        values.put(ExpenseContract.TransactionContent.COLUMN_NAME_CATEGORY, DEFAULT_CATEGORY);
        values.put(ExpenseContract.TransactionContent.COLUMN_NAME_SUB_CATEGORY, DEFAULT_SUBCATEGORY);
        db.update(ExpenseContract.TransactionContent.TABLE_NAME, values, whereExpensesClause, whereExpensesArgs);

        //delete subcategory
        String whereClause = CategoryContract.SubCategoryContent.COLUMN_NAME_PARENT_CATEGORY_ID + " = ?";
        String[] whereArgs = {String.valueOf(category.getId())};
        db.delete(CategoryContract.SubCategoryContent.TABLE_NAME, whereClause, whereArgs);

        //delete category
        String whereCategClause = CategoryContract.CategoryContent.COLUMN_NAME_ID + " = ?";
        String[] whereCategArgs = {String.valueOf(category.getId())};
        db.delete(CategoryContract.CategoryContent.TABLE_NAME, whereCategClause, whereCategArgs);
    }

    public void delete(String subCategoryName) {
        SQLiteDatabase db = this.getWritableDatabase();

        //update expenses with Other (sub)category
        String whereExpensesClause = ExpenseContract.TransactionContent.COLUMN_NAME_SUB_CATEGORY + " = ?";
        String[] whereExpensesArgs = {String.valueOf(subCategoryName)};
        ContentValues values = new ContentValues();
        values.put(ExpenseContract.TransactionContent.COLUMN_NAME_SUB_CATEGORY, DEFAULT_SUBCATEGORY);
        db.update(ExpenseContract.TransactionContent.TABLE_NAME, values, whereExpensesClause, whereExpensesArgs);

        //delete subcategory
        String whereClause = CategoryContract.SubCategoryContent.COLUMN_NAME_NAME + " = ?";
        String[] whereArgs = {String.valueOf(subCategoryName)};
        db.delete(CategoryContract.SubCategoryContent.TABLE_NAME, whereClause, whereArgs);
    }

    public void update(int categoryId, String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();

        //update expenses with new category name
        Category category = getCategory(categoryId);
        ContentValues newCategoryName = new ContentValues();
        newCategoryName.put(ExpenseContract.TransactionContent.COLUMN_NAME_CATEGORY, categoryName);
        db.execSQL("UPDATE expense SET category = '" + categoryName + "' WHERE category= '" + category.getName() +"'");

        //update category
        String whereClause = COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {String.valueOf(categoryId)};
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, categoryName);
        db.update(CategoryContract.CategoryContent.TABLE_NAME, values, whereClause, whereArgs);
    }
}
