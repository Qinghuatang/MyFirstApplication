package com.jnu.student.data;

import java.sql.Timestamp;

public class Award {
    private int id;
    private Timestamp time;
    private String content;
    private int point;
    private int buyNum;
    private String classification;

    public Award() {
    }

    public Award(int id, Timestamp time, String content, int point, int buyNum, String classification) {
        this.id = id;
        this.time = time;
        this.content = content;
        this.point = point;
        this.buyNum = buyNum;
        this.classification = classification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return "Award{" +
                "id=" + id +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", point=" + point +
                ", buyNum=" + buyNum +
                ", classification='" + classification + '\'' +
                '}';
    }
}
