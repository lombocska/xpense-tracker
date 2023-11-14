package com.example.xpense_tracker.data.model;

//TODO: baseclass BaseCategory
public class SubCategory {
    private int id;
    private String name;
    private String type;
    private int parentCategory;

    public SubCategory(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public SubCategory(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getParentCategory() {
        return parentCategory;
    }
}
