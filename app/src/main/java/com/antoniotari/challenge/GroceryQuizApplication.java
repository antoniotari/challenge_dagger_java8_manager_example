package com.antoniotari.challenge;

import com.antoniotari.android.jedi.JediUtil;
import com.antoniotari.challenge.injection.QuizModule;
import com.facebook.drawee.backends.pipeline.Fresco;

import android.app.Application;

/**
 * Created by anthony on 9/19/15.
 */
public class GroceryQuizApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JediUtil.init(this, new QuizModule(this));
        Fresco.initialize(this);
    }
}