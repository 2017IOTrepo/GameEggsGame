package com.example.a41448.huawu.view.sideslip.Shop;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a41448.huawu.R;


class DialogUtils  {

    private static TextView tipTextView;

    private static Dialog ProgressDialog;

    private  static  CustomStatusView customStatusView;



    public static void showCompleteDialog(Context context, String msg){

        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(R.layout.shop_dialog_toast, null);// 得到加载view

        tipTextView = v.findViewById(R.id.tv_toast_content);// 提示文字

        tipTextView.setText(msg);// 设置加载信息
        customStatusView = v.findViewById(R.id.as_status);
        customStatusView.loadLoading();



        ProgressDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog

        ProgressDialog.setCancelable(true); // 是否可以按“返回键”消失

        ProgressDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域

        ProgressDialog.setContentView(v, new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        /**

         *将显示Dialog的方法封装在这里面

         */

        final Window window = ProgressDialog.getWindow();

        WindowManager.LayoutParams lp = window.getAttributes();

        lp.width =1000;
        lp.height =800;

        lp.y=-150;

        window.setGravity(Gravity.CENTER_HORIZONTAL);

        window.setAttributes(lp);

        window.setWindowAnimations(R.style.PopWindowAnimStyle);
         ProgressDialog.show();

         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 customStatusView.loadSuccess();
             }
         },2500);


    }



    public class AutoDismissDialog extends Dialog{
        //handler 用来更新UI的一套机制也是消息机制
        private final Handler handler = new Handler();

        public AutoDismissDialog(@NonNull Context context) {
            super(context);
        }

        public AutoDismissDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

        @Override
        protected   void onStart() {
            super.onStart();
            if (handler != null) {
                //这里用到了handler的定时器效果 延迟2秒执行dismiss();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ProgressDialog.dismiss();
                    }
                }, 4000);
            }
        }


    }
}