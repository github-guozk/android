package com.android.guozk.crimeintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.android.guozk.crimeintent.database.CrimeBaseHelper;
import com.android.guozk.crimeintent.database.CrimeCursorWrapper;
import com.android.guozk.crimeintent.database.CrimeDbSchema.CrimeTable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
  private static CrimeLab sCrimeLab;
  private Context mContext;
  private SQLiteDatabase mDatabase;

  private CrimeLab(Context context) {
    mContext = context.getApplicationContext();
    mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
  }

  public static CrimeLab get(Context context) {
    if (null == sCrimeLab) {
      sCrimeLab = new CrimeLab(context);
    }
    return sCrimeLab;
  }

  public List<Crime> getCrimes() {
    ArrayList<Crime> crimes = new ArrayList<>();
    CrimeCursorWrapper cursor = queryCrimes(null, null);
    try {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        crimes.add(cursor.getCrime());
        cursor.moveToNext();
      }
    } finally {
      cursor.close();
    }
    return crimes;
  }

  public Crime getCrime(UUID id) {
    Crime c = null;
    CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[] {id.toString()});
    try {
      if (cursor.getCount() != 0) {
        cursor.moveToFirst();
        c = cursor.getCrime();
      }
    } finally {
      cursor.close();
    }

    return c;
  }

  public void addCrime(Crime c) {
    ContentValues values = getContentValues(c);
    mDatabase.insert(CrimeTable.NAME, null, values);
  }

  public void updateCrime(Crime c) {
    String uuid = c.getId().toString();
    ContentValues values = getContentValues(c);
    mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[] { uuid });
  }

  private static ContentValues getContentValues(Crime crime) {
    ContentValues values = new ContentValues();
    values.put(CrimeTable.Cols.UUID, crime.getId().toString());
    values.put(CrimeTable.Cols.TITLE, crime.getTitle());
    values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
    values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
    values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
    return values;
  }

  private CrimeCursorWrapper queryCrimes(String where, String whereArgs[]) {
    Cursor cursor = mDatabase.query(CrimeTable.NAME, null, where, whereArgs, null, null, null);
    return new CrimeCursorWrapper(cursor);
  }

  public File getPhotoFile(Crime c) {
    File filesDir = mContext.getFilesDir();
    return new File(filesDir, c.getPhotoFilename());
  }

}
