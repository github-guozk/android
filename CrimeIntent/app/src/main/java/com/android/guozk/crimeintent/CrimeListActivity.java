package com.android.guozk.crimeintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{

  @Override protected Fragment createFragment() {
    return new CrimeListFragment();
  }

  @Override protected int getLayoutResId() {
    return R.layout.activity_masterdetail;
  }

  @Override public void onCrimeSelected(Crime crime) {
    if (findViewById(R.id.detail_fragment_container) == null) {
      startActivity(CrimePagerActivity.newIntent(this, crime.getId()));
    } else {
      Fragment fragment = CrimeFragment.newInstance(crime.getId());
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.detail_fragment_container, fragment)
          .commit();
    }

  }

  @Override public void onCrimeUpdate(Crime c) {
    CrimeListFragment listFragment = (CrimeListFragment)
        getSupportFragmentManager()
            .findFragmentById(R.id.fragment_container);
    listFragment.updateUI();
  }
}
