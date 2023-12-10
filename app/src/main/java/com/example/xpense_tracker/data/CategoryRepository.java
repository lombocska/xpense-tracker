package com.example.xpense_tracker.data;

import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryType;
import com.example.xpense_tracker.data.model.SubCategory;

import java.util.List;
import java.util.Map;

public class CategoryRepository {

    private static volatile CategoryRepository instance;
    private CategoryDataSource dataSource;

    private CategoryRepository(CategoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static CategoryRepository getInstance(CategoryDataSource dataSource) {
        if (instance == null) {
            instance = new CategoryRepository(dataSource);
        }
        return instance;
    }

    public List<Category> getCategories(CategoryType type) {
        return dataSource.getCategory(type);
    }

    public List<SubCategory> getSubCategories(int parentCategoryId) {
        return dataSource.getSubCategories(parentCategoryId);
    }

    public List<SubCategory> getSubCategories(CategoryType type) {
        return dataSource.getSubCategories(type);
    }

    public void saveCategory(String categoryName, CategoryType type) {
        dataSource.save(categoryName, type);
    }

    public void saveSubCategory(Map<String, List<String>> categoryWithSubCategories, CategoryType type) {
        dataSource.saveSubCategory(categoryWithSubCategories, type);
    }

    public void deleteCategory(Category category) {
        dataSource.delete(category);
    }

    public void deleteSubCategory(String subCategoryName) {
        dataSource.delete(subCategoryName);
    }

    public void update(int categoryId, String categoryName) {
        dataSource.update(categoryId, categoryName);
    }
}
