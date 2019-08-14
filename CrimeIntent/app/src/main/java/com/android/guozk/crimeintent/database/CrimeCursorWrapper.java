package com.android.guozk.crimeintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.android.guozk.crimeintent.Crime;
import com.android.guozk.crimeintent.database.CrimeDbSchema.CrimeTable;
import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
  /**
   * Creates a cursor wrapper.
   *
   * @param cursor The underlying cursor to wrap.
   */
  public CrimeCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  /**
   *  Obtains a crime object from current cursor
   * @return
   */
  public Crime getCrime() {
    String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
    String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
    Long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
    int solved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
    String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));
    Crime c = new Crime(UUID.fromString(uuidString));
    c.setTitle(title);
    c.setDate(new Date(date));
    c.setSolved(solved != 0);
    c.setSuspect(suspect);
    return c;
  }
}
