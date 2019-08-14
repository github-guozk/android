package com.android.guozk.crimeintent;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 *  表示一个不良记录
 */
public class Crime implements Serializable {
  private UUID mId;
  private String mTitle;
  private Date mDate;
  private boolean mSolved;
  private String mSuspect;

  public Crime () {
    this(UUID.randomUUID());
  }

  /**
   *  根据给定的id 构造一条不良记录
   * @param id 构造一条UUID 为 <code>id</code> 的不良记录
   */
  public Crime(UUID id) {
    this.mId = id;
    mDate = new Date();
  }

  /**
   *  查询当前不良记录的UUID
   * @return 当前记录的UUID
   */
  public UUID getId() {
    return mId;
  }

  public String getTitle() {
    return mTitle;
  }

  public void setTitle(String title) {
    mTitle = title;
  }

  public Date getDate() {
    return mDate;
  }

  public void setDate(Date date) {
    mDate = date;
  }

  public boolean isSolved() {
    return mSolved;
  }

  public void setSolved(boolean solved) {
    mSolved = solved;
  }

  public String getSuspect() {
    return mSuspect;
  }

  public void setSuspect(String suspect) {
    mSuspect = suspect;
  }

  public String getPhotoFilename() {
    return "IMG_"+ getId().toString() + ".jpg";
  }
}
