package com.example.a41448.huawu.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a41448.huawu.view.fragment.CommunityFragment;
import com.example.a41448.huawu.view.fragment.GameFragment;
import com.example.a41448.huawu.view.fragment.MapFragment;
import com.example.a41448.huawu.view.fragment.MessageFragment;
import com.example.a41448.huawu.view.fragment.QuestionFragment;
import com.example.a41448.huawu.R;


public class PageAdapter extends FragmentPagerAdapter {

    //定义标签的数量
    public final int COUNT  = 3;

    private String [] titles = {"地图","社群","游戏"};


    private int[] tabimgs = new int[]{
            R.drawable.message_selector,R.drawable.contact_selector,
            R.drawable.question_selector
    };

    public Context mContext;

    public PageAdapter(FragmentManager fm, Context context){
        super(fm);
        this.mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            //返回一个Fragment
            return MapFragment.newInstance();
        else if (position == 1)
//            return ContactFragment.newInstance(position + 1);
            return CommunityFragment.newInstance( position + 1 );
        else if (position == 2)
            return GameFragment.newInstance();
        else
            return null;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public View getTabView(int position){

        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_page,null);
        TextView textView = (TextView) v.findViewById(R.id.news_title);
        ImageView tabImageView = (ImageView) v.findViewById(R.id.news_image_view);
        tabImageView.setImageResource( tabimgs[position] );
        textView.setText(titles[position]);
        return v;
    }
}
