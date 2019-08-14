package com.android.guozk.crimeintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks{
  private static final String EXTRA_CRIME_ID = "com.android.guozk.crimeintent.crime_id";
  private ViewPager mViewPager;
  private List<Crime> mCrimes;

  public static Intent newIntent(Context context, UUID crimeId) {
    Intent intent = new Intent(context, CrimePagerActivity.class);
    intent.putExtra(EXTRA_CRIME_ID, crimeId);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_crime_pager);
    UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
    mViewPager = findViewById(R.id.crime_view_pager);
    mCrimes = CrimeLab.get(this).getCrimes();
    mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
      @Override public Fragment getItem(int position) {
        return CrimeFragment.newInstance(mCrimes.get(position).getId());
      }

      @Override public int getCount() {
        return mCrimes.size();
      }
    });
    for (int i = 0; i < mCrimes.size(); i++) {
      if (mCrimes.get(i).getId().equals(crimeId)) {
        mViewPager.setCurrentItem(i);
        break;
      }
    }
  }

  @Override public void onCrimeUpdate(Crime c) {

  }
}
