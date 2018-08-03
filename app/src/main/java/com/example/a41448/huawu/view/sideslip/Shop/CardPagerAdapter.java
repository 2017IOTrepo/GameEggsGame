package com.example.a41448.huawu.view.sideslip.Shop;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a41448.huawu.R;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter{

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
                .inflate(R.layout.shop_adapter, container, false);
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
         titleTextView = (TextView) view.findViewById(R.id.titleTextView);
         contentTextView = (TextView) view.findViewById(R.id.contentTextView);
         imageView = view.findViewById(R.id.iv_goods);
         textView_coin = view.findViewById(R.id.tv_coin);

        imageView.setImageResource(item.getmImageView());
        textView_coin.setText(item.getmText_coin());
        titleTextView.setText(item.getTitle());
        contentTextView.setText(item.getText());
    }

}