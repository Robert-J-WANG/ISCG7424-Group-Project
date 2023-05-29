package com.example.groupproject.component;

import android.annotation.SuppressLint;

import java.util.Random;

public class DivisionQuestion extends Question {
    @Override
    public String generateQuestion() {
        // Generate division question
        Random random = new Random();
        this.number2 = random.nextInt(10) + 1;
        this.number1 = number2 * (random.nextInt(10) + 1);

        // Create and return the question string
        @SuppressLint("DefaultLocale") String question = String.format("%d ÷ %d", number1, number2);
        return question;
    }

    public int getAnswer() {
        return number1 / number2;
    }
}
