package com.example.a41448.huawu.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xmmmmovo on 2018/7/15.
 */

public class BaseActivity extends AppCompatActivity{
    private List<Activity> ActivityCollector = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ActivityCollector.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.remove(this);
    }

    public void finishall() {
        for (Activity activity:
             ActivityCollector) {
            activity.finish();
        }
    }
}
