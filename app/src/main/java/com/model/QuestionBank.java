package com.model;

import java.util.Collections;
import java.util.List;

public class QuestionBank {
    List<Question> mQuestionList;
    int mNextQuestionIndex;

    public QuestionBank(List<Question> questionList) {
        mQuestionList = questionList;
        Collections.shuffle(mQuestionList); // shuffle the question list
        mNextQuestionIndex = 0;
    }

    public Question getQuestion(){
        if(mNextQuestionIndex == mQuestionList.size()){
            mNextQuestionIndex = 0;
        }
        return mQuestionList.get(mNextQuestionIndex++);
    }
}
