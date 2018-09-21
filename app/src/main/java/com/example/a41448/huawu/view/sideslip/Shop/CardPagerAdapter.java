package com.example.a41448.huawu.view.sideslip.Shop;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a41448.huawu.R;
import com.netease.nrtc.video.gl.EglBase;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter{

    private Context mContext;
    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;

    TextView titleTextView;
    TextView contentTextView;
    ImageView imageView;
    TextView textView_coin;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }
    public CardPagerAdapter(Context context)  {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        mContext = context;
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.daily_sign_item, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    public  void bind(CardItem item, View view) {
         titleTextView = (TextView) view.findViewById(R.id.daily_title);
         contentTextView = (TextView) view.findViewById(R.id.daily_text);
         imageView = view.findViewById(R.id.daily_image);
         Glide.with(mContext).load( item.getImageUri() ).into( imageView );
         titleTextView.setText(item.getTitle());
         contentTextView.setText(item.getText());
    }
}