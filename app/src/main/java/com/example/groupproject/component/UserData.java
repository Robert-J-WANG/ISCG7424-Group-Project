package com.example.groupproject.component;

public class UserData {
    private String userName;
    private int savedNumber;
    private int averageScore;

    public UserData(String userName, int savedNumber, int averageScore) {
        this.userName = userName;
        this.savedNumber = savedNumber;
        this.averageScore = averageScore;
    }

    public String getUserName() {
        return userName;
    }

    public int getSavedNumber() {
        return savedNumber;
    }

    public int getAverageScore() {
        return averageScore;
    }
}