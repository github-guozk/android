package com.android.guozk.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
  public static final String TAG = "BeatBox";
  public static final String SOUND_FOLDER = "sample_sounds";

  private List<Sound> mSounds = new ArrayList<>();

  private AssetManager mAssetManager;

  public BeatBox(Context context) {
    mAssetManager = context.getAssets();
    loadSounds();
  }

  private void loadSounds() {
    String[] soundNames = new String[]{};
    try {
      soundNames = mAssetManager.list(SOUND_FOLDER);
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (String name:soundNames) {
      mSounds.add(new Sound(SOUND_FOLDER + "/" + name));
    }
  }

  public List<Sound> getSounds() {
    return mSounds;
  }
}
