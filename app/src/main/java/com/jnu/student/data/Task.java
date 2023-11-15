package com.jnu.student.data;

import java.sql.Timestamp;

public class Task {
    private String taskType;
    private Timestamp time;
    private String content;
    private int point;
    private int num;
    private String classification;

    public Task() {
    }

    public Task(String content, Timestamp time, int point) {
        this.content = content;
        this.time = time;
        this.point = point;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskType='" + taskType + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", point=" + point +
                ", num=" + num +
                ", classification='" + classification + '\'' +
                '}';
    }
}
