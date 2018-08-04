package com.example.a41448.huawu.base.comment;

public class SearchTag {

    public String title,about,time,message_num,imageUri;
    public int photo,viewtype;

    public SearchTag(String title, String about, String imageUri, int viewtype,String time,String message_num) {
        this.title = title;
        this.about = about;
        this.imageUri = imageUri;
        this.viewtype = viewtype;
        this.time = time;
        this.message_num = message_num;
    }
}
