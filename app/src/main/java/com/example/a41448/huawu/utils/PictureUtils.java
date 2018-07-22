package com.example.a41448.huawu.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;

public class PictureUtils {

    //保留估算值，根据屏幕的大小估算将要缩放的程度
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();

        //获取屏幕的宽度和高度
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path,size.x,size.y);
    }

    //该方法用于缩放照片
    public static Bitmap getScaledBitmap(String path,int destWidth,int destHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth){
            if (srcWidth > srcHeight){
                inSampleSize = Math.round(srcHeight  / destHeight);
            }else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path,options);
    }

}
