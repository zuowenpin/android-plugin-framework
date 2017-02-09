package com.uusafe.plugin_frame_host.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by ${zuowp291} on 2017/2/10.
 */

public class ReflectUtils {
    public static Object getField(Class<?> c, Object object, String fName) throws NoSuchFieldException, IllegalAccessException {
        Field f = c.getDeclaredField(fName);
        boolean acc = f.isAccessible();
        if (!acc) {
            f.setAccessible(true);
        }
        Object o = f.get(object);
        if (!acc) {
            f.setAccessible(acc);
        }
        return o;
    }


    public static Object invokeStaticMethod(String clzName,
                                            String methodName,
                                            Class<?>[] methodParamTypes,
                                            Object... methodParamValues) {
        try {
            Class clz = Class.forName(clzName);
            if (clz != null) {
                Method med = clz.getMethod(methodName, methodParamTypes);
                if (med != null) {
                    med.setAccessible(true);
                    Object retObj = med.invoke(null, methodParamValues);
                    return retObj;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
