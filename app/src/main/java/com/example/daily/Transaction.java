package com.example.daily;

public class Transaction {
    private int id; // 新增 id 字段
    private String title;
    private double amount;
    private String date;

    // 构造方法
    public Transaction(String title, double amount, String date) {
        this.title = title;
        this.amount = amount;
        this.date = date;
    }

    // 默认构造方法
    public Transaction() {}

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
