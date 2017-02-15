package com.uusafe.mobilesafe_plugin_framework;

import android.app.Application;
import android.content.Context;

import com.uusafe.plugin_frame_host.PluginHost;

/**
 * Created by ${zuowp291} on 2017/2/15.
 */

public class AndroidApplication extends Application {
    private final PluginHost pluginHost = new PluginHost();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        pluginHost.attachBaseContext(this);
    }


    public PluginHost getPluginHost() {
        return pluginHost;
    }
}
