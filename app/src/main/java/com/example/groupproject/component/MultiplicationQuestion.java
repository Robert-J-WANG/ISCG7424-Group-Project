package com.example.groupproject.component;

import android.annotation.SuppressLint;

import java.util.Random;

public class MultiplicationQuestion extends Question {

    @Override
    public String generateQuestion() {
        // Generate multiplication question
        Random random = new Random();
        // 生成不大于10的随机整数
        this.number1 = random.nextInt(11);
        // 生成不大于20的随机整数
        this.number2 = random.nextInt(21);

        // Create and return the question string
        @SuppressLint("DefaultLocale") String question = String.format("%d x %d", number1, number2);
        return question;
    }

    public int getAnswer() {
        return number1 * number2;
    }
}

