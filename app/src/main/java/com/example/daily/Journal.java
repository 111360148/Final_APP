package com.example.daily;

public class Journal {
    private String date;  // 日期
    private String title; // 日誌標題
    private String content; // 日誌內容
    private String imageUri; // 圖片路徑
    private int moodIndex; // 心情指數 (1~10)

    // Getters 和 Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getMoodIndex() {
        return moodIndex;
    }

    public void setMoodIndex(int moodIndex) {
        this.moodIndex = moodIndex;
    }
}
