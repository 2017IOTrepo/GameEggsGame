package com.example.a41448.huawu.base.Contacts;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NineGridTestModel implements Serializable {
    private static final long serialVersionUID = 2189052605715370758L;

    public List<String> urlList = new ArrayList<>();

    public boolean isShowAll = false;

    public int image;

    public String name;

    public String time;

    public String start;

}
