package com.example.a41448.huawu.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

public class EmoViewPagerAdapter  extends PagerAdapter {

    private List<View> mViewList;

    public EmoViewPagerAdapter(List<View> viewList){
        this.mViewList = viewList;
    }


    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull View container, int position) {
        ((ViewPager) container).addView( mViewList.get( position ) );
        return mViewList.get(position );
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView( mViewList.get( position ) );
    }
}
