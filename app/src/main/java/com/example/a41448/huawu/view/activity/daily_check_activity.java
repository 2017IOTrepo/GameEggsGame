package com.example.a41448.huawu.view.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.view.sideslip.Shop.CardItem;
import com.example.a41448.huawu.view.sideslip.Shop.CardPagerAdapter;
import com.example.a41448.huawu.view.sideslip.Shop.Shadowtransformer;

public class daily_check_activity extends AppCompatActivity implements View.OnClickListener ,
        CompoundButton.OnCheckedChangeListener{

    private Button mButton;
    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private Shadowtransformer mCardShadowTransformer;
    private Shadowtransformer mFragmentCardShadowTransformer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_daily_check_activity );
        mViewPager = (ViewPager) findViewById( R.id.viewPager );
        mButton = (Button) findViewById( R.id.card_Btn );
        ((CheckBox) findViewById( R.id.check_box )).setOnClickListener( this );
        mButton.setOnClickListener( this );
        mCardAdapter = new CardPagerAdapter(this);
        mCardAdapter.addCardItem( new CardItem( R.string.title_1,R.string.text1,
                "http://pd35yssng.bkt.clouddn.com/1.jpg") );
        mCardAdapter.addCardItem( new CardItem( R.string.title_1,R.string.text2,
                "http://pd35yssng.bkt.clouddn.com/2.jpg") );
        mCardAdapter.addCardItem( new CardItem( R.string.title_1,R.string.text3,
                "http://pd35yssng.bkt.clouddn.com/3.jpg") );
        mCardAdapter.addCardItem( new CardItem( R.string.title_1,R.string.text4,
                "http://pd35yssng.bkt.clouddn.com/4.jpg") );
        mCardAdapter.addCardItem( new CardItem( R.string.title_1,R.string.text5,
                "http://pd35yssng.bkt.clouddn.com/5.jpg") );
        mCardAdapter.addCardItem( new CardItem( R.string.title_1,R.string.text6,
                "http://pd35yssng.bkt.clouddn.com/6.jpg") );
        mCardShadowTransformer = new Shadowtransformer(mViewPager, mCardAdapter);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onClick(View v) {

    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCardShadowTransformer.enableScaling(isChecked);
        mFragmentCardShadowTransformer.enableScaling(isChecked);
    }
}
