package com.example.a41448.huawu.base.others;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;


//判断当前的网路连接爱你概况
public class CommonUtils {

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    //网络连接为WIFi模式
    public static boolean isWifi(Context context){
        NetworkInfo info = getNetworkInfo( context );;
        if (info == null){
            if (info.getType() == ConnectivityManager.TYPE_WIFI);
        }
        return false;
    }
    public static boolean isMobile(Context context){
        NetworkInfo info = getNetworkInfo( context );
        if (info != null){
            if (info.getType() == ConnectivityManager.TYPE_MOBILE);
        }
        return false;
    }
    //网络连接为流量模式
    private static NetworkInfo getNetworkInfo(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean checkSdCard(){
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED ))
            return true;
        else
            return false;
    }


}
