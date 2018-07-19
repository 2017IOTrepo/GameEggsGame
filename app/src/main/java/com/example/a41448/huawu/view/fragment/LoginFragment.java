package com.example.a41448.huawu.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.example.a41448.huawu.view.activity.LoginActivity;
import com.example.a41448.huawu.view.activity.MainActivity;

public class LoginFragment extends Fragment{

    private EditText mAccoutNumber;//账号
    private EditText mAccoutPassword;//密码
    private CheckBox mRemeberPasswordCheck;//记住密码
    private CheckBox mAutoLoginCheck;//自动登录
    private ActionProcessButton mLoginButton;//登录按钮
    private FloatingActionButton mRegisterButton;//注册按钮
    private CardView cardView;

    private boolean FirstLoginAccout = false;//判断是否为第一次登陆
    private Context context;
    private View view;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);

        initView();

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
                    FragmentUtils.replaceFragment(fragmentManager, new LableChosingFragment(), R.id.login_fragment_container);
                    //到职业选取跟tag选取activity
                }else{
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("登录错误")
                            .setCancelable(false)
                            .setMessage("未找到账号 请检查账号密码是否正确")
                            .setNegativeButton("暂不注册",null)//后期可加入游客系统
                            .setPositiveButton("注册账号", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                getActivity().getWindow().setExitTransition(null);
                getActivity().getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                        mRegisterButton, mRegisterButton.getTransitionName());
                startActivity(new Intent(context, RegisterFragment.class), options.toBundle());
            }
        });

        return view;
    }

    private void initView() {
        mAccoutNumber = view.findViewById(R.id.et_username);
        mAccoutPassword = view.findViewById(R.id.et_password);
        cardView = view.findViewById(R.id.cv);
        mRegisterButton = view.findViewById(R.id.register_fab);
        mLoginButton = (ActionProcessButton)view.findViewById(R.id.login_button);
    }

    /*
    * 登陆函数 未完成
    * */
    private boolean login() {

        return true;
    }

}