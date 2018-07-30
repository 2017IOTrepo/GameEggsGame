package com.example.a41448.huawu.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.base.BaseActivity;
import com.example.a41448.huawu.bean.Messages;
import com.example.a41448.huawu.utils.ActivityCollector;
import com.example.a41448.huawu.utils.FragmentUtils;
import com.example.a41448.huawu.utils.PermissionUtil;
import com.example.a41448.huawu.view.fragment.LoginFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

public class LoginActivity extends BaseActivity{

    private String[] permissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Bmob.initialize(this, "8145941241be2373c4c28c78c52ac64b");
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new Messages());
        }

        requestPermissions();
        LoginFragment LoginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentUtils.addFragment(fragmentManager, LoginFragment, R.id.login_fragment_container);
    }


    private void requestPermissions() {
        permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        PermissionUtil.requestPermissions(this, permissions, new PermissionUtil.OnRequestPermissionsListener() {
            @Override
            public void onGranted() {
                Toast.makeText(LoginActivity.this, "所有权限均已同意", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied() {
                AlertDialog deniedDialog = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("错误！")
                        .setMessage("有权限未同意!")
                        .setCancelable(false)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCollector.finishiAll();
                            }
                        })
                        .show();

            }
        });
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}