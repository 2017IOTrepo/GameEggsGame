package com.example.a41448.huawu.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.tools.views.MatrixView;

import org.angmarch.views.NiceSpinner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class gamehallActivity extends AppCompatActivity {

    NiceSpinner niceSpinner_1,niceSpinner_2;

    private ListView listview;
    private int[] images = new int[]{R.drawable.picture_1, R.drawable.picture_2, R.drawable.picture_3,
    R.drawable.picture_4,R.drawable.picture_5,R.drawable.picture_6};
    private String[] room_list = new String[]{"文瀛5241", "啦啦啦", "桥东五块", "实践部晚会", "新生指导"};


    List<String> my_item = new LinkedList<>( Arrays.asList("中北大学", "大一", "安卓", "文瀛五", "河北"));
    List<String> my_target = new LinkedList<>( Arrays.asList("程序员", "学生", "中北大学", "大数据", "太原"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_gamehall );
        niceSpinner_1 = (NiceSpinner) findViewById(R.id.nice_spinner_1);
        niceSpinner_1.attachDataSource(my_item);
        niceSpinner_2 = (NiceSpinner) findViewById( R.id.nice_spinner_2 );
        niceSpinner_2.attachDataSource( my_target );
        listview = (ListView) findViewById(R.id.lv);
        listview.setAdapter(new MyAdapter());
        listview.setClipToPadding(false);
        listview.setClipChildren(false);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                for (int i = 0; i < listview.getChildCount(); i++) {
                    listview.getChildAt(i).invalidate();
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 9999;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                MatrixView m = (MatrixView) LayoutInflater.from(gamehallActivity.this).inflate(R.layout.play_room_item, null);
                m.setParentHeight(listview.getHeight());
                convertView = m;
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            imageView.setImageResource(images[position % images.length]);
            TextView textView = (TextView) convertView.findViewById( R.id.text );
            textView.setText( room_list[position % room_list.length] );
            return convertView;
        }

    }

    @Deprecated
    public void changeGroupFlag(Object obj) throws Exception {
        Field[] f = obj.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredFields(); // 获得成员映射数组
        for (Field tem : f) {
            if (tem.getName().equals("mGroupFlags")) {
                tem.setAccessible(true);
                Integer mGroupFlags = (Integer) tem.get(obj);
                int newGroupFlags = mGroupFlags & 0xfffff8;
                tem.set(obj, newGroupFlags);
            }
        }
    }
}
