package com.example.a41448.huawu.view.fragment;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.bean.Players;
import com.example.a41448.huawu.utils.FragmentUtils;
import com.example.a41448.huawu.view.activity.MainActivity;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginFragment extends Fragment{

    private EditText mAccoutNumber;//账号
    private EditText mAccoutPassword;//密码
    private CheckBox mRemeberPasswordCheck;//记住密码
    private CheckBox mAutoLoginCheck;//自动登录
    private ActionProcessButton mLoginButton;//登录按钮
    private FloatingActionButton mRegisterButton;//注册按钮
    private CardView cardView;

    private String userName;
    private String userPassword;
    private Context context;
    private View view;
    private FragmentManager fragmentManager;
    private Players players;
    private BmobIM bmobIM;
    private BmobIMUserInfo bmobIMUserInfo;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean ifAutoLogin;
    private boolean ifRemeberPw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);

        initView();
        checkBoxCheck();

        mAutoLoginCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("ifAutoLogin", b);
                editor.apply();
            }
        });
        fragmentManager = getFragmentManager();
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDisEnabled();
                mLoginButton.setMode(ActionProcessButton.Mode.PROGRESS);

                userName = mAccoutNumber.getText().toString();
                userPassword = mAccoutPassword.getText().toString();
                if (mRemeberPasswordCheck.isChecked()){
                    editor.putString("userName", userName);
                    editor.putString("userPassword", userPassword);
                }else {
                    editor.putBoolean("ifRemeberCheck", mRemeberPasswordCheck.isChecked());
                }
                editor.apply();
                login();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                register();
            }
        });

        return view;
    }

    private void checkBoxCheck() {
        ifAutoLogin = preferences.getBoolean("ifAutoLogin", false);
        ifRemeberPw = preferences.getBoolean("ifRemeberCheck", false);
        userName = preferences.getString("userName", "null");
        userPassword = preferences.getString("userPassword", "null");
        if (ifAutoLogin){
            setDisEnabled();
            mAccoutNumber.setText(preferences.getString("userName", null));
            mAccoutPassword.setText(preferences.getString("userPassword", null));
            login();
            mAutoLoginCheck.setChecked(ifAutoLogin);
            mRemeberPasswordCheck.setChecked(ifRemeberPw);
        }else if (ifRemeberPw){
            mRemeberPasswordCheck.setChecked(ifRemeberPw);
            mAccoutNumber.setText(preferences.getString("userName", null));
            mAccoutPassword.setText(preferences.getString("userPassword", null));
        }

    }

    private void setDisEnabled() {
        mLoginButton.setEnabled(false);
        mAccoutNumber.setEnabled(false);
        mAccoutPassword.setEnabled(false);
        mRegisterButton.setEnabled(false);
        mAutoLoginCheck.setEnabled(false);
        mRemeberPasswordCheck.setEnabled(false);
    }

    private void setEnabled() {
        mLoginButton.setEnabled(true);
        mAccoutNumber.setEnabled(true);
        mAccoutPassword.setEnabled(true);
        mRegisterButton.setEnabled(true);
        mAutoLoginCheck.setEnabled(true);
        mRemeberPasswordCheck.setEnabled(true);
    }

    //初始化界面
    private void initView() {
        mAccoutNumber = (EditText) view.findViewById(R.id.et_username);
        mAccoutPassword = (EditText) view.findViewById(R.id.et_password);
        cardView = (CardView) view.findViewById(R.id.cv);
        mRegisterButton = (FloatingActionButton) view.findViewById(R.id.register_fab);
        mLoginButton = (ActionProcessButton)view.findViewById(R.id.login_button);
        context = getContext();
        preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = preferences.edit();
        mRemeberPasswordCheck = (CheckBox)view.findViewById(R.id.remeber_passworld_checkBox);
        mAutoLoginCheck = (CheckBox)view.findViewById(R.id.auto_login_checkBox);
    }

    /*
    * 注册跳转
    * */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void register() {
        getActivity().getWindow().setExitTransition(null);
        getActivity().getWindow().setEnterTransition(null);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                mRegisterButton, mRegisterButton.getTransitionName());
        startActivity(new Intent(context, RegisterFragment.class), options.toBundle());
    }

    /*
    * 登陆函数
    * */
    private void login() {
        players = new Players();
        players.setUsername(userName);
        players.setPassword(userPassword);
        players.login(new SaveListener<Players>() {
            @Override
            public void done(Players players, BmobException e) {
                if (e == null){
                    mLoginButton.setMode(ActionProcessButton.Mode.ENDLESS);
                    if (players.isFirstLogin()){
                        FragmentUtils.replaceFragment(fragmentManager,
                                new LableChosingFragment(), R.id.login_fragment_container);
                        //到职业选取跟tag选取activity
                    }else {
                        MainActivity.startActivity(context);
                        //到主activity
                    }
                }else {
                    if (e.getErrorCode() == 109){
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle("登录错误")
                                .setCancelable(false)
                                .setMessage("未找到账号 请检查账号密码是否正确")
                                .setNegativeButton("暂不注册",null)//后期可加入游客系统
                                .setPositiveButton("注册账号", new DialogInterface.OnClickListener() {
                                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        register();
                                    }
                                })
                                .show();
                    }else if (e.getErrorCode() == 304){
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle("登录错误")
                                .setCancelable(false)
                                .setMessage("账号或密码为空！")
                                .setNegativeButton("好的",null)
                                .show();
                    }
                    setEnabled();
                    if (ifAutoLogin){
                        editor.putBoolean("ifAutoLogin", !ifAutoLogin);
                        editor.apply();
                    }
                }
            }
        });
    }
}