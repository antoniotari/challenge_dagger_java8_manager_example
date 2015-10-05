package com.antoniotari.challenge.injection;

import com.antoniotari.android.injection.ForApplication;
import com.antoniotari.challenge.GroceryQuizApplication;
import com.antoniotari.challenge.QuestionsManager;
import com.antoniotari.challenge.activities.MainActivity;
import com.antoniotari.challenge.activities.StartActivity;
import com.antoniotari.challenge.adapters.GridAnswerAdapter;
import com.antoniotari.challenge.adapters.GridAnswerAdapter.ViewHolder;
import com.antoniotari.challenge.fragments.QuestionsFragment;
import com.antoniotari.challenge.models.Questions;

import org.json.JSONException;

import android.app.Application;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by anthony on 9/19/15.
 */

@Module (
        injects = {
                GroceryQuizApplication.class,
                MainActivity.class,
                QuestionsFragment.class,
                GridAnswerAdapter.class,
                QuestionsManager.class,
                ViewHolder.class,
                StartActivity.class
        },
        includes = {
        },
        complete = false,
        library=true
)
public class QuizModule {

    private final Application mApplication;

    public QuizModule(Application application) {
        mApplication = application;
    }

    @Provides @Singleton @ForApplication
    Application provideApplication() {
        return mApplication;
    }

    @Provides @Singleton
    Questions provideQuestions(@ForApplication Context context){
        Questions questions=null;
        try {
            InputStream inputStream = context.getAssets().open("zquestions.json");
            InputStreamReader reader=new InputStreamReader(inputStream);
            BufferedReader br=new BufferedReader(reader);
            String line;
            StringBuilder sb=new StringBuilder();
            while((line=br.readLine())!=null){
                sb.append(line);
            }
            questions= Questions.questionsFactory(sb.toString());
        } catch (IOException |JSONException e) {
            e.printStackTrace();
        }
        return questions;
    }

    @Provides @Singleton @Named("questions")
    rx.Observable<Questions> provideQuestionsObservable(@ForApplication Context context){
        return Observable.create(new OnSubscribe<Questions>() {
            @Override
            public void call(final Subscriber<? super Questions> subscriber) {
                try {
                    InputStream inputStream = context.getAssets().open("zquestions.json");
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader br = new BufferedReader(reader);
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    subscriber.onNext(Questions.questionsFactory(sb.toString()));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Provides @Singleton
    QuestionsManager provideQuestionsManager(){
        return new QuestionsManager();
    }
}