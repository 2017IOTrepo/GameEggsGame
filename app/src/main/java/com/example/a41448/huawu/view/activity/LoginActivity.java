package com.example.a41448.huawu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.BaseActivity;
import com.example.a41448.huawu.utils.FragmentUtils;
import com.example.a41448.huawu.view.fragment.LoginFragment;

public class LoginActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        LoginFragment LoginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentUtils.addFragment(fragmentManager, LoginFragment, R.id.login_fragment_container);
    }
}