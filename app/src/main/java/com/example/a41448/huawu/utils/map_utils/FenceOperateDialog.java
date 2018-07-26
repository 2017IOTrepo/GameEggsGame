package com.example.a41448.huawu.utils.map_utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.trace.api.fence.FenceShape;
import com.baidu.trace.api.fence.FenceType;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.view.activity.PointActivity;

/**
 * 围栏创建对话框
 */
public class FenceOperateDialog extends PopupWindow{


    private View mView = null;
    private Button alarmBtn = null;
    private Button updateBtn = null;
    private Button deleteBtn = null;
    private Button cancelBtn = null;
    private TextView titleText = null;

    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    public FenceOperateDialog(final PointActivity parent) {
        super(parent);
        LayoutInflater inflater = (LayoutInflater) parent
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate( R.layout.dialog_fence_operate, null);
        alarmBtn = (Button) mView.findViewById(R.id.btn_fenceOperate_alarm);
        updateBtn = (Button) mView.findViewById(R.id.btn_fenceOperate_update);
        deleteBtn = (Button) mView.findViewById(R.id.btn_fenceOperate_delete);
        cancelBtn = (Button) mView.findViewById(R.id.btn_all_cancel);
        titleText = (TextView) mView.findViewById(R.id.tv_dialog_title);
        alarmBtn.setOnClickListener(parent);
        updateBtn.setOnClickListener(parent);
        deleteBtn.setOnClickListener(parent);
        titleText.setText("围栏操作");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setContentView(mView);
        setFocusable(false);
        setOutsideTouchable(false);
        setWidth( ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight( ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.dialog_anim_style);
        setBackgroundDrawable(null);

    }
}
