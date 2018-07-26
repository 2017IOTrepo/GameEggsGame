package com.example.a41448.huawu.utils.map_utils;

import android.app.Activity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a41448.huawu.R;

public class ViewUtil {

    private Toast mToast = null;

    private TextView mTextView = null;

    public void showToast(Activity activity, String message) {
        StringBuilder strBuilder = new StringBuilder("<font face='" + "微软雅黑" + "'>");
        strBuilder.append(message).append("</font>");

        View toastRoot = activity.getLayoutInflater().inflate(R.layout.layout_toast, null);
        if (null == mToast || null == mTextView) {
            mToast = new Toast(activity);
            mToast.setView(toastRoot);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mTextView = (TextView) toastRoot.findViewById(R.id.tv_toast_info);
            mTextView.setText( Html.fromHtml(strBuilder.toString()));
        } else {
            mTextView.setText(Html.fromHtml(strBuilder.toString()));
        }
        mToast.setGravity( Gravity.BOTTOM, 0, activity.getResources().getDisplayMetrics().heightPixels / 5);
        mToast.show();
    }
}
