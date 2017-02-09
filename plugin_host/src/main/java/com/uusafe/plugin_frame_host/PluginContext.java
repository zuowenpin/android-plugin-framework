package com.uusafe.plugin_frame_host;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;

import com.uusafe.plugin_frame_host.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class PluginContext extends ContextThemeWrapper {

    private final ClassLoader mNewClassLoader;
    private final Resources mNewResources;
    private final String mPlugin;
    private final Loader mLoader;
    private final Object mSync = new Object();
    private File mFilesDir;
    private File mCacheDir;
    private File mDatabaseDir;
    private LayoutInflater mInflater;

    LayoutInflater.Factory mFactory = new LayoutInflater.Factory() {
        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return handleCreateView(name, context, attrs);
        }
    };

    public PluginContext(Context base, int themeRes, ClassLoader cl, Resources r, String plugin, Loader loader) {
        super(base, themeRes);
        mNewClassLoader = cl;
        mNewResources = r;
        mPlugin = plugin;
        mLoader = loader;
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mNewClassLoader != null) {
            return mNewClassLoader;
        }
        return super.getClassLoader();
    }

    @Override
    public Resources getResources() {
        if (mNewResources != null) {
            return mNewResources;
        }
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (mNewResources != null) {
            return mNewResources.getAssets();
        }
        return super.getAssets();
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                LayoutInflater inflater = (LayoutInflater) super.getSystemService(name);
                mInflater = inflater.cloneInContext(this);
                mInflater.setFactory(mFactory);
                //后续可再次设置工厂
                mInflater = mInflater.cloneInContext(this);
            }
            return mInflater;
        }
        return super.getSystemService(name);
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        name = "plugin_" + name;
        return super.getSharedPreferences(name, mode);
    }

    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        File f = makeFilename(getFilesDir(), name);
        return new FileInputStream(f);
    }

    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        final boolean append = (mode & MODE_APPEND) != 0;
        File f = makeFilename(getFilesDir(), name);
        try {
            FileOutputStream fos = new FileOutputStream(f, append);
            setFilePermissionFromMode(f.getPath(), mode, 0);
            return fos;
        } catch (FileNotFoundException e) {
        }

        File parent = f.getParentFile();
        parent.mkdir();
        FileUtils.setPermissions(parent.getPath(), FileUtils.S_IRWXU | FileUtils.S_IRWXG | FileUtils.S_IXOTH, -1, -1);
        FileOutputStream fos = new FileOutputStream(f, append);
        setFilePermissionFromMode(f.getPath(), mode, 0);
        return fos;
    }

    @Override
    public boolean deleteFile(String name) {
        File f = makeFilename(getFilesDir(), name);
        return f.delete();
    }

    @Override
    public File getFilesDir() {
        synchronized (mSync) {
            if (mFilesDir == null) {
                mFilesDir = new File(getDataDirFile(), "files");
            }

            if (!mFilesDir.exists()) {
                if (!mFilesDir.mkdirs()) {
                    if (mFilesDir.exists()) {
                        return mFilesDir;
                    }
                    return null;
                }

                FileUtils.setPermissions(mFilesDir.getPath(),
                        FileUtils.S_IRWXU | FileUtils.S_IRWXG | FileUtils.S_IXOTH, -1, -1);
            }
            return mFilesDir;
        }
    }

    @Override
    public File getCacheDir() {
        synchronized (mSync) {
            if (mCacheDir == null) {
                mCacheDir = new File(getDataDirFile(), "cache");
            }

            if (!mCacheDir.exists()) {
                if (!mCacheDir.mkdirs()) {
                    if (mCacheDir.exists()) {
                        return mCacheDir;
                    }
                    return null;
                }
                FileUtils.setPermissions(mCacheDir.getPath(),
                        FileUtils.S_IRWXU | FileUtils.S_IRWXG | FileUtils.S_IXOTH, -1, -1);
            }
        }
        return mCacheDir;
    }

    private File getDatabasesDir() {
        synchronized (mSync) {
            if (mDatabaseDir == null) {
                mDatabaseDir = new File(getDataDirFile(), "databases");
            }

            if (mDatabaseDir.getPath().equals("databases")) {
                mDatabaseDir = new File("/data/System");
            }
            return mDatabaseDir;
        }
    }

    private File validateFilePath(String name, boolean createDirectory) {
        File dir;
        File f;
        if (name.charAt(0) == File.separatorChar) {
            String dirPath = name.substring(0, name.lastIndexOf(File.separatorChar));
            dir = new File(dirPath);
            name = name.substring(name.lastIndexOf(File.separatorChar));
            f = new File(dir, name);
        } else {
            dir = getDatabasesDir();
            f = makeFilename(dir, name);
        }
        if (createDirectory && !dir.isDirectory() && dir.mkdir()) {
            FileUtils.setPermissions(dir.getPath(), FileUtils.S_IRWXU | FileUtils.S_IRWXG | FileUtils.S_IXOTH, -1, -1);
        }
        return f;
    }

    public File getDir(String name, int mode) {
        name = "app_" + name;
        File file = makeFilename(getDataDirFile(), name);
        if (!file.exists()) {
            file.mkdir();
            setFilePermissionFromMode(file.getPath(), mode, FileUtils.S_IRWXU | FileUtils.S_IRWXG | FileUtils.S_IXOTH);
        }
        return file;
    }

    private final File getDataDirFile() {
        File dir0 = getBaseContext().getFilesDir();
        File dir = new File(dir0, Constant.LOCAL_PLUGIN_DATA_SUB_DIR);

        if (!dir.exists()) {
            if (!dir.mkdir()) {
                return null;
            }
            setFilePermissionFromMode(dir.getPath(), 0, FileUtils.S_IRWXU | FileUtils.S_IRWXG | FileUtils.S_IXOTH);
        }
        //plugin specific
        File file = makeFilename(dir, mPlugin);
        if (!file.exists()) {
            if (!file.mkdir()) {
                return null;
            }
            setFilePermissionFromMode(file.getPath(), 0, FileUtils.S_IRWXU | FileUtils.S_IRWXG | FileUtils.S_IXOTH);
        }
        return file;
    }

    private final File makeFilename(File base, String name) {
        if (name.indexOf(File.separatorChar) < 0) {
            return new File(base, name);
        }
        throw new IllegalArgumentException("file name invalid:" + name);
    }

    private View handleCreateView(String name, Context context, AttributeSet attrs) {
        if (mLoader.mIgnores.contains(name)) {
            return null;
        }
        Constructor<?> construct = mLoader.mConstructors.get(name);
        if (construct == null) {
            //find class
            Class<?> c = null;
            boolean found = false;
            do {
                try {
                    c = mNewClassLoader.loadClass(name);
                    if (c == null) {
                        break;
                    }
                    if (c == ViewStub.class) {
                        //special class of system
                        break;
                    }

                    if (c.getClassLoader() != mNewClassLoader) {
                        //not plugin class, leave alone
                        break;
                    }
                    found = true;

                } catch (ClassNotFoundException e) {
                    break;
                }
            } while (false);
            if (!found) {
                mLoader.mIgnores.add(name);
                return null;
            }
            try {
                construct = c.getConstructor(Context.class, AttributeSet.class);
                mLoader.mConstructors.put(name, construct);
            } catch (Exception e) {
                InflateException ie = new InflateException("can not inflate class:" + name);
                ie.initCause(e);
                throw ie;
            }
        }
        //Construct
        try {
            View v = (View) construct.newInstance(context, attrs);
            return v;
        } catch (Exception e) {
            InflateException ie = new InflateException("class constructor failed:" + name);
            ie.initCause(e);
            throw ie;
        }
    }

    private final void setFilePermissionFromMode(String name, int mode, int extraPermissions) {
        int perms = FileUtils.S_IRUSR | FileUtils.S_IWUSR | FileUtils.S_IRGRP | FileUtils.S_IWGRP | extraPermissions;
        if ((mode & MODE_WORLD_READABLE) != 0) {
            perms |= FileUtils.S_IROTH;
        }
        if ((mode & MODE_WORLD_WRITEABLE) != 0) {
            perms |= FileUtils.S_IWOTH;
        }
        FileUtils.setPermissions(name, perms, -1, -1);
    }
}
