package com.antoniotari.challenge.activities;

import com.antoniotari.android.jedi.JediUtil;
import com.antoniotari.challenge.QuestionsManager;
import com.antoniotari.challenge.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by antonio on 28/09/15.
 */
public class StartActivity extends AppCompatActivity {

    public static final int REQ_CODE_MAINACTIVITY=111;
    public static final String KEY_RESULT_MAINACTIVITY="result";

    @Bind(R.id.buttonStart)
    Button mButtonStart;

    @Bind(R.id.titleText)
    TextView mTextViewTitle;

    @Bind(R.id.resultText)
    TextView mTextViewResult;

    @Inject
    QuestionsManager mQuestionsManager;

    private int mLastResult=0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        JediUtil.inject(this);
        ButterKnife.bind(this);

        if(savedInstanceState==null) {
            mButtonStart.setText("START");
        } else {
            updateLastResult(savedInstanceState.getInt("last_result"));
        }

        mButtonStart.setOnClickListener(this::onStartClick);
    }

    protected void onStartClick(View view){
        mQuestionsManager.reset();
        startActivityForResult(new Intent(this, MainActivity.class), REQ_CODE_MAINACTIVITY);
    }

    private void updateLastResult(int result){
        mTextViewResult.setVisibility(View.VISIBLE);
        mTextViewResult.setText("YOUR SCORE IS: "+result);
        mLastResult=result;
        mButtonStart.setText("RESTART");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_MAINACTIVITY) {
            if(resultCode == RESULT_OK){
                int result=data.getIntExtra(KEY_RESULT_MAINACTIVITY, 0);
                updateLastResult(result);
            }
            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("last_result",mLastResult);
    }
}
