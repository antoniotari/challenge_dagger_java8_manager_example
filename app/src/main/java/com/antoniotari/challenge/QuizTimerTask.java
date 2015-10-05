package com.antoniotari.challenge;

import com.antoniotari.challenge.interfaces.QuizTimerListener;

import android.os.Handler;
import android.os.Looper;

import java.util.TimerTask;

/**
 * Created by antonio on 28/09/15.
 */
public class QuizTimerTask extends TimerTask {

    private int timeCounter;
    private QuizTimerListener mTimeListener;
    private Handler mHandler;
    private boolean isPause=false;

    public QuizTimerTask(int startingPoint,QuizTimerListener timeListener){
        timeCounter=startingPoint;
        mTimeListener=timeListener;
        mHandler=new Handler(Looper.getMainLooper());
    }

    @Override
    public void run() {
        if(!isPause) {
            ++timeCounter;
            mHandler.post(() -> mTimeListener.onTimerTick(timeCounter));
        }
    }

    public int getTimeCounter(){
        return timeCounter;
    }

    public void pause(){
        isPause=true;
    }

    public void resume(){
        isPause=false;
    }
}
