package com.uusafe.plugin_frame_base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class Factory2 {
    public static Context createActivityContext(LoaderActivity activity, Context newBase) {
        if (!PluginFramework.mHostInitialized) {
            return newBase;
        }
        try {
            return (Context) ProxyFactory2Var.createActivityContext.invoke(null, activity, newBase);
        } catch (Exception ignored) {
        }
        return null;
    }

    public static void handleActivityCreate(LoaderActivity activity, Bundle savedInstanceState) {
        if (!PluginFramework.mHostInitialized) {
            return;
        }
        try {
            ProxyFactory2Var.handleActivityCreate.invoke(null, activity, savedInstanceState);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleActivityDestroy(LoaderActivity activity) {
        if (!PluginFramework.mHostInitialized) {
            return;
        }

        try {
            ProxyFactory2Var.handleActivityDestroy.invoke(null, activity);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleRestoreInstanceState(LoaderActivity activity, Bundle savedInstanceState) {
        if (!PluginFramework.mHostInitialized) {
            return;
        }

        try {
            ProxyFactory2Var.handleRestoreInstanceState.invoke(null, activity, savedInstanceState);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean startActivity(LoaderActivity activity, Intent intent) {
        if (!PluginFramework.mHostInitialized) {
            return false;
        }

        try {
            return (Boolean) ProxyFactory2Var.startActivity.invoke(null, activity, intent);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
