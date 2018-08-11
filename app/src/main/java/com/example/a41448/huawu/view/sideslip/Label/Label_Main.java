package com.example.a41448.huawu.view.sideslip.Label;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.view.sideslip.Label.bean.SimpleTitleTip;
import com.example.a41448.huawu.view.sideslip.Label.bean.Tip;
import com.example.a41448.huawu.view.sideslip.Label.widget.TipItemView;

import java.util.ArrayList;

public class Label_Main   extends AppCompatActivity{
    private EasyTipDragView easyTipDragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        easyTipDragView = findViewById(R.id.easy_tip_drag_view);
        //设置已经包含的标签数据
        easyTipDragView.setAddData(TipDataModel.getAddTips());
        //设置可以添加的标签数据
        easyTipDragView.setDragData(TipDataModel.getDragTips());
        //在easyTipDragView处于非编辑模式下点积item的毁掉（编辑模式下点积item作为删除的item）
        easyTipDragView.setSelectedListener(new TipItemView.OnSelectedListener() {
            @Override
            public void onTileSelected(Tip entity, int position, View view) {
                toast(((SimpleTitleTip) entity).getTip());
            }
        });
        //设置每次数据改变之后的回调（每次标签删除和添加都会回调）
        easyTipDragView.setDataResultCallback(new EasyTipDragView.OnDataChangeResultCallback() {
            @Override
            public void onDataChangeResult(ArrayList<Tip> tips) {
                Log.i("skr", tips.toString());
            }
        });
        //设置点击“确定”按钮最终数据的回调
        easyTipDragView.setOnCompleteCallback(new EasyTipDragView.OnCompleteCallback() {
            @Override
            public void onComplete(ArrayList<Tip> tips) {
                //     toast("最终数据：" + tips.toString());
                //  for (int i = 0 ; i <tips.size();i++){
                //      TipDataModel.dragTips[i] = String.valueOf(tips.get(i));
                //  }

            }
        });
        easyTipDragView.open();
    }

    public void toast(String str){
        Toast.makeText(Label_Main.this, str, Toast.LENGTH_SHORT).show();
    }
}
