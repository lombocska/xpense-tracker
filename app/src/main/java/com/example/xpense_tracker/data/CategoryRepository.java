package com.example.xpense_tracker.data;

import com.example.xpense_tracker.data.model.Category;
import com.example.xpense_tracker.data.model.CategoryType;

import java.util.List;

public class CategoryRepository {

    private static volatile CategoryRepository instance;
    private CategoryDataSource dataSource;

    private CategoryRepository(CategoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static CategoryRepository getInstance(CategoryDataSource dataSource) {
        if (instance == null) {
            return new CategoryRepository(dataSource);
        }
        return instance;
    }

    public List<Category> getCategories(CategoryType type) {
        return dataSource.getCategory(type);
    }
}
