package com.wenchao.superandfix;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;

public class DexManager {
    private static final DexManager ourInstance = new DexManager();

    public static DexManager getInstance() {
        return ourInstance;
    }

    private DexManager() {
    }

    private Context mContext;

    public void setContext(Context context) {
        this.mContext = context;
    }

    /**
     * 加载dex文件
     */
    public void loadDex(File file) {
        try {
            DexFile dexFile = DexFile.loadDex(file.getAbsolutePath(),
                    new File(mContext.getCacheDir(), "opt").getAbsolutePath(),
                    Context.MODE_PRIVATE);
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String className = entries.nextElement();
                Class fixedClass = dexFile.loadClass(className, mContext.getClassLoader());
                if (null != fixedClass) {
                    fixClass(fixedClass);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fixClass(Class fixedClass) {
        Method[] methods = fixedClass.getDeclaredMethods();
        for (Method method : methods) {
            MethodReplace methodReplace = method.getAnnotation(MethodReplace.class);
            if (null == methodReplace) {
                continue;
            }
            String className = methodReplace.className();
            String methodName = methodReplace.methodName();
            if (!TextUtils.isEmpty(className) && !TextUtils.isEmpty(methodName)) {
                try {
                    Class<?> bugClass = Class.forName(className);
                    Method bugMethod = bugClass.getDeclaredMethod(methodName, method.getParameterTypes());
                    replace(bugMethod, method);
                } catch (ClassNotFoundException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private native void replace(Method bugMethod, Method fixedMethod);

}
