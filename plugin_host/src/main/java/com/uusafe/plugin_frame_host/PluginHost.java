package com.uusafe.plugin_frame_host;

import android.app.Application;
import android.content.Context;

import com.uusafe.plugin_frame_host.utils.PatchClassLoaderUtils;

import dalvik.system.PathClassLoader;

/**
 * Created by ${zuowp291} on 2017/2/13.
 */

public class PluginHost {
    PluginManager getPluginManager() {
        return manager;
    }

    private PluginManager manager;

    /**
     * Invoked at the beginning
     * fix the classLoader
     */
    public void attachBaseContext(Application base) {
        manager = new PluginManager(base);
        Factory2.sPluginManager = manager;
        PatchClassLoaderUtils.patchAppContextPackageInfoClassLoader(base, manager);
    }

    public void startPluginActivity(String pluginName, String activityName) {
        manager.startPluginActivity(pluginName, activityName);
    }
}
