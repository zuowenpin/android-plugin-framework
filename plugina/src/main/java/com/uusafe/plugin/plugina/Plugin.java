package com.uusafe.plugin.plugina;

import android.content.Context;

import com.uusafe.plugin_frame_base.IPlugin;
import com.uusafe.plugin_frame_base.PluginFramework;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

@SuppressWarnings("WeakerAccess")
public class Plugin implements IPlugin {
    public Plugin(ClassLoader cl) {
        PluginFramework.init(cl);
    }
}
