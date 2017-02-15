package com.uusafe.plugin_frame_host.utils;

import android.app.Application;
import android.util.Log;

import com.uusafe.plugin_frame_host.LocalClassLoader;
import com.uusafe.plugin_frame_host.PluginManager;

/**
 * Created by ${zuowp291} on 2017/2/10.
 */

public class PatchClassLoaderUtils {
    private static final String TAG = "PatchClassLoaderUtils";

    public static boolean patchAppContextPackageInfoClassLoader(Application app, PluginManager pm) {
        try {
            Class<?> c = Class.forName("android.content.ContentWrapper");
            Object oBase = ReflectUtils.getField(c, app, "mBase");
            c = oBase.getClass();
            Object oPackageInfo = null;

            if ("android.app.ApplicationContext".equals(c.getName())) {
                oPackageInfo = ReflectUtils.getField(c, oBase, "mPackageInfo");
            } else if ("android.app.ContextImpl".equals(c.getName())) {
                oPackageInfo = ReflectUtils.getField(c, oBase, "mPackageInfo");
            } else if ("android.app.AppContextImpl".equals(c.getName())) {
                oPackageInfo = ReflectUtils.getField(c, oBase, "mPackageInfo");
            } else {
                oPackageInfo = ReflectUtils.getField(c, oBase, "mPackageInfo");
                if (oPackageInfo == null) {
                    Log.e(TAG, "not found mPackageInfo");
                    return false;
                }
            }
            c = oPackageInfo.getClass();
            //2.1 2.2
            if ("android.app.ActivityThread$PackageInfo".equals(c.getName())) {
                Log.d(TAG, "patch Context ActivityThread$PackageInfo");
            } else if ("android.app.loadedApk".equals(c.getName())) {
                //2.3.4 or higher
                Log.d(TAG, "patch context loaderApk");
            } else {
                Log.d(TAG, "unknown internal packageInfo: " + c);
            }

            ClassLoader oClassLoader = (ClassLoader) ReflectUtils.getField(c, oPackageInfo, "mClassLoader");
            ClassLoader cl = new LocalClassLoader(oClassLoader.getParent(), oClassLoader, pm);
            ReflectUtils.setField(c, oPackageInfo, "mClassLoader", cl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
