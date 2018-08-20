package com.example.a41448.huawu.base.Contacts;

import android.content.Context;

import com.example.a41448.huawu.bean.Players;

import java.util.ArrayList;
import java.util.List;

public class ContactList  {

    //创建联系人列表
    private static ContactList sContactList;

    private List<Players> mContacts;


    //创建构造器返回联系人列表的数组
    private ContactList(Context context){
        mContacts = new ArrayList<Players>();
    }

    //获取联系人列表
    public static ContactList get(Context context){
        if (sContactList == null){
            //实例化ContactList对象
            sContactList = new ContactList(context);
        }
        return sContactList;
    }

    //创建列表用于返回联系人列表破
    public List<Players> getContacts(){
        return mContacts;
    }

}
