package com.tgx.extended.ui;

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

import com.tgx.extended.Config;

public class ExtendedSettingsController extends RecyclerViewController<Void> implements View.OnClickListener {
  public ExtendedSettingsController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override public int getId () {
    return R.id.controller_extendedSettings;
  }

  @Override public CharSequence getName() {
	return Lang.getString(R.string.ExtendedSettings);
  }

  private SettingsAdapter adapter;

  @Override public void onClick(View v) {
  	int viewId = v.getId();
  	if (viewId == R.id.btn_generalSettings) {
  		navigateTo(new GeneralSettingsController(context, tdlib));
  	} else if (viewId == R.id.btn_appearanceSettings) {
  		navigateTo(new AppearanceSettingsController(context, tdlib));
  	} else if (viewId == R.id.btn_chatsSettings) {
  		navigateTo(new ChatsSettingsController(context, tdlib));
  	} else if (viewId == R.id.btn_miscSettings) {
  		navigateTo(new MiscSettingsController(context, tdlib));
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
  	}
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
          }
        }
      };

      ArrayList<ListItem> items = new ArrayList<>();

      items.add(new ListItem(ListItem.TYPE_EMPTY_OFFSET_SMALL));

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

      adapter.setItems(items, true);
      recyclerView.setAdapter(adapter);

  }
}