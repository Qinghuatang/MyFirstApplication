package com.jnu.student.data;

public class Book {

    private String title;
    private final int coverResourceId;

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public Book(String name, int coverResourceId) {
        this.title = name;
        this.coverResourceId = coverResourceId;
    }
}
