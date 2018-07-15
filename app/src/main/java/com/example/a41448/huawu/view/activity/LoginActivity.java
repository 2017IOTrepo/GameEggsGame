package com.example.a41448.huawu.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.BaseActivity;

public class LoginActivity extends BaseActivity{

    private EditText mAccoutNumber;//账号
    private EditText mAccoutPassword;//密码
    private CheckBox mRemeberPasswordCheck;//记住密码
    private CheckBox mAutoLoginCheck;//自动登录
    private ActionProcessButton mLoginButton;//登录按钮
    private boolean FirstLoginAccout = false;//判断是否为第一次登陆

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAccoutNumber = (EditText)findViewById(R.id.accont_number_edit_text);
        mAccoutPassword = (EditText)findViewById(R.id.accont_password_edit_text);
        mRemeberPasswordCheck = (CheckBox)findViewById(R.id.remeber_passworld_checkBox);
        mAutoLoginCheck = (CheckBox)findViewById(R.id.auto_login_checkBox);

        mLoginButton = (ActionProcessButton)findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginButton.setEnabled(false);
                mAccoutNumber.setEnabled(false);
                mAccoutPassword.setEnabled(false);
                mLoginButton.setMode(ActionProcessButton.Mode.PROGRESS);
                login();//此处应用多线程与服务器连接
                mLoginButton.setMode(ActionProcessButton.Mode.ENDLESS);

                if (!FirstLoginAccout){
                    //到主activity
                    MainActivity.startActivity(LoginActivity.this);
                }else{
                    //到职业选取跟tag选取activity
                }
            }
        });
    }

    /*
    * 登陆函数 未完成
    * */
    private boolean login() {

        return true;
    }


}