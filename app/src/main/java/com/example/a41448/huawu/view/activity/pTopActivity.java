package com.example.a41448.huawu.view.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.comment.ShowType;
import com.example.a41448.huawu.Communication.widget.EmotionEditText;
import com.example.a41448.huawu.tools.listener.SecurityEditCompileListener;
import com.example.a41448.huawu.tools.views.SecurityPasswordEditText;

public class pTopActivity extends AppCompatActivity {

    public static ShowType showType = ShowType.hideNumber;

    private EmotionEditText mEditText;

    public static String ROOMNUMBER = "256";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_p_top );
        mEditText = (EmotionEditText) findViewById( R.id.tv_room_number );
//        if (mEditText.getText().toString() != null){
//            ROOMNUMBER = mEditText.getText().toString();
//        }else {
//            ROOMNUMBER = null;
//        }
        findViewById( R.id.btn_made ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMadeDialog();
            }
        } );
        findViewById( R.id.btn_join ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinDialog();
            }
        } );
    }

    public void showMadeDialog(){
        final AlertDialog dialog = new AlertDialog.Builder( this ).create();
        dialog.setCanceledOnTouchOutside( false );
        dialog.show();

        Window window = dialog.getWindow();
        window.clearFlags( WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM );
        window.setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE );
        window.setContentView( R.layout.activity_input_roommade_dialog );

        final SecurityPasswordEditText editText = (SecurityPasswordEditText) window.findViewById( R.id.security_linear );

        editText.setSecurityEditCompileListener( new SecurityEditCompileListener() {
            @Override
            public void onNumCompleted(String num) {
                Toast.makeText( pTopActivity.this,"你输入的密码是：" + num,Toast.LENGTH_SHORT ).show();
                dialog.dismiss();
            }
        } );
    }

    public void showJoinDialog(){
        final AlertDialog dialog = new AlertDialog.Builder( this ).create();
        dialog.setCanceledOnTouchOutside( false );
        dialog.show();

        Window window = dialog.getWindow();
        window.clearFlags( WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM );
        window.setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE );
        window.setContentView( R.layout.activity_input_join_dailog );

        final SecurityPasswordEditText editText = (SecurityPasswordEditText) window.findViewById( R.id.security_linear );

        editText.setSecurityEditCompileListener( new SecurityEditCompileListener() {
            @Override
            public void onNumCompleted(String num) {
                Toast.makeText( pTopActivity.this,"恭喜加入房间：" + ROOMNUMBER,Toast.LENGTH_SHORT ).show();
                dialog.dismiss();
            }
        } );
    }
}
