package com.antoniotari.challenge.activities;

import com.antoniotari.android.jedi.JediUtil;
import com.antoniotari.android.jedi.ScreenDimension;
import com.antoniotari.challenge.QuestionsManager;
import com.antoniotari.challenge.QuizTimerTask;
import com.antoniotari.challenge.R;
import com.antoniotari.challenge.fragments.QuestionsFragment;
import com.antoniotari.challenge.interfaces.FragmentCallback;
import com.antoniotari.challenge.models.Questions.Question;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FragmentCallback {

    public static final int TIME_TOTAL=120;
    public static final String KEY_TIMER_START="timeStart";

    @Inject
    QuestionsManager mQuestionsManager;

    @Inject
    ScreenDimension mScreenDimension;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.timerCount)
    TextView mTimerTextView;

    @Bind(R.id.resultCount)
    TextView mResultTextView;

    private Timer mTimer;
    private QuizTimerTask mQuizTimerTask;

    private boolean isCurrentRight=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JediUtil.inject(this);
        ButterKnife.bind(this);

        setupActionBar();

        int timerStart=0;
        if (savedInstanceState != null){
            mQuestionsManager.restoreState(savedInstanceState);
            nextFragment(mQuestionsManager.getCurrentQuestion());
            timerStart=savedInstanceState.getInt(KEY_TIMER_START);
        } else {
            nextFragment(mQuestionsManager.getNextQuestion());
        }

        updateScore();

        mQuizTimerTask=new QuizTimerTask(timerStart,this::timerUpdate);
        mTimer = new Timer();
        mTimer.schedule(mQuizTimerTask, 0, 1000);
    }

    private void updateScore(){
        mResultTextView.setText("SCORE "+mQuestionsManager.getCorrectAnswers());
    }

    private void timerUpdate(int seconds){
        if(TIME_TOTAL-seconds<=0){
            showToast("Time is UP", Toast.LENGTH_LONG);
            new Handler().postDelayed(this::endQuiz, 1000);
        }else {
            mTimerTextView.setText("time remaining: " + (TIME_TOTAL - seconds));
        }
    }

    protected void nextFragment(Question question){
        isCurrentRight=false;
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, QuestionsFragment.newInstance(question))
                .commit();

        setTitle("Question " + mQuestionsManager.getCurrentPosition() + " of " + mQuestionsManager.getTotQuestions());
    }

    public void setupActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            //restoreActionBar();
            return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            if(isCurrentRight) {
                showToast("CORRECT",Toast.LENGTH_SHORT);
                mQuestionsManager.increaseCorrectAnswers();
            } else {
                showToast("WRONG",Toast.LENGTH_SHORT);
            }

            updateScore();

            if(mQuestionsManager.hasNext()) {
                nextFragment(mQuestionsManager.getNextQuestion());
            } else {
                endQuiz();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void endQuiz(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(StartActivity.KEY_RESULT_MAINACTIVITY,mQuestionsManager.getCorrectAnswers());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onAnswer(final boolean right) {
        isCurrentRight=right;
    }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        mQuestionsManager.saveCurrentState(outState);
        outState.putInt(KEY_TIMER_START, mQuizTimerTask.getTimeCounter());
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mQuestionsManager.restoreState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //in case want to pause the timer
        //mQuizTimerTask.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mQuizTimerTask.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer!=null){
            mTimer.cancel();
            mTimer.purge();
            mQuizTimerTask=null;
            mTimer=null;
        }
    }

    protected void showToast(String theText,int duration){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView textView = (TextView) layout.findViewById(R.id.toastText);
        textView.setText(theText);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 22);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
}