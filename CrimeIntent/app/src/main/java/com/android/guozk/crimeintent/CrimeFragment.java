package com.android.guozk.crimeintent;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.widget.CompoundButton.OnCheckedChangeListener;
import static android.widget.CompoundButton.OnClickListener;

public class CrimeFragment extends Fragment {
  private static final String ARG_CRIME_ID = "crimeId";
  private static final String DIALOG_TAG_DATE = "DialogDate";
  private static final int REQUEST_CODE = 0;
  private static final int REQUEST_CONTACT = 1;
  private static final int REQUEST_PHOTO = 2;
  private Crime mCrime;
  private EditText mCrimeTitleField;
  private Button mDateButton;
  private CheckBox mSolvedCheckBox;
  private Button mReportButton;
  private Button mSuspectButton;
  private ImageButton mPhotoButton;
  private ImageView mPhotoView;
  private File mPhotoFile;
  private Callbacks mCallbacks;

  public static CrimeFragment newInstance(UUID crimeId) {
    CrimeFragment crimeFragment = new CrimeFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_CRIME_ID, crimeId);
    crimeFragment.setArguments(args);
    return crimeFragment;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    this.mCallbacks = (Callbacks) context;
  }

  @Override public void onDetach() {
    super.onDetach();
    this.mCallbacks = null;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UUID crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
    mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
    mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_crime, container, false);
    bindView(v);
    return v;
  }

  @Override public void onPause() {
    super.onPause();
    updateCrime();
  }

  private void bindView(View v) {
    mPhotoButton = v.findViewById(R.id.crime_camera);
    mPhotoView = v.findViewById(R.id.crime_photo);
    updatePhotoView();
    mCrimeTitleField = v.findViewById(R.id.crime_title);
    mCrimeTitleField.setText(mCrime.getTitle());
    mCrimeTitleField.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCrime.setTitle(s.toString());
        updateCrime();
      }

      @Override public void afterTextChanged(Editable s) {

      }
    });

    mDateButton = v.findViewById(R.id.crime_date);
    updateDate();
    mDateButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        DatePickerFragment datePickerDialog = DatePickerFragment.newInstance(mCrime.getDate());
        datePickerDialog.setTargetFragment(CrimeFragment.this, REQUEST_CODE);
        datePickerDialog.show(fragmentManager, DIALOG_TAG_DATE);
      }
    });

    mSolvedCheckBox = v.findViewById(R.id.crime_solved);
    mSolvedCheckBox.setChecked(mCrime.isSolved());
    mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCrime.setSolved(isChecked);
        updateCrime();
      }
    });

    mReportButton = v.findViewById(R.id.crime_report);
    mReportButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(getActivity());
        Intent i = builder.setType("text/plain")
            .setText(getCrimeReport())
            .setSubject(getString(R.string.crime_report_subject))
            .setChooserTitle(R.string.send_report)
            .createChooserIntent();
        startActivity(i);
      }
    });

    mSuspectButton = v.findViewById(R.id.crime_suspect);
    final Intent pickContact =
        new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    mSuspectButton.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        startActivityForResult(pickContact, REQUEST_CONTACT);
      }
    });
    if (mCrime.getSuspect() != null) {
      mSuspectButton.setText(mCrime.getSuspect());
    }
    PackageManager packageManager = getActivity().getPackageManager();
    if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
      mSuspectButton.setEnabled(false);
    }
    final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    boolean canCapture = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
    mPhotoButton.setEnabled(canCapture);
    mPhotoButton.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        Uri uri = FileProvider.getUriForFile(getActivity(), "com.android.guozk.crimeintent.fileProvider", mPhotoFile);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo activity: cameraActivities) {
          getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        };
        startActivityForResult(captureImage, REQUEST_PHOTO);
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (REQUEST_CODE == requestCode) {
      Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTAR_DATE);
      mCrime.setDate(date);
      updateDate();
      updateCrime();
    } else if (REQUEST_CONTACT == requestCode && null != data) {
      Uri contactUri = data.getData();
      String[] queryFields = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
      Cursor c =
          getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
      try {
        if (c.getCount() == 0) {
          return;
        }
        c.moveToFirst();
        String suspect = c.getString(0);
        mCrime.setSuspect(suspect);
        mSuspectButton.setText(mCrime.getSuspect());
        updateCrime();
      } finally {
        c.close();
      }
    } else if (REQUEST_PHOTO == requestCode) {
      Uri uri = FileProvider.getUriForFile(getActivity(),
          "com.android.guozk.crimeintent.fileProvider",
          mPhotoFile);
      getActivity().revokeUriPermission(uri,
          Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
      updatePhotoView();
    }
  }

  private void updateDate() {
    mDateButton.setText(mCrime.getDate().toString());
  }

  private String getCrimeReport() {
    String solvedString = null;
    if (mCrime.isSolved()) {
      solvedString = getString(R.string.crime_report_solved);
    } else {
      solvedString = getString(R.string.crime_report_unsolved);
    }
    String dateFormat = "EEE, MMM dd";
    String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
    String suspect = mCrime.getSuspect();
    if (suspect == null) {
      suspect = getString(R.string.crime_report_no_suspect);
    } else {
      suspect = getString(R.string.crime_report_suspect, suspect);
    }
    String report =
        getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
    return report;
  }

  private void updatePhotoView() {
    if (mPhotoFile == null || !mPhotoFile.exists()) {
      mPhotoView.setImageDrawable(null);
    } else {
      Bitmap bitmap = PictureUtils.getScaledBitmap(
          mPhotoFile.getPath(), getActivity());
      mPhotoView.setImageBitmap(bitmap);
    }
  }

  public interface Callbacks {
    public void onCrimeUpdate(Crime c);
  }

  private void updateCrime() {
    CrimeLab.get(getActivity()).updateCrime(mCrime);
    mCallbacks.onCrimeUpdate(mCrime);
  }
}
