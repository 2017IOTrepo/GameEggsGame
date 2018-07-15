package com.example.a41448.huawu.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by xmmmmovo on 2018/7/15.
 *  来源网络 非本人编写
 */

public class FragmentUtils {

    /**
     * 添加Fragment到布局容器中
     * @param fragmentManager 传入的FragmentManager
     * @param fragment 要添加的Fragment
     * @param containerId 要添加到的布局容器ID
     */
    public static void addFragment(@NonNull FragmentManager fragmentManager,
                                   @NonNull Fragment fragment, int containerId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(containerId, fragment);
        transaction.commit();
    }

    /**
     * 替换布局容器里的Fragment
     * @param fragmentManager 传入的FragmentManager
     * @param fragment 要替换成的Fragment
     * @param containerId 被替换的布局容器
     */

    public static void replaceFragment(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment fragment, int containerId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
