package com.uusafe.plugin.plugina;

import android.content.Context;

import com.uusafe.plugin_frame_base.IPlugin;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class Entry {

    public static IPlugin create(Context context, ClassLoader cl) {
        return new Plugin(cl);
    }
}
