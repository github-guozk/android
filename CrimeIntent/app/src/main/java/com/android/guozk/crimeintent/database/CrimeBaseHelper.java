package com.android.guozk.crimeintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.guozk.crimeintent.database.CrimeDbSchema.CrimeTable;

public class CrimeBaseHelper extends SQLiteOpenHelper {
  public static final int VERSION = 2;
  public static final String DATABASE_NAME = "crimeBase.db";

  public CrimeBaseHelper(Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + CrimeTable.NAME + "(" + "_id integer primary key autoincrement, "
        + CrimeTable.Cols.UUID + "," + CrimeTable.Cols.TITLE + "," + CrimeTable.Cols.DATE + ","
        + CrimeTable.Cols.SOLVED + "," + CrimeTable.Cols.SUSPECT + ")");
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
