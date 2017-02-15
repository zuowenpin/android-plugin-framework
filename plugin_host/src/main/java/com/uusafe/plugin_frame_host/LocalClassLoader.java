package com.uusafe.plugin_frame_host;

import com.uusafe.plugin_frame_base.IPluginManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created by ${zuowp291} on 2017/2/10.
 */

@SuppressWarnings("unchecked")
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

            findResourcesMethod = c.getDeclaredMethod("findResources", String.class);
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
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        Class<?> c = mPluginManager.handleFindClass(findClassMethod, className);
        if (c != null) {
            return c;
        }
        try {
            c = (Class<?>) findClassMethod.invoke(mBase, className);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (c != null) {
            return c;
        }
        return super.findClass(className);
    }

    @Override
    protected Enumeration<URL> findResources(String resName) throws IOException {
        try {
            return (Enumeration<URL>) findResourcesMethod.invoke(mBase, resName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.findResources(resName);
    }

    @Override
    protected String findLibrary(String libname) {
        try {
            return (String) findLibraryMethod.invoke(mBase, libname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.findLibrary(libname);
    }

    @Override
    protected Package getPackage(String name) {
        try {
            return (Package) getPackageMethod.invoke(mBase, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.getPackage(name);
    }

    @Override
    public String toString() {
        return getClass().getName() + "[mBase=" + mBase.toString() + "]";
    }
}
