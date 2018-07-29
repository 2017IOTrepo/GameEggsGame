package com.example.a41448.huawu.view.sideslip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class Achievement extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Achievements_Adapter achievements_adapter;
    private List<DataBean>lists;
    private ImageView  iv_ach;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievement_layout_1);
        initData();
        recyclerView = findViewById(R.id.recycler_view_achievements);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        achievements_adapter = new Achievements_Adapter(Achievement.this,lists);
        //设置分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider2(this,layoutManager.getOrientation()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(achievements_adapter);


       achievements_adapter.setOnItemClickListener(new Achievements_Adapter.OnRecyclerViewItemClickListener() {
           @Override
           public void onClick(View view, int position) {

            Achievements_Adapter.ItemHolder  viewHolder = (Achievements_Adapter.ItemHolder)recyclerView.findViewHolderForLayoutPosition(position);
               iv_ach = viewHolder.imageView;

               switch (view.getId()){
                   case R.id.iv_achievements:
                       Anima_Dialog.getIntent().show(getSupportFragmentManager(),"");
                         break;
               }
           }
       });
    }

    public class DataBean {
        String textView;
        int imageView;

              DataBean(String textView,int imageView){
                  this.textView = textView;
                  this.imageView = imageView;
              }

              public String getText(){
                  String text = textView;
                  return text;
              }

              public int getImageView() {
                  int id = imageView;
                  return id;
              }

    }
    private  void initData(){
        lists = new ArrayList<>();
        lists.add(new DataBean("运动小白",R.drawable.class1));
        lists.add(new DataBean("运动渴望",R.drawable.class2));
        lists.add(new DataBean("运动热身",R.drawable.class3));
        lists.add(new DataBean("运动激情",R.drawable.class4));
        lists.add(new DataBean("运动快感",R.drawable.class5));
        lists.add(new DataBean("运动欲望",R.drawable.class6));
        lists.add(new DataBean("运动小将",R.drawable.class7));
        lists.add(new DataBean("运动达人",R.drawable.class8));
        lists.add(new DataBean("运动神人",R.drawable.class9));
        lists.add(new DataBean("运动霸主",R.drawable.class10));
    }

    //其他activity跳转到AchievementActivity的函数
    public static void startActivity(Context context){
        Intent intent = new Intent(context, Achievement.class);
        context.startActivity(intent);
    }
}
