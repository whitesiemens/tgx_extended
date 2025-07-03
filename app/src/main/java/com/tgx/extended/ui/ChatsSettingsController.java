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

import com.tgx.extended.ExtendedConfig;

public class ChatsSettingsController extends RecyclerViewController<Void> implements View.OnClickListener {
  public ChatsSettingsController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override public int getId () {
    return R.id.controller_chatsSettings;
  }

  @Override public CharSequence getName() {
	  return Lang.getString(R.string.ChatsSettings);
  }

  private SettingsAdapter adapter;

  @Override public void onClick(View v) {
  	int viewId = v.getId();
    if (viewId == R.id.btn_hideMessageButtons) {
      showMessagePanelOptions();
    }
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
      if (ExtendedConfig.disableSenerButton == (result.get(R.id.btn_disableSendAsButton) == 0)) {
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
  				if (itemId == R.id.btn_disableCameraButton) {
            view.getToggler().setRadioEnabled(ExtendedConfig.disableCameraButton, isUpdate);
          } else if (itemId == R.id.btn_disableRecordButton) {
            view.getToggler().setRadioEnabled(ExtendedConfig.disableRecordButton, isUpdate);
          } else if (itemId == R.id.btn_disableCommandButton) {
            view.getToggler().setRadioEnabled(ExtendedConfig.disableCommandButton, isUpdate);
          } else if (itemId == R.id.btn_disableSenderButton) {
            view.getToggler().setRadioEnabled(ExtendedConfig.disableSenderButton, isUpdate);
          } else if (itemId == R.id.btn_hideMessageButtons) {
            StringBuilder b = new StringBuilder();
            String separator = Lang.getConcatSeparator();
            if (ExtendedConfig.disableCameraButton) {
              b.append(Lang.getString(R.string.DisableCameraButton));
              if (b.length() > 0) b.append(separator);
            } else if (ExtendedConfig.disableRecordButton) {
              b.append(Lang.getString(R.string.DisableRecordButton));
              if (b.length() > 0) b.append(separator);
            } else if (ExtendedConfig.disableCommandButton) {
              b.append(Lang.getString(R.string.DisableCommandButton));
              if (b.length() > 0) b.append(separator);
            } else if (ExtendedConfig.disableSenderButton) {
              b.append(Lang.getString(R.string.DisableSenderButton));
            }
          }
        }
      };

      ArrayList<ListItem> items = new ArrayList<>();

      items.add(new ListItem(ListItem.TYPE_EMPTY_OFFSET_SMALL));

      items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.MessagePanelPreferences));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_VALUED_SETTING_COMPACT, R.id.btn_hideMessageButtons, 0, R.string.HideMessageButtons));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

      adapter.setItems(items, true);
      recyclerView.setAdapter(adapter);

  }

}