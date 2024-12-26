package com.example.daily;

public class Transaction {
    private int id;
    private String title;
    private double amount;
    private String date;
    private String type; // 收入或支出類型

    // 获取 id
    public int getId() {
        return id;
    }

    // 设置 id
    public void setId(int id) {
        this.id = id;
    }

    // 获取 title
    public String getTitle() {
        return title;
    }

    // 设置 title
    public void setTitle(String title) {
        this.title = title;
    }

    // 获取 amount
    public double getAmount() {
        return amount;
    }

    // 设置 amount
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // 获取 date
    public String getDate() {
        return date;
    }

    // 设置 date
    public void setDate(String date) {
        this.date = date;
    }

    // 获取 type (收入或支出)
    public String getType() {
        return type;
    }

    // 设置 type
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
