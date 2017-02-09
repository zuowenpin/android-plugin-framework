package com.uusafe.plugin_frame_host.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class AssetsUtils {
    public static final boolean quickExtractTo(Context context, final String name, final String dir,
                                               final String dstName, boolean verify, byte endec[], String dexOutputDir) {
        File file = new File(dir + "/" + dstName);

        //建立子目录
        File d = file.getParentFile();
        if (!d.exists()) {
            if (!d.mkdirs()) {
                return false;
            }
        }

        //检查创建是否成功
        if (!d.exists() || !d.isDirectory()) {
            return false;
        }

        //文件已存在
        if (file.exists()) {
            //先删除对应的Dex文件
            if (((dstName.endsWith(".jar") || dstName.endsWith(".apk")) && !TextUtils.isEmpty(dexOutputDir))) {
                String baseName = dstName.substring(0, dstName.length());
                File dexf = new File(dexOutputDir + "/" + baseName + ".dex");
                if (dexf.exists()) {
                    dexf.delete();
                    if (dexf.exists()) {
                        return false;
                    }
                }
            }

            if (!file.delete()) {
                return false;
            }

            if (file.exists()) {
                return false;
            }
        }
        return extractTo(context, name, dir, dstName, endec);
    }

    public static boolean extractTo(Context context, final String name, final String dir, final String dstName,
                                    byte endec[]) {
        File file = new File(dir + "/" + dstName);

        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getAssets().open(name);
            os = new FileOutputStream(file);
            byte bs[] = new byte[4096];
            int rc = 0;
            if (endec != null && endec.length > 0) {
                int idx = 0;
                while ((rc = is.read(bs)) >= 0) {
                    if (rc > 0) {
                        for (int i = 0; i < rc; i++) {
                            bs[i] ^= endec[idx++];
                            if (idx >= endec.length) {
                                idx = 0;
                            }
                        }
                        os.write(bs, 0, rc);
                    }
                }
            } else {
                while ((rc = is.read(bs)) >= 0) {
                    if (rc > 0) {
                        os.write(bs, 0, rc);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
