package com.example.a41448.huawu.chatUI.utils;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.tools.views.RatioImageView;
import com.example.a41448.huawu.utils.ImageLoaderUtil;
import com.example.a41448.huawu.view.fragment.DynamicsFragment;
import com.example.a41448.huawu.view.fragment.PhotoFragment;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.client.utils.URIUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class NineGridTestLayout extends NineGridLayout {

    protected static final int MAX_W_H_RATIO = 3;

    private ImageView mPhotoView = findViewById( R.id.crime_photo );
    public NineGridTestLayout(Context context) {
        super( context );
    }

    public NineGridTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {
        ImageLoaderUtil.displayImage(mContext, imageView, url, ImageLoaderUtil.getPhotoImageOption(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                int newW;
                int newH;
                if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                }
                setOneImageLayoutParams(imageView, newW, newH);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        ImageLoaderUtil.getImageLoader(mContext).displayImage(url, imageView, ImageLoaderUtil.getPhotoImageOption());
    }

    @Override
    protected void onClickImage(int position, String url, List<String> urlList) {
        Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
        final Dialog dia;
        Context context = getContext();
        dia = new Dialog(context, R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.dialog_photo);
        ImageView imageView = (ImageView) dia.findViewById(R.id.crime_photo);
        try {
            imageView.setImageBitmap( getImage( url ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
        dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
        Window w = dia.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 40;
        dia.onWindowAttributesChanged(lp);
        dia.show();
        imageView.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.dismiss();
                    }
                });
    }

    public static Bitmap getImage(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = conn.getInputStream();
        return BitmapFactory.decodeStream(is);
    }

}
