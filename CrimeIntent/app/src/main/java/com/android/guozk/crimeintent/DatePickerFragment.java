package com.android.guozk.crimeintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
  private static final String ARGS_DATE = "date";
  public static final String EXTAR_DATE ="com.android.guozk.crimeintent.date";
  private DatePicker mDatePicker;
  public static DatePickerFragment newInstance(Date date) {
    Bundle args = new Bundle();
    args.putSerializable(ARGS_DATE, date);
    DatePickerFragment fragment = new DatePickerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @NonNull @Override public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    Date date = (Date)getArguments().getSerializable(ARGS_DATE);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null);
    mDatePicker = v.findViewById(R.id.dialog_date_picker);
    mDatePicker.init(year, month, day, null);
    return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title)
        .setView(v)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            int year = mDatePicker.getYear();
            int month = mDatePicker.getMonth();
            int dayOfMonth = mDatePicker.getDayOfMonth();
            Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
            sendResut(Activity.RESULT_OK, date);
          }
        })
        .create();
  }

  private void sendResut(int resultCode, Date date) {
    if (getTargetFragment() == null) {
      return;
    }
    Intent intent = new Intent();
    intent.putExtra(EXTAR_DATE, date);
    getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
  }
}
