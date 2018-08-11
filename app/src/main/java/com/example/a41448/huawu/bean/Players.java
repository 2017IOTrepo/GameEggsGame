package com.example.a41448.huawu.bean;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by xmmmmovo on 2018/7/16.
 *
 * 玩家的实体类
 * 登录用
 * bmob账号密码群内公告可见
 *
 */

public class Players extends BmobUser {

    private List<String> lables;//标签
    private List<Boolean> achievement;//成就 true为获得
    private List<Integer> items;//道具
    private String userAccontId;//用户id
    private boolean sex = true; // 性别 true为男性 默认为男性
    private BmobGeoPoint location;// 地点坐标记录
    private int points;
    private int coins;
    private BmobFile avatar = null;//头像
    private String lastMessage = "null";//最后消息
    private boolean isOnline = true;//是否在线

    public Players() {
    }

    public Players(String userAccontId, boolean sex, BmobFile avatar, String lastMessage, boolean isOnline) {
        this.userAccontId = userAccontId;
        this.sex = sex;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
        this.isOnline = isOnline;
    }

    public Players(List<String> lables, List<Boolean> achievement, String userAccontId, boolean isFirstLogin, int points, int coins) {
        this.lables = lables;
        this.userAccontId = userAccontId;
        this.isFirstLogin = isFirstLogin;
        this.points = points;
        this.coins = coins;
        this.achievement = achievement;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }

    public List<Boolean> getAchievement() {
        return achievement;
    }

    public void setAchievement(List<Boolean> achievement) {
        this.achievement = achievement;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
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

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }


    public BmobGeoPoint getLocation() {
        return location;
    }

    public void setLocation(BmobGeoPoint location) {
        this.location = location;
    }


}