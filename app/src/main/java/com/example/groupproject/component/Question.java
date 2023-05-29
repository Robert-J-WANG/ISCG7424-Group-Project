package com.example.groupproject.component;

public abstract class Question {
    protected int number1;
    protected int number2;


    public String generateQuestion() {
        // 由子类实现
        return null;
    }

    public int getAnswer() {
        // 由子类实现
        return 0;
    }

}

