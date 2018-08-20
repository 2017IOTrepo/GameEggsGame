package com.example.a41448.huawu.view.sideslip.Label;


import com.example.a41448.huawu.view.sideslip.Label.bean.SimpleTitleTip;
import com.example.a41448.huawu.view.sideslip.Label.bean.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xuchao on 2018/7/26 0026.
         */
public class TipDataModel {
    public  static String[] dragTips ={"宅男","运动狂","直播","体育","健身","科技","段子","漫画","手机控","爬山","长跑","孤独","外卖"
            ,"游泳","旅游","网游"};
    public static String[] addTips ={"二次元","颜控","自我","星族","自由","高冷","读书","外向","文艺","小清新","嘻哈","微博"};
    public static List<Tip> getDragTips(){
        List<Tip> result = new ArrayList<>();
        for(int i=0;i<dragTips.length;i++){
            String temp =dragTips[i];
            SimpleTitleTip tip = new SimpleTitleTip();
            tip.setTip(temp);
            tip.setId(i);
            result.add(tip);
        }
        return result;
    }
    public static List<Tip> getAddTips(){
        List<Tip> result = new ArrayList<>();
        for(int i=0;i<addTips.length;i++){
            String temp =addTips[i];
            SimpleTitleTip tip = new SimpleTitleTip();
            tip.setTip(temp);
            tip.setId(i+dragTips.length);
            result.add(tip);
        }
        return result;
    }
}
