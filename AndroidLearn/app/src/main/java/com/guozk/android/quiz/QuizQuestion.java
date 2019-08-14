package com.guozk.android.quiz;

class QuizQuestion {
  private int mTextResId;
  private boolean mAnswerTrue;

  public int getTextResId() {
    return mTextResId;
  }

  public QuizQuestion(int resId, boolean result) {
    this.mTextResId = resId;
    this.mAnswerTrue = result;
  }

  public void setTextResId(int textResId) {
    mTextResId = textResId;
  }

  public boolean isAnswerTrue() {
    return mAnswerTrue;
  }

  public void setAnswerTrue(boolean answerTrue) {
    mAnswerTrue = answerTrue;
  }
}
