package com.android.guozk.crimeintent;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
  @LayoutRes
  protected int getLayoutResId() {
    return R.layout.activity_fragment;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutResId());
    FragmentManager fmgr = getSupportFragmentManager();
    Fragment fragment = fmgr.findFragmentById(R.id.fragment_container);
    if (null == fragment) {
      fragment = createFragment();
      fmgr.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }
  }

  protected abstract Fragment createFragment();
}
