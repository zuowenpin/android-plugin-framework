package com.uusafe.plugin_frame_host;

import com.uusafe.plugin_frame_base.IPluginManager;

import java.lang.reflect.Method;

/**
 * Created by ${zuowp291} on 2017/2/10.
 */

public class LocalClassLoader extends ClassLoader {
    private static final String TAG = "LoadClassLoader";
    private final ClassLoader mBase;
    private Method findClassMethod;
    private Method findResourceMethod;
    private Method findResourcesMethod;
    private Method getPackageMethod;
    private Method findLibraryMethod;
    private final PluginManager mPluginManager;

    public LocalClassLoader(ClassLoader parent, ClassLoader base, PluginManager pm) {
        super(parent);
        mBase = base;
        mPluginManager = pm;

        try {
            Class<?> c = Class.forName("java.lang.ClassLoader");
            findClassMethod = c.getDeclaredMethod("findClass", String.class);
            findClassMethod.setAccessible(true);

            findResourceMethod = c.getDeclaredMethod("findResource", String.class);
            findResourceMethod.setAccessible(true);

            findResourcesMethod = c.getDeclaredMethod("findResource", String.class);
            findResourcesMethod.setAccessible(true);

            findLibraryMethod = c.getDeclaredMethod("findLibraryMethod", String.class);
            findLibraryMethod.setAccessible(true);

            getPackageMethod = c.getDeclaredMethod("getPackage", String.class);
            getPackageMethod.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
