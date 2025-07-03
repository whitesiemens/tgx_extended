package com.tgx.extended;

import android.content.SharedPreferences;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.drinkmore.Tracer;
import org.thunderdog.challegram.Log;
import org.thunderdog.challegram.tool.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

import me.vkryl.core.reference.ReferenceList;
import me.vkryl.leveldb.LevelDB;

public class Config {

  private static final int VERSION = 1;
  private static final AtomicBoolean hasInstance = new AtomicBoolean(false);
  private static volatile Config instance;
  private final LevelDB config;
  private static final String KEY_VERSION = "version";

  public static final String KEY_SHOW_USER_ID = "show_user_id";

  public static boolean showUserId = instance().getBoolean(KEY_SHOW_USER_ID, false);

  private Config () {
    File configDir = new File(UI.getAppContext().getFilesDir(), "extended_config");
    if (!configDir.exists() && !configDir.mkdir()) {
      throw new IllegalStateException("Unable to create working directory");
    }
    long ms = SystemClock.uptimeMillis();
    config = new LevelDB(new File(configDir, "db").getPath(), true, new LevelDB.ErrorHandler() {
      @Override public boolean onFatalError (LevelDB levelDB, Throwable error) {
        Tracer.onDatabaseError(error);
        return true;
      }

      @Override public void onError (LevelDB levelDB, String message, @Nullable Throwable error) {
        android.util.Log.e(Log.LOG_TAG, message, error);
      }
    });
    int configVersion = 0;
    try {
      configVersion = Math.max(0, config.tryGetInt(KEY_VERSION));
    } catch (FileNotFoundException ignored) {
    }
    if (configVersion > VERSION) {
      Log.e("Downgrading database version: %d -> %d", configVersion, VERSION);
      config.putInt(KEY_VERSION, VERSION);
    }
    for (int version = configVersion + 1; version <= VERSION; version++) {
      SharedPreferences.Editor editor = config.edit();
      editor.putInt(KEY_VERSION, version);
      editor.apply();
    }
    Log.i("Opened database in %dms", SystemClock.uptimeMillis() - ms);
  }

  public static Config instance () {
    if (instance == null) {
      synchronized (Config.class) {
        if (instance == null) {
          if (hasInstance.getAndSet(true)) throw new AssertionError();
          instance = new Config();
        }
      }
    }
    return instance;
  }

  public LevelDB edit () {
    return config.edit();
  }

  public void remove (String key) {
    config.remove(key);
  }

  public void putLong (String key, long value) {
    config.putLong(key, value);
  }

  public long getLong (String key, long defValue) {
    return config.getLong(key, defValue);
  }

  public void putLongArray (String key, long[] value) {
    config.putLongArray(key, value);
  }

  public long[] getLongArray (String key) {
    return config.getLongArray(key);
  }

  public void putInt (String key, int value) {
    config.putInt(key, value);
  }

  public int getInt (String key, int defValue) {
    return config.getInt(key, defValue);
  }

  public void putFloat (String key, float value) {
    config.putFloat(key, value);
  }

  public void getFloat (String key, float defValue) {
    config.getFloat(key, defValue);
  }

  public void putBoolean (String key, boolean value) {
    config.putBoolean(key, value);
  }

  public boolean getBoolean (String key, boolean defValue) {
    return config.getBoolean(key, defValue);
  }

  public void putString (String key, @NonNull String value) {
    config.putString(key, value);
  }

  public String getString (String key, String defValue) {
    return config.getString(key, defValue);
  }

  public boolean containsKey (String key) {
    return config.contains(key);
  }

  public LevelDB config () {
    return config;
  }

  public interface SettingsChangeListener {
    void onSettingsChanged (String key, Object newSettings, Object oldSettings);
  }

  private ReferenceList<SettingsChangeListener> settingsListeners;

  public void addSettingsListener (SettingsChangeListener listener) {
    if (settingsListeners == null)
      settingsListeners = new ReferenceList<>();
    settingsListeners.add(listener);
  }

  public void removeSettingsListener (SettingsChangeListener listener) {
    if (settingsListeners != null) {
      settingsListeners.remove(listener);
    }
  }

  private void notifyClientListeners (String key, Object newSettings, Object oldSettings) {
    if (settingsListeners != null) {
      for (SettingsChangeListener listener : settingsListeners) {
        listener.onSettingsChanged(key, newSettings, oldSettings);
      }
    }
  }

  public void toggleShowUserId () {
    putBoolean(KEY_SHOW_USER_ID, showUserId ^= true);
  }
}