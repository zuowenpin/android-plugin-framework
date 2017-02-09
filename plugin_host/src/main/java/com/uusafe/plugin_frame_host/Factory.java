package com.uusafe.plugin_frame_host;

import android.content.Context;
import android.os.IBinder;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class Factory {
    public static final String PLUGIN_ENTRY_PACKAGE_PREFIX = "com.uusafe.plugin";
    public static final String PLUGIN_ENTRY_CLASS_NAME = "Entry";
    public static final String PLUGIN_ENTRY_EXPORT_METHOD_NAME = "create";
    public static final Class<?> PLUGIN_ENTRY_EXPORT_METHOD_PARAMS[] = {Context.class, ClassLoader.class};
    public static final Class<?> PLUGIN_ENTRY_EXPORT_METHOD2_PARAMS[] = {Context.class, ClassLoader.class, IBinder.class};
}
