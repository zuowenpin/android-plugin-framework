package com.uusafe.plugin_frame_base;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class PluginFramework {
    static final Object LOCK = new Object();
    static boolean mInitialized;
    public static boolean mHostInitialized;


    public static final boolean init(ClassLoader cl) {
        synchronized (LOCK) {
            return initLocked(cl);
        }
    }

    private static boolean initLocked(ClassLoader cl) {
        if (mInitialized) {
            return mHostInitialized;
        }
        mInitialized = true;
        try {
            final Class<?> factoryClass = cl.loadClass("com.example.mock.main.i.Factory2");
            init2Locked(factoryClass);
            mHostInitialized = true;
        } catch (Exception ignored) {
        }
        return mHostInitialized;
    }

    private static void init2Locked(Class<?> factory) throws NoSuchMethodException {
        ProxyFactory2Var.createActivityContext = factory.getDeclaredMethod("createActivityContext", Activity.class,
                Context.class);
        ProxyFactory2Var.handleActivityCreate = factory.getDeclaredMethod("handleActivityCreate", Activity.class, Bundle.class);
        ProxyFactory2Var.handleActivityDestroy = factory.getDeclaredMethod("handleActivityDestroy", Activity.class);
        ProxyFactory2Var.handleRestoreInstanceState = factory.getDeclaredMethod("handleRestoreInstanceState",
                Activity.class, Bundle.class);
        ProxyFactory2Var.handleServiceCreate = factory.getDeclaredMethod("handleServiceCreate", Service.class);
        ProxyFactory2Var.handleServiceDestroy = factory.getDeclaredMethod("handleServiceDestroy", Service.class);
        ProxyFactory2Var.startActivity = factory.getDeclaredMethod("startActivity", Activity.class, Intent.class);
        ProxyFactory2Var.startActivity2 = factory.getDeclaredMethod("startActivity", Context.class, Intent.class,
                String.class, String.class, int.class, boolean.class);
    }
}
