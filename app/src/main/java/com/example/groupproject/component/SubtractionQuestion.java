package com.example.groupproject.component;

import android.annotation.SuppressLint;

import java.util.Random;

public class SubtractionQuestion extends Question {

    @Override
    public String generateQuestion() {
        // Generate subtraction question
        Random random = new Random();
        this.number1 = random.nextInt(90) + 10;
        this.number2 = random.nextInt(number1 - 9) + 10;
        // Create and return the question string
        @SuppressLint("DefaultLocale") String question = String.format("%d - %d", number1, number2);
        return question;
    }

    public int getAnswer() {
        return number1 - number2;
    }
}
