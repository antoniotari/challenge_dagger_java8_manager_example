package com.antoniotari.challenge;

import com.antoniotari.android.injection.ApplicationGraph;
import com.antoniotari.challenge.models.Questions;
import com.antoniotari.challenge.models.Questions.Question;

import android.os.Bundle;

import javax.inject.Inject;

/**
 * Created by antonio on 28/09/15.
 */
public class QuestionsManager {

    @Inject
    Questions mQuestions;

    //private Question mCurrentQuestion;

    private int currentIndex=0;

    private int mCorrectAnswers=0;

    public QuestionsManager(){
        ApplicationGraph.getObjectGraph().inject(this);
    }

    public boolean hasNext(){
        //int pos=mQuestions.getQuestionList().indexOf(mCurrentQuestion);
        return((currentIndex+1) != mQuestions.getQuestionList().size());
    }

    public Question getNextQuestion(){
        Question question;
        if(currentIndex==0){
            //the first time randomize the list
            question = mQuestions.getRandomizedQuestionList().get(currentIndex++);
        } else {
            //int pos=mQuestions.getQuestionList().indexOf(mCurrentQuestion);
            question=mQuestions.getQuestionList().get(++currentIndex);
        }
        return question;
    }

    public void increaseCorrectAnswers(){
        ++mCorrectAnswers;
    }

    public void reset(){
        currentIndex=0;
        mCorrectAnswers=0;
    }

    public int getCurrentPosition(){
        return currentIndex;
        //if(mCurrentQuestion==null)return 1;
        //return mQuestions.getQuestionList().indexOf(mCurrentQuestion)+1;
    }

    public int getTotQuestions(){
        return mQuestions.getQuestionList().size()-1;
    }

    public int getCorrectAnswers() {
        return mCorrectAnswers;
    }

    public Question getCurrentQuestion() {
        return mQuestions.getQuestionList().get(currentIndex);
    }

    public void saveCurrentState(Bundle bundle){
        bundle.putParcelable("questions",mQuestions);
        //bundle.putParcelable("current_questions",mCurrentQuestion);
        bundle.putInt("current_position",currentIndex);
        bundle.putInt("correctAnswers",mCorrectAnswers);
    }

    public void restoreState(Bundle bundle){
        Questions questions=bundle.getParcelable("questions");
        //TODO use an init method inside the Question singleton
        mQuestions.restoreList(questions.getQuestionList());
        //mCurrentQuestion=bundle.getParcelable("current_questions");
        currentIndex=bundle.getInt("current_position");
        mCorrectAnswers=bundle.getInt("correctAnswers");
    }
}
