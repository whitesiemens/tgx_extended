package com.tgx.extended;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.thunderdog.challegram.R;
import org.thunderdog.challegram.U;
import org.thunderdog.challegram.component.base.SettingView;
import org.thunderdog.challegram.core.Lang;
import org.thunderdog.challegram.navigation.SettingsWrapBuilder;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.telegram.TdlibUi;
import org.thunderdog.challegram.tool.UI;
import org.thunderdog.challegram.ui.ListItem;
import org.thunderdog.challegram.ui.RecyclerViewController;
import org.thunderdog.challegram.ui.SettingsAdapter;
import org.thunderdog.challegram.v.CustomRecyclerView;

import java.util.ArrayList;

import com.tgx.extended.ExtendedConfig;

public class ExtendedSettingsController extends RecyclerViewController<ExtendedSettingsController.Args> implements View.OnClickListener {
  public ExtendedSettingsController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override public int getId () {
    return R.id.controller_extendedSettings;
  }

  public static final int MODE_GENERAL = 1;
  public static final int MODE_INTERFACE = 2;
  public static final int MODE_CHATS = 3;
  public static final int MODE_MISC = 4;

  private int mode;

  public static class Args {
    private final int mode;

    public Args (int mode) {
      this.mode = mode;
    }
  }

  @Override
  public void setArguments (Args args) {
    super.setArguments(args);
    this.mode = args.mode;
  }

  @Override public CharSequence getName() {
    return mode == MODE_GENERAL
      ? Lang.getString(R.string.GeneralSettings) : mode == MODE_INTERFACE
      ? Lang.getString(R.string.AppearanceSettings) : mode == MODE_CHATS
      ? Lang.getString(R.string.ChatsSettings) : mode == MODE_MISC
      ? Lang.getString(R.string.MiscSettings) : Lang.getString(R.string.ExtendedSettings);
  }

  private SettingsAdapter adapter;

  @Override public void onClick(View v) {
  	int viewId = v.getId();
    ExtendedSettingsController c = new ExtendedSettingsController(context, tdlib);
  	if (viewId == R.id.btn_generalSettings) {
  		c.setArguments(new ExtendedSettingsController.Args(ExtendedSettingsController.MODE_GENERAL));
      navigateTo(c);
  	} else if (viewId == R.id.btn_appearanceSettings) {
      c.setArguments(new ExtendedSettingsController.Args(ExtendedSettingsController.MODE_INTERFACE));
  		navigateTo(c);
  	} else if (viewId == R.id.btn_chatsSettings) {
      c.setArguments(new ExtendedSettingsController.Args(ExtendedSettingsController.MODE_CHATS));
  		navigateTo(c);
  	} else if (viewId == R.id.btn_miscSettings) {
      c.setArguments(new ExtendedSettingsController.Args(ExtendedSettingsController.MODE_MISC));
  		navigateTo(c);
  	} else if (viewId == R.id.btn_channelLink) {
  		tdlib.ui().openUrl(this, Lang.getString(R.string.ExtendedChannelLink), new TdlibUi.UrlOpenParameters());
  	} else if (viewId == R.id.btn_githubLink) {
  		tdlib.ui().openUrl(this, Lang.getString(R.string.GithubLink), new TdlibUi.UrlOpenParameters());
  	} else if (viewId == R.id.btn_crowdinLink) {
  		tdlib.ui().openUrl(this, Lang.getString(R.string.CrowdinLink), new TdlibUi.UrlOpenParameters());
  	} else if (viewId == R.id.btn_donateLink) {
  		tdlib.ui().openUrl(this, Lang.getString(R.string.DonateLink), new TdlibUi.UrlOpenParameters());
  	} else if (viewId == R.id.btn_checkExtendedUpdates) {
  		// TODO: Re-create official updater here.
  	} else if (viewId == R.id.btn_showUserId) {
      ExtendedConfig.instance().toggleShowUserId();
      adapter.updateValuedSettingById(viewId);
    } else if (viewId == R.id.btn_hidePhoneNumber) {
      ExtendedConfig.instance().toggleHidePhoneNumber();
      adapter.updateValuedSettingById(viewId);
    } else if (viewId == R.id.btn_hideMessageButtons) {
      showMessagePanelOptions();
    } else if (viewId == R.id.btn_drawerItems) {
      showDrawerItems();
    }
  }

  private void showDrawerItems () {
    showSettings(new SettingsWrapBuilder(R.id.btn_drawerItems).addHeaderItem(
      new ListItem(ListItem.TYPE_INFO, R.id.text_title, 0, R.string.DrawerItems, false)).setRawItems(
      new ListItem[] {
        new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_contacts, 0, R.string.Contacts, ExtendedConfig.drawerHideContacts),
        new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_calls, 0, R.string.Calls, ExtendedConfig.drawerHideCalls),
        new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_savedMessages, 0, R.string.SavedMessages, ExtendedConfig.drawerHideFavourite),
        new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_invite, 0, R.string.InviteFriends, ExtendedConfig.drawerHideInvite),
        new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_help, 0, R.string.Help, ExtendedConfig.drawerHideHelp),
        new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_night, 0, R.string.NightMode, ExtendedConfig.drawerHideNight)
      }).setIntDelegate((id, result) -> {
      if (ExtendedConfig.drawerHideContacts == (result.get(R.id.btn_contacts) == 0)) {
        ExtendedConfig.instance().toggleDrawerHideContacts();
      }
      if (ExtendedConfig.drawerHideCalls == (result.get(R.id.btn_calls) == 0)) {
        ExtendedConfig.instance().toggleDrawerHideCalls();
      }
      if (ExtendedConfig.drawerHideFavourite == (result.get(R.id.btn_savedMessages) == 0)) {
        ExtendedConfig.instance().toggleDrawerHideFavourite();
      }
      if (ExtendedConfig.drawerHideInvite == (result.get(R.id.btn_invite) == 0)) {
        ExtendedConfig.instance().toggleDrawerHideInvite();
      }
      if (ExtendedConfig.drawerHideHelp == (result.get(R.id.btn_invite) == 0)) {
        ExtendedConfig.instance().toggleDrawerHideHelp();
      }
      if (ExtendedConfig.drawerHideNight == (result.get(R.id.btn_invite) == 0)) {
        ExtendedConfig.instance().toggleDrawerHideNight();
      }
      adapter.updateValuedSettingById(R.id.btn_drawerItems);
    }));
  }

  private void showMessagePanelOptions () {
    showSettings(new SettingsWrapBuilder(R.id.btn_hideMessageButtons).addHeaderItem(
      new ListItem(ListItem.TYPE_INFO, R.id.text_title, 0, R.string.MessagePanelPreferences, false)).setRawItems(
      new ListItem[] {
        new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_disableCameraButton, 0, R.string.DisableCameraButton, ExtendedConfig.disableCameraButton),
        new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_disableCommandButton, 0, R.string.DisableCommandButton, ExtendedConfig.disableCommandButton),
        new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_disableRecordButton, 0, R.string.DisableRecordButton, ExtendedConfig.disableRecordButton),
        new ListItem(ListItem.TYPE_CHECKBOX_OPTION, R.id.btn_disableSenderButton, 0, R.string.DisableSenderButton, ExtendedConfig.disableSenderButton)
      }).setIntDelegate((id, result) -> {
      if (ExtendedConfig.disableCameraButton == (result.get(R.id.btn_disableCameraButton) == 0)) {
        ExtendedConfig.instance().toggleDisableCameraButton();
      }
      if (ExtendedConfig.disableCommandButton == (result.get(R.id.btn_disableCommandButton) == 0)) {
        ExtendedConfig.instance().toggleDisableCommandButton();
      }
      if (ExtendedConfig.disableRecordButton == (result.get(R.id.btn_disableRecordButton) == 0)) {
        ExtendedConfig.instance().toggleDisableRecordButton();
      }
      if (ExtendedConfig.disableSenderButton == (result.get(R.id.btn_disableSenderButton) == 0)) {
        ExtendedConfig.instance().toggleDisableSenderButton();
      }
      adapter.updateValuedSettingById(R.id.btn_hideMessageButtons);
    }));
  }

  @Override protected void onCreateView(Context context, CustomRecyclerView recyclerView) {
  		adapter = new SettingsAdapter(this) {
  			@Override protected void setValuedSetting(ListItem item, SettingView view, boolean isUpdate) {
  				view.setDrawModifier(item.getDrawModifier());
  				int itemId = item.getId();
  				if (itemId == R.id.btn_channelLink) {
  					view.setData("@tgx_extended");
  				} else if (itemId == R.id.btn_githubLink) {
  					view.setData("GitHub");
  				} else if (itemId == R.id.btn_crowdinLink) {
            view.setData(R.string.CrowdinDesc);
          } else if (itemId == R.id.btn_donateLink) {
            view.setData(R.string.DonateDesc);
          } else if (itemId == R.id.btn_checkExtendedUpdates) {
            // TODO: Dynamic string with last time checked.
            view.setData("Already updated to latest version.");
          } else if (itemId == R.id.btn_showUserId) {
            view.getToggler().setRadioEnabled(ExtendedConfig.showUserId, isUpdate);
          } else if (itemId == R.id.btn_hidePhoneNumber) {
            view.setData(R.string.HidePhoneNumberDesc);
            view.getToggler().setRadioEnabled(ExtendedConfig.hidePhoneNumber, isUpdate);
          } else if (itemId == R.id.btn_disableCameraButton) {
            view.getToggler().setRadioEnabled(ExtendedConfig.disableCameraButton, isUpdate);
          } else if (itemId == R.id.btn_disableRecordButton) {
            view.getToggler().setRadioEnabled(ExtendedConfig.disableRecordButton, isUpdate);
          } else if (itemId == R.id.btn_disableCommandButton) {
            view.getToggler().setRadioEnabled(ExtendedConfig.disableCommandButton, isUpdate);
          } else if (itemId == R.id.btn_disableSenderButton) {
            view.getToggler().setRadioEnabled(ExtendedConfig.disableSenderButton, isUpdate);
          } else if (itemId == R.id.btn_hideMessageButtons) {
            view.setData(R.string.HideMessageButtonsDesc);
          } else if (itemId == R.id.btn_drawerItems) {
            view.setData(R.string.DrawerItemsDesc);
          } else if (itemId == R.id.btn_contacts) {
            view.getToggler().setRadioEnabled(ExtendedConfig.drawerHideContacts, isUpdate);
          } else if (itemId == R.id.btn_calls) {
            view.getToggler().setRadioEnabled(ExtendedConfig.drawerHideCalls, isUpdate);
          } else if (itemId == R.id.btn_savedMessages) {
            view.getToggler().setRadioEnabled(ExtendedConfig.drawerHideFavourite, isUpdate);
          } else if (itemId == R.id.btn_invite) {
            view.getToggler().setRadioEnabled(ExtendedConfig.drawerHideInvite, isUpdate);
          } else if (itemId == R.id.btn_help) {
            view.getToggler().setRadioEnabled(ExtendedConfig.drawerHideHelp, isUpdate);
          } else if (itemId == R.id.btn_night) {
            view.getToggler().setRadioEnabled(ExtendedConfig.drawerHideNight, isUpdate);
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
        items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT_WITH_TOGGLER, R.id.btn_hidePhoneNumber, 0, R.string.HidePhoneNumber));
        items.add(new ListItem(ListItem.TYPE_SEPARATOR_FULL));
        items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_drawerItems, 0, R.string.DrawerItems));
        items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
      } else if (mode == MODE_CHATS) {
        items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.MessagePanelPreferences));
        items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
        items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_hideMessageButtons, 0, R.string.HideMessageButtons));
        items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
      } else if (mode == MODE_MISC) {
        // TODO: No features yet
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
        items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_channelLink, 0, R.string.TelegramChannel));
        items.add(new ListItem(ListItem.TYPE_SEPARATOR));
        items.add(new ListItem(ListItem.TYPE_SETTING, R.id.btn_githubLink, 0, R.string.GitHub));
        items.add(new ListItem(ListItem.TYPE_SEPARATOR));
        items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_crowdinLink, 0, R.string.Crowdin));
        items.add(new ListItem(ListItem.TYPE_SEPARATOR));
        items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_donateLink, 0, R.string.Donate));
        items.add(new ListItem(ListItem.TYPE_SEPARATOR));
        items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_checkExtendedUpdates, 0, R.string.OTACheck));
        items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));
      }

      adapter.setItems(items, true);
      recyclerView.setAdapter(adapter);

  }
}