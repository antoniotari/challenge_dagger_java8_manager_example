package com.antoniotari.challenge.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import com.antoniotari.android.JediObject;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by antonio on 28/09/15.
 */
public class Questions extends JediObject implements Parcelable{

    @SerializedName("questions")
    private Map<String,List<String>> questions;

    private List<Question> questionList;

    public static Questions questionsFactory(String json) throws JSONException {
        JSONObject jsonObject=new JSONObject(json);
        JSONObject questions=new JSONObject();
        questions.put("questions", jsonObject);
        return new Gson().fromJson(questions.toString(),Questions.class);
    }

    private List<Question> generateQuestionList(){
        questionList = new ArrayList<>();
        for (Map.Entry<String,List<String>> entry : questions.entrySet()) {
            Question question = new Question();
            question.question=entry.getKey();
            question.answers=entry.getValue();
            question.rightAnswer=question.answers.get(0).hashCode();
            questionList.add(question);
        }
        return questionList;
    }

    public List<Question> getRandomizedQuestionList() {
        if(questionList==null){
           questionList=generateQuestionList();
        }
        Collections.shuffle(questionList);
        return questionList;
    }

    public List<Question> getQuestionList() {
        if(questionList==null){
            questionList=generateQuestionList();
        }
        return questionList;
    }

    protected Questions(Parcel in) {
        if (in.readByte() == 0x01) {
            questionList = new ArrayList<Question>();
            in.readList(questionList, Question.class.getClassLoader());
        } else {
            questionList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (questionList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(questionList);
        }
    }

    public void restoreList(List<Question> questions){
        questionList = questions;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Questions> CREATOR = new Parcelable.Creator<Questions>() {
        @Override
        public Questions createFromParcel(Parcel in) {
            return new Questions(in);
        }

        @Override
        public Questions[] newArray(int size) {
            return new Questions[size];
        }
    };


    public static class Question implements Parcelable {
        private String question;
        private List<String> answers;
        private int rightAnswer;

        private Question(){
        }

        public List<String> getAnswers() {
            return answers;
        }

        public List<String> getRandomizedAnswers(){
            Collections.shuffle(answers);
            return answers;
        }

        public String getQuestion() {
            return question;
        }

        public int getRightAnswer() {
            return rightAnswer;
        }

        protected Question(Parcel in) {
            question = in.readString();
            if (in.readByte() == 0x01) {
                answers = new ArrayList<String>();
                in.readList(answers, String.class.getClassLoader());
            } else {
                answers = null;
            }
            rightAnswer = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(question);
            if (answers == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeList(answers);
            }
            dest.writeInt(rightAnswer);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
            @Override
            public Question createFromParcel(Parcel in) {
                return new Question(in);
            }

            @Override
            public Question[] newArray(int size) {
                return new Question[size];
            }
        };
    }
}
