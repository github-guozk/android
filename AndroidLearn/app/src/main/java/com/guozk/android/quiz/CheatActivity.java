package com.guozk.android.quiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import com.guozk.android.R;
import org.w3c.dom.Text;

public class CheatActivity extends AppCompatActivity {
  private static final String EXTRA_ANSWER_IS_TRUE = "answer_is_true";
  private static final String EXTRA_ANSWER_SHOWN = "answer_shown";
  private boolean mAnswer;
  private Button btnShowResult;
  private TextView tvAswer;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cheat);
    mAnswer = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
    bindView();
    registerListeners();
  }

  public static Intent newIntent(Context context, boolean answer) {
    Intent intent = new Intent(context, CheatActivity.class);
    intent.putExtra(EXTRA_ANSWER_IS_TRUE, answer);
    return intent;
  }

  private void bindView() {
    btnShowResult = findViewById(R.id.btnShowResult);
    tvAswer = findViewById(R.id.tvAnswer);
  }

  private void registerListeners() {
    btnShowResult.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (mAnswer) {
          tvAswer.setText(R.string.btn_true_text);
        } else {
          tvAswer.setText(R.string.btn_false_text);
        }
        setAnswerShownResult();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          int cx = btnShowResult.getWidth() / 2;
          int cy = btnShowResult.getHeight() / 2;
          float radius = btnShowResult.getWidth();
          Animator anim = ViewAnimationUtils
              .createCircularReveal(btnShowResult, cx, cy, radius, 0);
          anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              super.onAnimationEnd(animation);
              btnShowResult.setVisibility(View.INVISIBLE);
            }
          });
          anim.start();
        } else {
          btnShowResult.setVisibility(View.INVISIBLE);
        }

      }
    });
  }

  private void setAnswerShownResult() {
    Intent intent = new Intent();
    intent.putExtra(EXTRA_ANSWER_SHOWN, true);
    setResult(RESULT_OK, intent);
  }

  public static boolean wasACheater(Intent data) {
    return data.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
  }
}
