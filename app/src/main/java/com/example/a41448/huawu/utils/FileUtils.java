package com.example.a41448.huawu.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

/*
*
* 文件的工具类
* */
public class FileUtils {

    /**
     * 照顾旧机型
     *
     * Gets root path.
     *
     * @param context the context
     * @return the root path
     * @description 获取存储路径(如果有内存卡，这是内存卡根目录，如果没有内存卡，则是软件的包file目录)
     */
    public static String getRootFolder(@NonNull Context context) {
        String rootPath = null;

        if (android.os.Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            rootPath = context.getFilesDir().getAbsolutePath();
        }
        return rootPath;
    }

}
