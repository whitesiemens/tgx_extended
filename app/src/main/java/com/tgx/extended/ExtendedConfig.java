package com.tgx.extended;

import android.os.SystemClock;
import androidx.annotation.NonNull;
import org.drinkmore.Tracer;
import org.thunderdog.challegram.Log;
import org.thunderdog.challegram.tool.UI;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;
import me.vkryl.core.reference.ReferenceList;
import me.vkryl.leveldb.LevelDB;

public class ExtendedConfig {

  private static final int VERSION = 1;
  private static final AtomicBoolean hasInstance = new AtomicBoolean(false);
  private static volatile ExtendedConfig instance;
  private final LevelDB config;
  private static final String KEY_VERSION = "version";

  public static final String BUILD_VERSION = "1.0";
  public static final String BUILD_CODENAME = "Andromeda";
  public static final String BUILD = BUILD_VERSION + " " + BUILD_CODENAME;

  public enum Setting {
    SHOW_USER_ID("show_user_id", false, false),
    DRAWER_BLUR("drawer_blur", false, true),
    DRAWER_DARKEN("drawer_darken", false, false),
    HIDE_PHONE_NUMBER("hide_phone_number", false, true),
    DRAWER_HIDE_CONTACTS("drawer_hide_contacts", false, false),
    DRAWER_HIDE_CALLS("drawer_hide_calls", false, false),
    DRAWER_HIDE_FAVOURITE("drawer_hide_favourite", false, false),
    DRAWER_HIDE_INVITE("drawer_hide_invite", false, false),
    DRAWER_HIDE_HELP("drawer_hide_help", false, false),
    DRAWER_HIDE_NIGHT("drawer_hide_night", false, false),
    DISABLE_CAMERA_BUTTON("disable_camera_button", false, true),
    DISABLE_RECORD_BUTTON("disable_record_button", false, true),
    DISABLE_COMMAND_BUTTON("disable_command_button", false, true),
    DISABLE_SENDER_BUTTON("disable_sender_button", false, true);

    public final String key;
    public final boolean defaultValue;
    public final boolean shouldNotify;
    public boolean value;

    Setting(String key, boolean defaultValue, boolean shouldNotify) {
      this.key = key;
      this.defaultValue = defaultValue;
      this.shouldNotify = shouldNotify;
    }
  }

  static {
    ExtendedConfig cfg = instance();
    for (Setting s : Setting.values()) {
      if (!cfg.containsKey(s.key)) {
        cfg.putBoolean(s.key, s.defaultValue);
        s.value = s.defaultValue;
      } else {
        s.value = cfg.getBoolean(s.key, s.defaultValue);
      }
    }
  }

  private ExtendedConfig() {
    File configDir = new File(UI.getAppContext().getFilesDir(), "extended_config");
    if (!configDir.exists() && !configDir.mkdir()) {
      throw new IllegalStateException("Unable to create working directory");
    }

    long ms = SystemClock.uptimeMillis();
    config = new LevelDB(new File(configDir, "db").getPath(), true, new LevelDB.ErrorHandler() {
      @Override public boolean onFatalError(LevelDB db, Throwable error) {
        Tracer.onDatabaseError(error);
        return true;
      }

      @Override public void onError(LevelDB db, String message, Throwable error) {
        android.util.Log.e(Log.LOG_TAG, message, error);
      }
    });

    int configVersion = 0;
    try {
      configVersion = Math.max(0, config.tryGetInt(KEY_VERSION));
    } catch (FileNotFoundException ignored) {}

    if (configVersion > VERSION) {
      Log.e("Downgrading database version: %d -> %d", configVersion, VERSION);
      config.putInt(KEY_VERSION, VERSION);
    }

    for (int v = configVersion + 1; v <= VERSION; v++) {
      config.edit().putInt(KEY_VERSION, v).apply();
    }

    Log.i("Opened database in %dms", SystemClock.uptimeMillis() - ms);
  }

  public static ExtendedConfig instance() {
    if (instance == null) {
      synchronized (ExtendedConfig.class) {
        if (instance == null) {
          if (hasInstance.getAndSet(true)) throw new AssertionError();
          instance = new ExtendedConfig();
        }
      }
    }
    return instance;
  }

  public void toggleSetting(Setting setting) {
    boolean oldValue = setting.value;
    boolean newValue = !oldValue;
    setting.value = newValue;
    putBoolean(setting.key, newValue);
    if (setting.shouldNotify) {
      notifyClientListeners(setting, newValue, oldValue);
    }
  }

  public boolean get(Setting setting) {
    return setting.value;
  }

  public interface SettingsChangeListener {
    void onSettingsChanged(Setting setting, boolean newVal, boolean oldVal);
  }

  private ReferenceList<SettingChangeListener> listeners;

  public void addSettingsListener(SettingChangeListener l) {
    if (listeners == null) listeners = new ReferenceList<>();
    listeners.add(l);
  }

  public void removeSettingsListener(SettingChangeListener l) {
    if (listeners != null) listeners.remove(l);
  }

  private void notifyClientListeners(Setting setting, boolean newVal, boolean oldVal) {
    if (listeners != null) {
      for (SettingsChangeListener l : listeners)
        l.onSettingsChanged(setting, newVal, oldVal);
    }
  }

  public LevelDB edit() { return config.edit(); }
  public void remove(String key) { config.remove(key); }
  public void putLong(String key, long value) { config.putLong(key, value); }
  public long getLong(String key, long def) { return config.getLong(key, def); }
  public void putLongArray(String key, long[] value) { config.putLongArray(key, value); }
  public long[] getLongArray(String key) { return config.getLongArray(key); }
  public void putInt(String key, int value) { config.putInt(key, value); }
  public int getInt(String key, int def) { return config.getInt(key, def); }
  public void putFloat(String key, float value) { config.putFloat(key, value); }
  public void getFloat(String key, float def) { config.getFloat(key, def); }
  public void putBoolean(String key, boolean value) { config.putBoolean(key, value); }
  public boolean getBoolean(String key, boolean def) { return config.getBoolean(key, def); }
  public void putString(String key, @NonNull String value) { config.putString(key, value); }
  public String getString(String key, String def) { return config.getString(key, def); }
  public boolean containsKey(String key) { return config.contains(key); }
  public LevelDB config() { return config; }

}