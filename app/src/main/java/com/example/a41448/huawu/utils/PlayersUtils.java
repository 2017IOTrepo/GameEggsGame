package com.example.a41448.huawu.utils;

import com.example.a41448.huawu.R;

public class PlayersUtils {

    public static String setSex(boolean sex){
        String sexReturn = "性别：男";

        if (!sex)
            sexReturn = "性别：女";

        return sexReturn;
    }

    public static int setOnline(boolean on){

        if (on){
            return R.drawable.ic_dot_24dp;
        }else{
            return R.drawable.ic_off_dot_24dp;
        }

    }

}
