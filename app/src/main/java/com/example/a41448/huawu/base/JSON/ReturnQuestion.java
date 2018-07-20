package com.example.a41448.huawu.base.JSON;

import java.util.ArrayList;
import java.util.List;

public class ReturnQuestion {
    private List<Question> questions = new ArrayList<>();
    public void add(Question question){
        questions.add(question);
    }
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
