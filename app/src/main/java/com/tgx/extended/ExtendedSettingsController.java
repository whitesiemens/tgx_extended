package com.tgx.extended;

import android.content.Context;
import android.view.View;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.navigation.SettingsWrapBuilder;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.telegram.TdlibUi;
import org.thunderdog.challegram.ui.ListItem;
import org.thunderdog.challegram.ui.RecyclerViewController;
import org.thunderdog.challegram.ui.SettingsAdapter;
import org.thunderdog.challegram.unsorted.Settings;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.ArrayList;
import java.util.Map;

import com.tgx.extended.ExtendedConfig;
import com.tgx.extended.utils.SystemUtils;
import com.tgx.extended.ExtendedConfig.Setting;

import static com.tgx.extended.ExtendedConfig.Setting.*;

public class ExtendedSettingsController extends RecyclerViewController<ExtendedSettingsController.Args> implements View.OnClickListener, ExtendedConfig.SettingsChangeListener {

  private SettingsAdapter adapter;
  private int mode;

  public static final int MODE_GENERAL = 1, MODE_INTERFACE = 2, MODE_CHATS = 3, MODE_MISC = 4;
  public static final int OPTIONS_MESSAGE_PANEL = 1, OPTIONS_DRAWER = 2, OPTIONS_PICSIZE = 3;

  public ExtendedSettingsController(Context ctx, Tdlib tdlib) {
    super(ctx, tdlib);
  }

  public static class Args {
    public final int mode;
    public Args(int m) { mode = m; }
  }

  @Override
  public void setArguments(Args args) {
    super.setArguments(args);
    mode = args.mode;
  }

  @Override
  public int getId() {
    return R.id.controller_extendedSettings;
  }

  @Override
  public CharSequence getName() {
    return mode == MODE_GENERAL
      ? Lang.getString(R.string.GeneralSettings)
      : mode == MODE_INTERFACE
      ? Lang.getString(R.string.AppearanceSettings)
      : mode == MODE_CHATS
      ? Lang.getString(R.string.ChatsSettings)
      : mode == MODE_MISC
      ? Lang.getString(R.string.MiscSettings) 
      : Lang.getString(R.string.ExtendedSettings);
  }

  @Override
  public void onSettingsChanged(ExtendedConfig.Setting setting, boolean newVal, boolean oldVal) {
    if (setting == ExtendedConfig.Setting.FOREVER_OFFLINE) {
      tdlib.setOnline(false);
    }
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.btn_generalSettings || id == R.id.btn_appearanceSettings || id == R.id.btn_chatsSettings || id == R.id.btn_miscSettings) {
      if (id == R.id.btn_generalSettings) mode = MODE_GENERAL;
      else if (id == R.id.btn_appearanceSettings) mode = MODE_INTERFACE;
      else if (id == R.id.btn_chatsSettings) mode = MODE_CHATS;
      else if (id == R.id.btn_miscSettings) mode = MODE_MISC;
      ExtendedSettingsController c = new ExtendedSettingsController(context(), tdlib);
      c.setArguments(new Args(mode));
      navigateTo(c);
    } else if (id == R.id.btn_extendedChannel) {
      tdlib.ui().openUrl(this, ExtendedConfig.CHANNEL, new TdlibUi.UrlOpenParameters());
    } else if (id == R.id.btn_extendedChat) {
      tdlib.ui().openUrl(this, ExtendedConfig.CHAT, new TdlibUi.UrlOpenParameters());
    } else if (id == R.id.btn_extendedSources) {
      tdlib.ui().openUrl(this, ExtendedConfig.SOURCES, new TdlibUi.UrlOpenParameters());
    } else if (id == R.id.btn_extendedTranslate) {
      tdlib.ui().openUrl(this, ExtendedConfig.TRANSLATE, new TdlibUi.UrlOpenParameters());
    } else if (id == R.id.btn_hideMessageButtons) {
      showOptions(OPTIONS_MESSAGE_PANEL, ListItem.TYPE_CHECKBOX_OPTION);
    } else if (id == R.id.btn_drawerItems) {
      showOptions(OPTIONS_DRAWER, ListItem.TYPE_CHECKBOX_OPTION);
    } else if (id == R.id.btn_photoSize) {
      showOptions(OPTIONS_PICSIZE, ListItem.TYPE_RADIO_OPTION);
    } else if (id == R.id.btn_restrictSensitiveContent) {
      tdlib.setIgnoreSensitiveContentRestrictions(adapter.toggleView(v));
    } else if (id == R.id.btn_ignoreContentRestrictions) {
      Settings.instance().setRestrictContent(!adapter.toggleView(v));
    } else {
      toggleSettingByViewId(id);
      adapter.updateValuedSettingById(id);
    }
  }

  private static final Map<Integer, Setting> toggleSettingsMapping = Map.of(
    R.id.btn_drawerBlur, DRAWER_BLUR,
    R.id.btn_drawerDarken, DRAWER_DARKEN,
    R.id.btn_showUserId, SHOW_USER_ID,
    R.id.btn_hidePhoneNumber, HIDE_PHONE_NUMBER,
    R.id.btn_disableReactions, DISABLE_REACTIONS,
    R.id.btn_disableTyping, DISABLE_TYPING,
    R.id.btn_foreverOffline, FOREVER_OFFLINE,
    R.id.btn_foreverUnread, FOREVER_UNREAD
  );

  private void toggleSettingByViewId(int id) {
    Setting s = toggleSettingsMapping.get(id);
    if (s != null) ExtendedConfig.instance().toggleSetting(s);
  }

  private void showOptions(int optionType, int itemType) {
    final Map<Integer, Setting> settingsMap;
    final int title;
    final int wrapId;
    final boolean shouldRestart;

    if (optionType == OPTIONS_MESSAGE_PANEL) {
      settingsMap = Map.of(
        R.id.btn_disableCameraButton, DISABLE_CAMERA_BUTTON,
        R.id.btn_disableCommandButton, DISABLE_COMMAND_BUTTON,
        R.id.btn_disableRecordButton, DISABLE_RECORD_BUTTON,
        R.id.btn_disableSenderButton, DISABLE_SENDER_BUTTON
      );
      shouldRestart = false;
      title = R.string.MessagePanelPreferences;
      wrapId = R.id.btn_hideMessageButtons;

    } else if (optionType == OPTIONS_DRAWER) {
      settingsMap = Map.of(
        R.id.btn_contacts, DRAWER_HIDE_CONTACTS,
        R.id.btn_calls, DRAWER_HIDE_CALLS,
        R.id.btn_savedMessages, DRAWER_HIDE_FAVOURITE,
        R.id.btn_invite, DRAWER_HIDE_INVITE,
        R.id.btn_help, DRAWER_HIDE_HELP,
        R.id.btn_night, DRAWER_HIDE_NIGHT
      );
      title = R.string.DrawerItems;
      wrapId = R.id.btn_drawerItems;
      shouldRestart = true;

    } else if (optionType == OPTIONS_PICSIZE) {
      settingsMap = Map.of(
        R.id.btn_800px, Q800PX,
        R.id.btn_1280px, Q1280PX,
        R.id.btn_2560px, Q2560PX
      );
      shouldRestart = false;
      title = R.string.ChangePhotoSize;
      wrapId = R.id.btn_photoSize;

    } else {
      return;
    }

    ListItem[] items = settingsMap.entrySet().stream()
      .map(entry -> new ListItem(
        itemType,
        entry.getKey(),
        0,
        getLabel(entry.getValue()),
        entry.getValue().value
      ))
      .toArray(ListItem[]::new);

    showSettings(new SettingsWrapBuilder(wrapId)
      .addHeaderItem(new ListItem(ListItem.TYPE_INFO, R.id.text_title, 0, title, false))
      .setRawItems(items)
      .setIntDelegate((id, result) -> {
        settingsMap.forEach((viewId, setting) -> {
          if (setting.value == (result.get(viewId) == 0))
            ExtendedConfig.instance().toggleSetting(setting);
        });
        adapter.updateValuedSettingById(wrapId);
        if (shouldRestart) SystemUtils.restartApp(context());
      }));
  }

  private static final Map<Setting, Integer> optionsLabels = Map.ofEntries(
    Map.entry(DISABLE_CAMERA_BUTTON, R.string.DisableCameraButton),
    Map.entry(DISABLE_COMMAND_BUTTON, R.string.DisableCommandButton),
    Map.entry(DISABLE_RECORD_BUTTON, R.string.DisableRecordButton),
    Map.entry(DISABLE_SENDER_BUTTON, R.string.DisableSenderButton),
    Map.entry(DRAWER_HIDE_CONTACTS, R.string.Contacts),
    Map.entry(DRAWER_HIDE_CALLS, R.string.Calls),
    Map.entry(DRAWER_HIDE_FAVOURITE, R.string.SavedMessages),
    Map.entry(DRAWER_HIDE_INVITE, R.string.InviteFriends),
    Map.entry(DRAWER_HIDE_HELP, R.string.Help),
    Map.entry(DRAWER_HIDE_NIGHT, R.string.NightMode),
    Map.entry(Q800PX, R.string.px800),
    Map.entry(Q1280PX, R.string.px1280),
    Map.entry(Q2560PX, R.string.px2560)
  );

  private int getLabel(Setting s) {
    return optionsLabels.getOrDefault(s, 0);
  }

  private void setToggle(SettingView view, ExtendedConfig.Setting s, boolean isUpdate) {
    view.getToggler().setRadioEnabled(s.value, isUpdate);
  }

  @Override
  protected void onCreateView(Context ctx, CustomRecyclerView recyclerView) {
    adapter = new SettingsAdapter(this) {
      @Override
      protected void setValuedSetting(ListItem item, SettingView view, boolean isUpdate) {
        int id = item.getId();
        if (id == R.id.btn_extendedChannel) view.setData(R.string.ExtendedChannelDesc);
        else if (id == R.id.btn_extendedChat) view.setData(R.string.ExtendedChatDesc);
        else if (id == R.id.btn_extendedSources) view.setData(R.string.ExtendedSourcesDesc);
        else if (id == R.id.btn_extendedTranslate) view.setData(R.string.ExtendedTranslateDesc);
        else if (id == R.id.btn_showUserId) setToggle(view, SHOW_USER_ID, isUpdate);
        else if (id == R.id.btn_drawerBlur) setToggle(view, DRAWER_BLUR, isUpdate);
        else if (id == R.id.btn_drawerDarken) setToggle(view, DRAWER_DARKEN, isUpdate);
        else if (id == R.id.btn_hidePhoneNumber) {
          view.setData(R.string.HidePhoneNumberDesc);
          setToggle(view, HIDE_PHONE_NUMBER, isUpdate);
        } else if (id == R.id.btn_disableCameraButton) setToggle(view, DISABLE_CAMERA_BUTTON, isUpdate);
        else if (id == R.id.btn_disableRecordButton) setToggle(view, DISABLE_RECORD_BUTTON, isUpdate);
        else if (id == R.id.btn_disableCommandButton) setToggle(view, DISABLE_COMMAND_BUTTON, isUpdate);
        else if (id == R.id.btn_disableSenderButton) setToggle(view, DISABLE_SENDER_BUTTON, isUpdate);
        else if (id == R.id.btn_hideMessageButtons) view.setData(R.string.HideMessageButtonsDesc);
        else if (id == R.id.btn_drawerItems) view.setData(R.string.DrawerItemsDesc);
        else if (id == R.id.btn_contacts) setToggle(view, DRAWER_HIDE_CONTACTS, isUpdate);
        else if (id == R.id.btn_calls) setToggle(view, DRAWER_HIDE_CALLS, isUpdate);
        else if (id == R.id.btn_savedMessages) setToggle(view, DRAWER_HIDE_FAVOURITE, isUpdate);
        else if (id == R.id.btn_invite) setToggle(view, DRAWER_HIDE_INVITE, isUpdate);
        else if (id == R.id.btn_help) setToggle(view, DRAWER_HIDE_HELP, isUpdate);
        else if (id == R.id.btn_night) setToggle(view, DRAWER_HIDE_NIGHT, isUpdate);
        else if (id == R.id.btn_photoSize) {
          if (ExtendedConfig.instance().get(ExtendedConfig.Setting.Q800PX)) view.setData(R.string.px800);
          else if (ExtendedConfig.instance().get(ExtendedConfig.Setting.Q1280PX)) view.setData(R.string.px1280);
          else if (ExtendedConfig.instance().get(ExtendedConfig.Setting.Q2560PX)) view.setData(R.string.px2560);
        }
        else if (id == R.id.btn_800px) setToggle(view, Q800PX, isUpdate);
        else if (id == R.id.btn_1280px) setToggle(view, Q1280PX, isUpdate);
        else if (id == R.id.btn_2560px) setToggle(view, Q2560PX, isUpdate);
        else if (id == R.id.btn_disableReactions) setToggle(view, DISABLE_REACTIONS, isUpdate);
        else if (id == R.id.btn_disableTyping) setToggle(view, DISABLE_TYPING, isUpdate);
        else if (id == R.id.btn_foreverOffline) setToggle(view, FOREVER_OFFLINE, isUpdate);
        else if (id == R.id.btn_foreverUnread) setToggle(view, FOREVER_UNREAD, isUpdate);
        else if (id == R.id.btn_restrictSensitiveContent) {
          view.getToggler().setRadioEnabled(tdlib.ignoreSensitiveContentRestrictions(), isUpdate);
        } else if (id == R.id.btn_ignoreContentRestrictions) {
          view.getToggler().setRadioEnabled(!Settings.instance().needRestrictContent(), isUpdate);
        }
      }
    };

    ArrayList<ListItem> items = new ArrayList<>();
    items.add(new ListItem(ListItem.TYPE_EMPTY_OFFSET_SMALL));

    if (mode == MODE_GENERAL) {
      items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.ProfilePreferences));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_showUserId, 0, R.string.ShowUserId));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    } else if (mode == MODE_INTERFACE) {
      items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.DrawerPreferences));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_drawerBlur, 0, R.string.DrawerBlur));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
      items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_drawerDarken, 0, R.string.DrawerDarken));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
      items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT_WITH_TOGGLER, R.id.btn_hidePhoneNumber, 0, R.string.HidePhoneNumber));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
      items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_drawerItems, 0, R.string.DrawerItems));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
    } else if (mode == MODE_CHATS) {
      items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.MessagePanelPreferences));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_hideMessageButtons, 0, R.string.HideMessageButtons));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
      items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_disableReactions, 0, R.string.DisableReactions));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

      items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.ActivityPreferences));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_foreverOffline, 0, R.string.ForeverOffline));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
      items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.ForeverOfflineDesc));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_foreverUnread, 0, R.string.ForeverUnread));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
      items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.ForeverUnreadDesc));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_disableTyping, 0, R.string.DisableTyping));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
      items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.DisableTypingDesc));
      
    } else if (mode == MODE_MISC) {
      items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.UnstablePreferences));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_restrictSensitiveContent, 0, R.string.DisplaySensitiveContent));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
      items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_ignoreContentRestrictions, 0, R.string.IgnoreRestrictions));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
      items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_photoSize, 0, R.string.ChangePhotoSize));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
      items.add(new ListItem(ListItem.TYPE_DESCRIPTION, 0, 0, R.string.ChangePhotoSizeDesc));

    } else {
      items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.Settings));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_generalSettings, R.drawable.baseline_widgets_24, R.string.GeneralSettings));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR));
      items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_appearanceSettings, R.drawable.baseline_palette_24, R.string.AppearanceSettings));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR));
      items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_chatsSettings, R.drawable.baseline_chat_bubble_24, R.string.ChatsSettings));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR));
      items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_miscSettings, R.drawable.baseline_star_24, R.string.MiscSettings));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

      items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.AboutExtended));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_extendedChannel, R.drawable.baseline_newspaper_24, R.string.ExtendedChannel));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR));
      items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_extendedChat, R.drawable.baseline_forum_24, R.string.ExtendedChat));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR));
      items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_extendedSources, R.drawable.baseline_github_24, R.string.ExtendedSources));
      items.add(new ListItem(ListItem.TYPE_SEPARATOR));
      items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_extendedTranslate, R.drawable.baseline_translate_24, R.string.ExtendedTranslate));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
      items.add(new ListItem(ListItem.TYPE_BUILD_NO, R.id.btn_build, 0, ExtendedConfig.BUILD));
    }
    adapter.setItems(items, true);
    recyclerView.setAdapter(adapter);
  }
}
