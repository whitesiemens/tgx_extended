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
import com.tgx.extended.Data;

public class AppearanceSettingsController extends RecyclerViewController<Void> implements View.OnClickListener {
  public AppearanceSettingsController (Context context, Tdlib tdlib) {
    super(context, tdlib);
  }

  @Override public int getId () {
    return R.id.controller_appearanceSettings;
  }

  @Override public CharSequence getName() {
	return Lang.getString(R.string.AppearanceSettings);
  }

  private SettingsAdapter adapter;

  @Override public void onClick(View v) {
  	int viewId = v.getId();
    if (viewId == R.id.btn_hidePhoneNumber) {
      Config.instance().toggleHidePhoneNumber();
      adapter.updateValuedSettingById(viewId);
    }
  }

  @Override protected void onCreateView(Context context, CustomRecyclerView recyclerView) {
  		adapter = new SettingsAdapter(this) {
  			@Override protected void setValuedSetting(ListItem item, SettingView view, boolean isUpdate) {
  				view.setDrawModifier(item.getDrawModifier());
  				int itemId = item.getId();
  				if (itemId == R.id.btn_hidePhoneNumber) {
            view.setData(R.string.HidePhoneNumberDesc);
            view.getToggler().setRadioEnabled(Config.hidePhoneNumber, isUpdate);
          }
        }
      };

      ArrayList<ListItem> items = new ArrayList<>();

      items.add(new ListItem(ListItem.TYPE_EMPTY_OFFSET_SMALL));

      items.add(new ListItem(ListItem.TYPE_HEADER, 0, 0, R.string.DrawerPreferences));
      items.add(new ListItem(ListItem.TYPE_SHADOW_TOP));
      items.add(new ListItem(ListItem.TYPE_RADIO_SETTING, R.id.btn_hidePhoneNumber, 0, R.string.HidePhoneNumber));
      items.add(new ListItem(ListItem.TYPE_SHADOW_BOTTOM));

      adapter.setItems(items, true);
      recyclerView.setAdapter(adapter);

  }
}