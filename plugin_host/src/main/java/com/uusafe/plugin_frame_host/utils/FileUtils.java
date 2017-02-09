package com.uusafe.plugin_frame_host.utils;

/**
 * Created by ${zuowp291} on 2017/2/10.
 */

public class FileUtils {
    public static final int S_IRWXU = 00700;
    public static final int S_IRUSR = 00400;
    public static final int S_IWUSR = 00200;
    public static final int S_IXUSR = 00100;

    public static final int S_IRWXG = 00070;
    public static final int S_IRGRP = 00040;
    public static final int S_IWGRP = 00020;
    public static final int S_IXGRP = 00010;

    public static final int S_IRWXO = 00007;
    public static final int S_IROTH = 00004;
    public static final int S_IWOTH = 00002;
    public static final int S_IXOTH = 00001;

    public static int setPermissions(String filePath, int mode, int uid, int gid) {
        Object retObj = ReflectUtils.invokeStaticMethod("android.os.FileUtils", "setPermissions", new Class[]{
                String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE
        }, new Object[]{filePath, mode, uid, gid});

        if (retObj instanceof Integer) {
            return ((Integer) retObj).intValue();
        }
        return -1;
    }
}
