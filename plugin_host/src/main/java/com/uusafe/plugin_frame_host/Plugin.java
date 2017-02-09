package com.uusafe.plugin_frame_host;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;

import com.uusafe.plugin_frame_base.IPluginManager;
import com.uusafe.plugin_frame_host.utils.AssetsUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class Plugin {
    final String mPluginName;
    final IPluginManager mManager;
    Loader mLoader;
    private Context mContext;
    private ClassLoader mParent;
    final HashMap<String, ActivityInfo> mActivities = new HashMap<>();

    public ClassLoader getClassLoader() {
        if (mLoader == null) {
            return null;
        }
        return mLoader.mClassLoader;
    }


    public Plugin(String pluginName, IPluginManager manager) {
        this.mPluginName = pluginName;
        mManager = manager;
    }

    final void attach(Context context, ClassLoader parent) {
        mContext = context;
        mParent = parent;
    }

    private boolean mInitialized;

    public boolean load(boolean b) {
        if (mInitialized) {
            if (mLoader == null) {
                return false;
            }
            return mLoader.isLoaded();
        }
        mInitialized = true;
        return doLoad(mContext, mParent, mPluginName);
    }

    private boolean doLoad(Context context, ClassLoader parent, String pluginName) {
        File dir = context.getDir(Constant.LOCAL_PLUGIN_SUB_DIR, 0);
        File dexDir = context.getDir(Constant.LOCAL_PLUGIN_ODEX_SUB_DIR, 0);
        String dstName = pluginName + ".jar";
        boolean rc = AssetsUtils.quickExtractTo(context, dstName, dir.getAbsolutePath(), dstName, false, null, dexDir.getAbsolutePath());
        if (!rc) {
            return false;
        }
        File file = new File(dir, dstName);
        mLoader = new Loader(context, pluginName, file.getAbsolutePath());

        if (!mLoader.loadDex(parent)) {
            return false;
        }
        PackageInfo pi = mLoader.mPackageInfo;
        if (pi.activities != null) {
            for (ActivityInfo ai : pi.activities) {
                mActivities.put(ai.name, ai);
            }
        }
        //currently we have load entry method
        if (mLoader.loadEntryMethod(false)) {
            if (!mLoader.invoke()) {
                return false;
            }
        }
        return true;
    }
}
