package com.example.a41448.huawu.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by xmmmmovo on 2018/7/16.
 *
 * 玩家的实体类
 * 登录用
 * bmob账号密码群内公告可见
 *
 */

public class Players extends BmobUser {

    private List<String> lables;
    private String userAccontId;

    public Players() {
    }

    public Players(List<String> lables, String userAccontId, boolean isFirstLogin) {
        this.lables = lables;
        this.userAccontId = userAccontId;
        this.isFirstLogin = isFirstLogin;
    }

    public String getUserAccontId() {
        return userAccontId;
    }

    public void setUserAccontId(String userAccontId) {
        this.userAccontId = userAccontId;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    private boolean isFirstLogin;

    public List<String> getLables() {
        return lables;
    }

    public void setLables(List<String> lables) {
        this.lables = lables;
    }

}
