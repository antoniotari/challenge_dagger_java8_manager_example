package com.antoniotari.challenge.fragments;

import com.antoniotari.android.injection.ApplicationGraph;
import com.antoniotari.challenge.interfaces.FragmentCallback;
import com.antoniotari.challenge.R;
import com.antoniotari.challenge.adapters.GridAnswerAdapter;
import com.antoniotari.challenge.models.Questions.Question;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by anthony on 9/19/15.
 */
public class QuestionsFragment extends Fragment {

    private static final int COLUMN_NUM = 2;
    private static final String ARG_FIRSTQUESTION = "first_question";

    private Question firstQuestion;
    private boolean rightAnswer=false;

    @Bind(R.id.question_text)
    TextView mQuestionTextView;

    @Bind(R.id.answer_grid_view)
    RecyclerView mAnswerGridView;

    FragmentCallback mFragmentCallback;


    public static QuestionsFragment newInstance(Question firstQuestion) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_FIRSTQUESTION, firstQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        ApplicationGraph.getObjectGraph().inject(this);

        firstQuestion = getArguments().getParcelable(ARG_FIRSTQUESTION);

        mAnswerGridView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),COLUMN_NUM);
        mAnswerGridView.setLayoutManager(gridLayoutManager);

        if(firstQuestion!=null) {
            mQuestionTextView.setText(firstQuestion.getQuestion());
            GridAnswerAdapter gridAnswerAdapter = new GridAnswerAdapter(firstQuestion.getRandomizedAnswers());
            gridAnswerAdapter.setOnClickListener(this::checkAnswer);
            mAnswerGridView.setAdapter(gridAnswerAdapter);
        }

        return rootView;
    }


    private void checkAnswer(int position,int hash){
        mFragmentCallback.onAnswer(hash==firstQuestion.getRightAnswer());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof FragmentCallback){
            mFragmentCallback=(FragmentCallback)activity;
        } else throw new RuntimeException("activity must implement Fragment callback");
    }
}