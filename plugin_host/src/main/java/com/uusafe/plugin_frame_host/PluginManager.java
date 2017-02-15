package com.uusafe.plugin_frame_host;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.uusafe.plugin_frame_base.IPluginManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class PluginManager extends IPluginManager {
    private final HashSet<String> mContainerActivities = new HashSet<>();
    private final PluginProcessPer mClientImpl = new PluginProcessPer(this);
    public static final String stubActivity = "com.uusafe.plugin_frame_host.stub.a.ActivityN1ST0";
    private final HashMap<String, Plugin> mPlugins = new HashMap<>();
    private final Context mContext;

    public PluginManager(Context ctx) {
        mContext = ctx;
        initPlugins();
    }

    public void initPlugins() {
        Plugin mockPlugin = new Plugin("plugina", this);
        mockPlugin.attach(mContext, PluginManager.class.getClassLoader().getParent());
        mPlugins.put("plugina", mockPlugin);
    }

    public void startPluginActivity(String pluginName, String activity) {
        mContainerActivities.add(stubActivity);
        mClientImpl.setPluginComponent(stubActivity, pluginName, activity);
        ComponentName cn = new ComponentName(mContext, stubActivity);
        Intent intent = new Intent();
        intent.setComponent(cn);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public Class<?> handleFindClass(Method findClassMethod, String className) {
        if (mContainerActivities.contains(className)) {
            Class<?> c = mClientImpl.findActivityClass(findClassMethod, className);
            if (c != null) {
                return c;
            }
            return DummyActivity.class;
        }
        return findClass(findClassMethod, className);
    }

    private Class<?> findClass(Method findClassMethod, String className) {
        return null;
    }

    public Plugin loadPlugin(String plugin, boolean b) {
        synchronized (mPlugins) {
            Plugin p = mPlugins.get(plugin);
            if (p != null) {
                if (!p.load(b)) {
                    return null;
                }
            }
            return p;
        }
    }

    public Context createActivityContext(Activity activity, Context newBase) {
        String pluginName = mClientImpl.getPluginName(activity.getClass().getName());
        Plugin plugin = loadPlugin(pluginName, true);
        if (plugin != null) {
            return plugin.mLoader.createBaseContext(newBase);
        }
        return null;
    }

    public void handleActivityCreate(Activity activity, Bundle saveInstanceState) {
        if (saveInstanceState != null) {
            saveInstanceState.setClassLoader(activity.getClassLoader());
        }

        Intent intent = activity.getIntent();
        if (intent != null) {
            intent.setExtrasClassLoader(activity.getClassLoader());
        }
    }

    public void handleActivityDestroy(Activity activity) {
    }

    public void handleRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.setClassLoader(activity.getClassLoader());
        }

        Set<String> set = savedInstanceState.keySet();
        if (set != null) {
            for (String key : set) {
                Object obj = savedInstanceState.get(key);
                if (obj instanceof Bundle) {
                    ((Bundle) obj).setClassLoader(activity.getClassLoader());
                }
            }
        }
    }

    public void handleServiceCreate(Service service) {

    }

    public void handleServiceDestroy(Service service) {
    }

    public boolean startActivity(Activity activity, Intent intent) {
        return false;
    }

    public boolean startActivity(Context context, Intent intent, String plugin, String activity, int process, boolean download) {
        return false;
    }
}
