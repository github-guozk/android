package com.android.guozk.crimeintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class CrimeListFragment extends Fragment {
  private static final String SAVED_SUBTITLE_VISIBLE = "subtitle_visibility";
  private RecyclerView mCrimeRecyclerView;
  private CrimeAdapter mAdapter;
  private Activity mActivity;
  private boolean mSubTitleVisible;
  private Callbacks mCallbacks;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    mCallbacks = (Callbacks) context;
  }

  @Override public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (null != savedInstanceState) {
      mSubTitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
    }
    setHasOptionsMenu(true);
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mActivity = getActivity();
    View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
    mCrimeRecyclerView = v.findViewById(R.id.crime_recycler_view);
    mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    updateUI();
    return v;
  }

  @Override public void onResume() {
    super.onResume();
    updateUI();
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_crime_list, menu);
    MenuItem menuItem = menu.findItem(R.id.show_subtitle);
    if (mSubTitleVisible) {
      menuItem.setTitle(R.string.hide_subtitle);
    } else {
      menuItem.setTitle(R.string.show_subtitle);
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.new_crime:
        Crime c = new Crime();
        CrimeLab.get(mActivity).addCrime(c);
        updateUI();
        if (mCallbacks != null) {
          mCallbacks.onCrimeSelected(c);
        }
        return true;
      case R.id.show_subtitle:
        mSubTitleVisible = !mSubTitleVisible;
        mActivity.invalidateOptionsMenu();
        updateSubtitle();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubTitleVisible);
  }

  public void updateUI() {
    CrimeLab crimeLab = CrimeLab.get(mActivity);
    List<Crime> crimes = crimeLab.getCrimes();
    if (mAdapter == null) {
      mAdapter = new CrimeAdapter(crimes);
      mCrimeRecyclerView.setAdapter(mAdapter);
    } else {
      mAdapter.setCrimes(crimes);
      mAdapter.notifyDataSetChanged();
    }
    updateSubtitle();
  }

  private void updateSubtitle() {
    List<Crime> crimes = CrimeLab.get(mActivity).getCrimes();
    String subTitle =
        getResources().getQuantityString(R.plurals.subtitle_format, crimes.size(), crimes.size());
    if (!mSubTitleVisible) {
      subTitle = null;
    }
    ((AppCompatActivity) mActivity).getSupportActionBar().setSubtitle(subTitle);
  }

  private class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mCrimeTitle;
    private TextView mCrimeDate;
    private ImageView mCrimeSolved;
    private Crime mCrime;

    public CrimeViewHolder(LayoutInflater inflater, ViewGroup parent) {
      super(inflater.inflate(R.layout.crime_list_item, parent, false));
      itemView.setOnClickListener(this);
      mCrimeTitle = itemView.findViewById(R.id.crime_title);
      mCrimeDate = itemView.findViewById(R.id.crime_date);
      mCrimeSolved = itemView.findViewById(R.id.crime_solved);
    }

    @TargetApi(19) public void bind(Crime crime) {
      mCrime = crime;
      mCrimeTitle.setText(mCrime.getTitle());
      String formatStr = DateFormat.getBestDateTimePattern(getResources().getConfiguration().locale,
          "yyyy MMMdd eee");
      mCrimeDate.setText(DateFormat.format(formatStr, mCrime.getDate()));
      mCrimeSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
    }

    @Override public void onClick(View v) {
      if (mCallbacks != null) {
        updateUI();
        mCallbacks.onCrimeSelected(mCrime);
      }
    }
  }

  private class CrimeAdapter extends RecyclerView.Adapter<CrimeViewHolder> {
    private List<Crime> mCrimes;

    public CrimeAdapter(List<Crime> crimes) {
      mCrimes = crimes;
    }

    public void setCrimes(List<Crime> crimes) {
      mCrimes = crimes;
    }

    @NonNull @Override
    public CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      LayoutInflater inflater = LayoutInflater.from(mActivity);
      return new CrimeViewHolder(inflater, viewGroup);
    }

    @Override public void onBindViewHolder(@NonNull CrimeViewHolder crimeViewHolder, int i) {
      crimeViewHolder.bind(mCrimes.get(i));
    }

    @Override public int getItemCount() {
      return mCrimes.size();
    }

    @Override public int getItemViewType(int position) {
      return super.getItemViewType(position);
    }
  }

  public interface Callbacks {
    void onCrimeSelected(Crime crime);
  }
}
