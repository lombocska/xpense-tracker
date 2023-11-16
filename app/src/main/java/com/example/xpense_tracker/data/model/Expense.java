package com.example.xpense_tracker.data.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private int id;

    private String category;
    private String subCategory;
    private String type;
    private String note;
    private LocalDate createdAt;
    private String amount;

    public Expense(String category, String subCategory, String type, LocalDate createdAt, String amount) {
        this.category = category;
        this.subCategory = subCategory;
        this.type = type;
        this.createdAt = createdAt;
        this.amount = amount;
    }

    public Expense(int id, String category, String subCategory, String type, LocalDate createdAt, String note, String amount) {
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
}
