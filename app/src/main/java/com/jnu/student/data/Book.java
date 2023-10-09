package com.jnu.student.data;

public class Book {

    private final String title;
    private final int coverResourceId;

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public String getTitle() {
        return title;
    }

    public Book(String name, int coverResourceId) {
        this.title = name;
        this.coverResourceId = coverResourceId;
    }
}
