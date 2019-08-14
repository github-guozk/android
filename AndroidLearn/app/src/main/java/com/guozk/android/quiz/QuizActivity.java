package com.guozk.android.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.guozk.android.R;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
  private static final String TAG = "QuizActivity";
  private static final String index = "index";
  private static final int REQUEST_CHEAT_CODE = 0;

  private Button mTrueBtn;
  private Button mFalseBtn;
  private TextView tvQuestion;
  private Button mNextBtn;
  private Button mPreBtn;
  private Button mCheatButton;
  private List<QuizQuestion> mQuestions;
  private int mCurrentQuestionIndex;
  private boolean mIsCheater = false;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate called");
    setContentView(R.layout.activity_quiz);
    if (savedInstanceState != null) {
      mCurrentQuestionIndex = savedInstanceState.getInt(index);
    }
    mIsCheater = false;
    setTitle(R.string.app_name);
    initData();
    bindView();
    registerListener();
  }

  private void refreshQuestion() {
    mIsCheater = false;
    tvQuestion.setText(mQuestions.get(mCurrentQuestionIndex).getTextResId());
  }

  private void initData() {
    mQuestions = new ArrayList<>();
    mQuestions.add(new QuizQuestion(R.string.question_australia, true));
    mQuestions.add(new QuizQuestion(R.string.question_oceans, true));
    mQuestions.add(new QuizQuestion(R.string.question_mideast, false));
    mQuestions.add(new QuizQuestion(R.string.question_africa, false));
    mQuestions.add(new QuizQuestion(R.string.question_americas, true));
    mQuestions.add(new QuizQuestion(R.string.question_asia, true));
  }

  private void bindView() {
    mTrueBtn = findViewById(R.id.true_btn);
    mFalseBtn = findViewById(R.id.false_btn);
    mNextBtn = findViewById(R.id.next_btn);
    mPreBtn = findViewById(R.id.pre_btn);
    tvQuestion = findViewById(R.id.tvQuestion);
    mCheatButton = findViewById(R.id.btnCheat);
    refreshQuestion();
  }

  private void checkAnswer(QuizQuestion quizQuestion, boolean givenAnswer) {
    int resourceId = 0;
    if (mIsCheater) {
      resourceId = R.string.judgment_toast;
    } else {
      if (quizQuestion.isAnswerTrue() == givenAnswer) {
        resourceId =  R.string.correct_toast;
      } else {
        resourceId = R.string.incorrect_toast;
      }
    }
    Toast.makeText(QuizActivity.this, resourceId, Toast.LENGTH_SHORT).show();
  }

  private void registerListener() {
    mTrueBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        checkAnswer(mQuestions.get(mCurrentQuestionIndex), true);
      }
    });
    mFalseBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        checkAnswer(mQuestions.get(mCurrentQuestionIndex), false);
      }
    });
    mNextBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mCurrentQuestionIndex = (mCurrentQuestionIndex + 1) % mQuestions.size();
        refreshQuestion();
      }
    });
    mPreBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mCurrentQuestionIndex = (mCurrentQuestionIndex + mQuestions.size() - 1) % mQuestions.size();
        refreshQuestion();
      }
    });
    mCheatButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = CheatActivity.newIntent(QuizActivity.this,mQuestions.get(mCurrentQuestionIndex).isAnswerTrue());
        startActivityForResult(intent, REQUEST_CHEAT_CODE);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (REQUEST_CHEAT_CODE != requestCode || RESULT_OK != resultCode) return;
    mIsCheater = CheatActivity.wasACheater(data);
  }

  @Override protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause called");
  }

  @Override protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume called");
  }

  @Override protected void onStart() {
    super.onStart();
    Log.d(TAG, "onStart called");
  }

  @Override protected void onStop() {
    super.onStop();
    Log.d(TAG, "onStop called");
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy called");
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(index, mCurrentQuestionIndex);
  }
}
