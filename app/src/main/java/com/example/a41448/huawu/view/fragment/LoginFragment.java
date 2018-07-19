package com.example.a41448.huawu.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.BaseActivity;
import com.example.a41448.huawu.utils.FragmentUtils;
import com.example.a41448.huawu.view.activity.MainActivity;

public class LoginFragment extends Fragment{

    private EditText mAccoutNumber;//账号
    private EditText mAccoutPassword;//密码
    private CheckBox mRemeberPasswordCheck;//记住密码
    private CheckBox mAutoLoginCheck;//自动登录
    private ActionProcessButton mLoginButton;//登录按钮
    private TextView mRegisterButton;

    private boolean FirstLoginAccout = false;//判断是否为第一次登陆
    private Context context;
    private View view;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.login_fragment, container, false);
        mAccoutNumber = (EditText)view.findViewById(R.id.accont_number_edit_text);
        mAccoutPassword = (EditText)view.findViewById(R.id.accont_password_edit_text);
        mRemeberPasswordCheck = (CheckBox)view.findViewById(R.id.remeber_passworld_checkBox);
        mAutoLoginCheck = (CheckBox)view.findViewById(R.id.auto_login_checkBox);
        mRegisterButton = (TextView)view.findViewById(R.id.login_register_button);
        mLoginButton = (ActionProcessButton)view.findViewById(R.id.login_button);

        fragmentManager = getFragmentManager();
        context = getContext();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginButton.setEnabled(false);
                mAccoutNumber.setEnabled(false);
                mAccoutPassword.setEnabled(false);
                mLoginButton.setMode(ActionProcessButton.Mode.PROGRESS);

                if (!FirstLoginAccout && login()){//此处应用多线程与服务器连接
                    mLoginButton.setMode(ActionProcessButton.Mode.ENDLESS);
                    //到主activity
                    MainActivity.startActivity(getActivity());
                }else if (FirstLoginAccout && login()){
                    fragmentReplace(new LableChosingFragment());
                    //到职业选取跟tag选取activity
                }else{
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("登录错误")
                            .setCancelable(false)
                            .setMessage("未找到账号 请检查账号密码是否正确")
                            .setNegativeButton("暂不注册",null)
                            .setPositiveButton("注册账号", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fragmentReplace(new RegisterFragment());//注册可用dialog代替
                                }
                            })
                            .show();
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentReplace(new RegisterFragment());
            }
        });

        return view;
    }

    /*
    * 登陆函数 未完成
    * */
    private boolean login() {

        return true;
    }

    private void fragmentReplace(Fragment fragment){
        FragmentUtils.replaceFragment(fragmentManager, fragment, R.id.login_fragment_container);
    }

}