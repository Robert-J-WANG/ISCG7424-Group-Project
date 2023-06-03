package com.example.groupproject;

import static org.junit.Assert.assertNotEquals;

import junit.framework.TestCase;

public class SubtractionQuestionTest extends TestCase {

    public void testGenerateQuestion_Positive() {
        SubtractionQuestion subtractionQuestion = new SubtractionQuestion();
        String question = subtractionQuestion.generateQuestion();
        assertNotNull(question);
        assertTrue(question.matches("\\d+ - \\d+"));
    }

    public void testGetAnswer_Positive() {
        SubtractionQuestion subtractionQuestion = new SubtractionQuestion();
        subtractionQuestion.setNumbers(10, 5);
        int answer = subtractionQuestion.getAnswer();
        assertEquals(5, answer);
    }

    public void testGenerateQuestion_Negative() {
        SubtractionQuestion subtractionQuestion = new SubtractionQuestion();
        String question = subtractionQuestion.generateQuestion();
        assertNotEquals("",question);
    }

    public void testGetAnswer_Negative() {
        SubtractionQuestion subtractionQuestion = new SubtractionQuestion();
        subtractionQuestion.setNumbers(5, 10);
        int answer = subtractionQuestion.getAnswer();
        assertNotEquals(5, answer);
    }

    private static class SubtractionQuestion extends com.example.groupproject.component.SubtractionQuestion {
        public void setNumbers(int number1, int number2) {
            this.number1 = number1;
            this.number2 = number2;
        }
    }
}
