package com.uusafe.plugin_frame_host;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ${zuowp291} on 2017/2/10.
 * Responsibility:
 * 1. supply correct context for component
 * 2. proxy component lifecycle actions
 * 3. load plugin if necessary
 * 4. manage stub for true component
 */

public class Factory2 {
    public static PluginManager sPluginManager;


    public static final Context createActivityContext(Activity activity, Context newBase) {
        return sPluginManager.createActivityContext(activity, newBase);
    }

    public static final void handleActivityCreate(Activity activity, Bundle savedInstanceState) {
        sPluginManager.handleActivityCreate(activity, savedInstanceState);
    }

    public static final void handleActivityDestroy(Activity activity) {
        sPluginManager.handleActivityDestroy(activity);
    }

    public static final void handleRestoreInstanceState(Activity activity, Bundle savedInstance) {
        sPluginManager.handleRestoreInstanceState(activity, savedInstance);
    }

    public static final void handleServiceCreate(Service service) {
        sPluginManager.handleServiceCreate(service);
    }

    public static final void handleServiceDestroy(Service service) {
        sPluginManager.handleServiceDestroy(service);
    }

    public static final boolean startActivity(Activity activity, Intent intent) {
        return sPluginManager.startActivity(activity, intent);
    }

    public static final boolean startActivity(Context context, Intent intent, String plugin, String activity, int process, boolean download) {
        return sPluginManager.startActivity(context, intent, plugin, activity, process, download);
    }
}
