package com.jnu.student.data;

import java.sql.Date;

public class Count {
    private int id;
    private Date date;
    private int point;
    private String content;
    private String classification;

    public Count() {
    }

    public Count(int id, Date date, int point, String content, String classification) {
        this.id = id;
        this.date = date;
        this.content = content;
        this.point = point;
        this.classification = classification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return "Count{" +
                "id=" + id +
                ", date=" + date +
                ", point=" + point +
                ", content='" + content + '\'' +
                ", classification='" + classification + '\'' +
                '}';
    }
}
