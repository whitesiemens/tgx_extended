package com.tgx.extended.utils;

import android.content.Context;

import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.telegram.TdlibUi;

public class URLUtils {

  public static void openUrl(Context context, String url) {
    TdlibUi.UrlOpenParameters params = new TdlibUi.UrlOpenParameters();
    Tdlib.instance().ui().openUrl(context, url, params);
  }
  
}