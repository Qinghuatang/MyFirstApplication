package com.jnu.student.data;

import java.sql.Timestamp;

public class Task {
    private int id;
    private String taskType;
    private Timestamp time;
    private String content;
    private int point;
    private int finishedNum;
    private int num;
    private String classification;

    public Task() {
    }

    public Task(int id, String taskType, Timestamp time, String content, int point, int finishedNum, int num, String classification) {
        this.id = id;
        this.taskType = taskType;
        this.time = time;
        this.content = content;
        this.point = point;
        this.finishedNum = finishedNum;
        this.num = num;
        this.classification = classification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFinishedNum() {
        return finishedNum;
    }

    public void setFinishedNum(int finishedNum) {
        this.finishedNum = finishedNum;
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
                "id=" + id +
                ", taskType='" + taskType + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", point=" + point +
                ", finishedNum=" + finishedNum +
                ", num=" + num +
                ", classification='" + classification + '\'' +
                '}';
    }
}
