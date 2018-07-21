package com.example.a41448.huawu.base.TextView;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.example.a41448.huawu.R;

public class ButtonSpan extends ClickableSpan{
    View.OnClickListener mOnClickListener;

    private Context mContext;

    private int colorId;

    public ButtonSpan(Context context,View.OnClickListener mOnClickListener){
        this(context,mOnClickListener, R.color.color_693f3e);
    }

    public ButtonSpan(Context context,View.OnClickListener mOnClickListener,int colorId){
        this.mOnClickListener = mOnClickListener;
        this.mContext = context;
        this.colorId = colorId;
    }

    public void updateDrawState(TextPaint ds){
        ds.setColor(mContext.getResources().getColor(colorId));
        ds.setUnderlineText(false);
    }


    @Override
    public void onClick(View widget) {
        if (mOnClickListener != null){
            mOnClickListener.onClick(widget);
        }
    }
}
