package com.example.groupproject.component;

import java.util.List;

public class AnswerSet {
    private final List<Integer> answers;

    public AnswerSet(QuestionSet questionSet) {
        this.answers = questionSet.getAnswers();
    }

    public List<Integer> getAnswers() {
        return answers;
    }
}
