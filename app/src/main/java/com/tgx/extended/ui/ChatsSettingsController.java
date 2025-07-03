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
  	// TODO: Later
  }

  @Override protected void onCreateView(Context context, CustomRecyclerView recyclerView) {
  		adapter = new SettingsAdapter(this) {
  			@Override protected void setValuedSetting(ListItem item, SettingView view, boolean isUpdate) {
  				view.setDrawModifier(item.getDrawModifier());
  				int itemId = item.getId();
  				// TODO: Later
        }
      };

      ArrayList<ListItem> items = new ArrayList<>();

      // TODO: Later

      adapter.setItems(items, true);
      recyclerView.setAdapter(adapter);

  }
}