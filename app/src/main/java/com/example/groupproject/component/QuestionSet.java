package com.example.groupproject.component;



import static com.example.groupproject.Game.GameMenuActivity.MODE_ADD;
import static com.example.groupproject.Game.GameMenuActivity.MODE_DIVIDE;
import static com.example.groupproject.Game.GameMenuActivity.MODE_MULTIPLY;
import static com.example.groupproject.Game.GameMenuActivity.MODE_RANDOM;
import static com.example.groupproject.Game.GameMenuActivity.MODE_SUBTRACT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionSet {
    private final List<String> questions;
    private final List<Integer> answers;

    public QuestionSet(int numQuestions, int mode) {
        questions = new ArrayList<String>();
        answers = new ArrayList<>();

        for (int i = 0; i < numQuestions; i++) {
            Question question = null;
            switch (mode){
                case MODE_RANDOM:
                    Class[] questionClasses = {AdditionQuestion.class, SubtractionQuestion.class, MultiplicationQuestion.class, DivisionQuestion.class};
                    int randomIndex = new Random().nextInt(questionClasses.length);
                    try {
                        question = (Question) questionClasses[randomIndex].newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                case MODE_ADD:
                    question = new AdditionQuestion();
                    break;
                case MODE_SUBTRACT:
                    question = new SubtractionQuestion();
                    break;
                case MODE_MULTIPLY:
                    question = new MultiplicationQuestion();
                    break;
                case MODE_DIVIDE:
                    question = new DivisionQuestion();
                    break;
                default:
                    break;
            }
            if (question != null) {
                questions.add(question.generateQuestion());
                answers.add(question.getAnswer());
            }
        }
    }


    public List<String> getQuestions() {
        return questions;
    }

    public List<Integer> getAnswers() {
        return answers;
    }
}



