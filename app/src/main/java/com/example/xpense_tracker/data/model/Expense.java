package com.example.xpense_tracker.data.model;

import java.time.LocalDate;

public class Expense {
    private int id;

    private String category;
    private String subCategory;
    private String type;
    private LocalDate createdAt;
    private String amount;
    private String note;

    public Expense(String category, String subCategory, String type, LocalDate createdAt, String note, String amount) {
        this.category = category;
        this.subCategory = subCategory;
        this.type = type;
        this.createdAt = createdAt;
        this.amount = amount;
        this.note = note;
    }

    public Expense() {
    }

    public Expense(int id, String category, String subCategory, String type, LocalDate createdAt, String amount, String note) {
        this.id = id;
        this.category = category;
        this.subCategory = subCategory;
        this.type = type;
        this.createdAt = createdAt;
        this.note = note;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
