package com.uusafe.plugin_frame_host;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

import dalvik.system.DexClassLoader;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class Loader {
    private final Context mContext;
    private final String mName;
    private final String mPath;
    Context mPkgContext;
    ClassLoader mClassLoader;
    PackageInfo mPackageInfo;
    Resources mPkgResources;
    Method mCreateMethod;
    Method mCreateMethod2;
    Object mPlugin;

    HashSet<String> mIgnores = new HashSet<String>();
    HashMap<String, Constructor<?>> mConstructors = new HashMap<>();

    public Loader(Context context, String plginName, String path) {
        mContext = context;
        mName = plginName;
        mPath = path;
    }

    public boolean loadDex(ClassLoader parent) {
        PackageManager pm = mContext.getPackageManager();
        if (mPackageInfo == null) {
            mPackageInfo = pm.getPackageArchiveInfo(mPath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES
                    | PackageManager.GET_PROVIDERS | PackageManager.GET_META_DATA);
            if (mPackageInfo == null || mPackageInfo.applicationInfo == null) {
                return false;
            }

            mPackageInfo.applicationInfo.sourceDir = mPath;
            mPackageInfo.applicationInfo.publicSourceDir = mPath;
        }

        if (mPkgResources == null) {
            try {
                mPkgResources = pm.getResourcesForApplication(mPackageInfo.applicationInfo);
            } catch (Exception e) {
                return false;
            }
        }

        String out = mContext.getDir(Constant.LOCAL_PLUGIN_ODEX_SUB_DIR, 0).getPath();
        parent = getClass().getClassLoader().getParent();
        mClassLoader = new DexClassLoader(mPath, out, null, parent);
        mPkgContext = new PluginContext(mContext, android.R.style.Theme, mClassLoader, mPkgResources, mName, this);
        return true;
    }

    final boolean loadEntryMethod2() {
        try {
            String className = Factory.PLUGIN_ENTRY_PACKAGE_PREFIX + "."
                    + mName + "." + Factory.PLUGIN_ENTRY_CLASS_NAME;
            Class<?> c = mClassLoader.loadClass(className);
            mCreateMethod2 = c.getDeclaredMethod(Factory.PLUGIN_ENTRY_EXPORT_METHOD_NAME,
                    Factory.PLUGIN_ENTRY_EXPORT_METHOD2_PARAMS);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return mCreateMethod2 != null;
    }

    final boolean loadEntryMethod(boolean log) {
        try {
            String className = Factory.PLUGIN_ENTRY_PACKAGE_PREFIX + "."
                    + mName + "." + Factory.PLUGIN_ENTRY_CLASS_NAME;
            Class<?> c = mClassLoader.loadClass(className);
            mCreateMethod = c.getDeclaredMethod(Factory.PLUGIN_ENTRY_EXPORT_METHOD_NAME,
                    Factory.PLUGIN_ENTRY_EXPORT_METHOD_PARAMS);
        } catch (Throwable e) {
            if (log) {
                e.printStackTrace();
            }
        }
        return mCreateMethod != null;
    }

    final boolean invoke() {
        try {
            mPlugin = mCreateMethod.invoke(null, mPkgContext, getClass().getClassLoader());
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    final Context createBaseContext(Context newBase) {
        return new PluginContext(newBase, android.R.style.Theme,
                mClassLoader, mPkgResources, mName, this);
    }

    final boolean isLoaded() {
        return mPlugin != null;
    }
}
