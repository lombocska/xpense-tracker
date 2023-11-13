package com.example.xpense_tracker.data.model;

public class Category {

    private int id;
    private String name;
    private String type;

    public Category(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Category(int id, String name, String type) {
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
}
