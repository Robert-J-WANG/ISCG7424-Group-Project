package com.example.groupproject.component;

import android.annotation.SuppressLint;

import java.util.Random;

public class AdditionQuestion extends Question {

    @Override
    public String generateQuestion() {
        Random random = new Random();
        this.number1 = random.nextInt(90) + 10; // Generate random two-digit number
        this.number2 = random.nextInt(100 - number1); // Generate random number up to 100 - number1
        // Create and return the question string
        @SuppressLint("DefaultLocale") String question = String.format("%d + %d", number1, number2);
        return question;
    }

    public int getAnswer() {
        return number1 + number2;
    }

}