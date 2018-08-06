package com.example.a41448.huawu.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.BaseActivity;
import com.example.a41448.huawu.bean.Players;
import com.example.a41448.huawu.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterFragment extends BaseActivity {

    private FloatingActionButton fab;
    private CardView cvAdd;
    private Button registerButton;
    private EditText registerName;
    private EditText registerPassword;
    private EditText registerAccontId;

    private String userName;//用户登录用id
    private String userPassword;//用户密码
    private String userAccontId;//用户名

    private Context context;
    private Players players;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_fragment);
        ShowEnterAnimation();
        initView();
        context = RegisterFragment.this;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAccontId = registerName.getText().toString();
                userPassword = registerPassword.getText().toString();
                userName = registerAccontId.getText().toString();

                players = new Players(new ArrayList<String>(), new ArrayList<Boolean>(),
                        userAccontId, true, 0, 0);
                players.setUsername(userName);
                players.setPassword(userPassword);
                players.signUp(new SaveListener<Players>() {

                    @Override
                    public void done(Players players, BmobException e) {
                        if (e == null){
                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                            MainActivity.startActivity(context);//暂时如此 在标签选取完成之前暂时跳转到主activity
                        }else {
                            Toast.makeText(RegisterFragment.this, e.toString(), Toast.LENGTH_SHORT).show();
                            if (e.getErrorCode() == 202){
                                AlertDialog dialog = new AlertDialog.Builder(context)
                                        .setTitle("错误")
                                        .setMessage("该账号已被注册！可尝试更换账号或登录！")
                                        .setPositiveButton("继续注册", null)
                                        .setNegativeButton("登录", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                animateRevealClose();
                                            }
                                        })
                                        .show();
                            }else if (e.getErrorCode() == 304){
                                AlertDialog dialog = new AlertDialog.Builder(context)
                                        .setTitle("错误")
                                        .setMessage("账号,用户名及密码不能为空！")
                                        .setPositiveButton("是", null)
                                        .show();
                            }
                        }
                    }
                });
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.cancle_fab);
        cvAdd = (CardView) findViewById(R.id.cv_add);
        registerButton = (Button) findViewById(R.id.register_button);
        registerName = (EditText)findViewById(R.id.register_username);
        registerPassword = (EditText)findViewById(R.id.register_password);
        registerAccontId = (EditText)findViewById(R.id.register_userAccontId);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterFragment.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }
}
