package com.uusafe.plugin_frame_host;

import android.content.ComponentName;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class PluginProcessPer {
    private final HashMap<String, ComponentName> mStates = new HashMap<>();
    private final Object mLock = new Object();
    private final PluginManager mPluginManager;

    public PluginProcessPer(PluginManager pl) {
        mPluginManager = pl;
    }


    public Class<?> findActivityClass(Method findClassMethod, String className) {
        String plugin = null;
        String activity = null;
        ComponentName cn = lookupContainer(className);
        if (cn != null) {
            plugin = cn.getPackageName();
            activity = cn.getClassName();
        }

        Plugin p = mPluginManager.loadPlugin(plugin, true);
        if (p == null) {
            return null;
        }
        ClassLoader cl = p.getClassLoader();
        Class<?> c = null;
        try {
            c = (Class<?>) findClassMethod.invoke(cl, activity);
        } catch (Throwable e) {
        }
        return c;
    }


    public void setPluginComponent(String className, String plugin, String targetClass) {
        mStates.put(className, new ComponentName(plugin, targetClass));
    }

    public ComponentName lookupContainer(String className) {
        synchronized (mLock) {
            return mStates.get(className);
        }
    }

    public String getPluginName(String targetName) {
        synchronized (mLock) {
            for (ComponentName cn : mStates.values()) {
                if (TextUtils.equals(targetName, cn.getClassName())) {
                    return cn.getPackageName();
                }
            }
        }
        return null;
    }
}
