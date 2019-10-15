package com.model;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String mQuestion = "";
    private List<String> mChoiceList = new ArrayList<>();
    private int mAnswerIndex = 0;

    public Question(String question, List<String> choiceList, int answerIndex) {

        this.mQuestion = question;
        this.mChoiceList = choiceList;
        this.mAnswerIndex = answerIndex;

        /*this.setQuestion(question);
        this.setAnswerIndex(answerIndex);
        this.setChoiceList(choiceList);*/
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public List<String> getChoiceList() {
        return mChoiceList;
    }

    public void setChoiceList(List<String> choiceList) {
        if(choiceList == null){
            throw new IllegalArgumentException("Array cannot be null.");
        }
        mChoiceList = choiceList;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        if(answerIndex < 0 || answerIndex >= mChoiceList.size()){
            throw new IllegalArgumentException("Answer index is out of bound.");
        }
        mAnswerIndex = answerIndex;
    }

    @Override
    public String toString() {
        return "Question{" +
                "mQuestion='" + mQuestion + '\'' +
                ", mChoiceList=" + mChoiceList +
                ", mAnswerIndex=" + mAnswerIndex +
                '}';
    }
}
