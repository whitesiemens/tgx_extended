package com.tgx.extended.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class SystemUtils {

    public static void restartApp(Context context) {
      Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
      if (intent != null) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
      }
      if (context instanceof Activity) {
        ((Activity) context).finish();
      }
      Runtime.getRuntime().exit(0);
    }
    
}