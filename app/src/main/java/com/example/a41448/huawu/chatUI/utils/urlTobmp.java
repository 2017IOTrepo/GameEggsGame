package com.example.a41448.huawu.chatUI.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class urlTobmp {
    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            int length = conn.getContentLength();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize =2;    // 设置缩放比例
            Rect rect = new Rect(0, 0,0,0);
            bitmap = BitmapFactory.decodeStream(bis,rect,options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
