package com.example.a41448.huawu.view.sideslip.Shop;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.a41448.huawu.R;

import java.util.ArrayList;

public class Shop_Main  extends AppCompatActivity{

    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private Shadowtransformer mCardShadowtransformer;
    private Context context;
    private ViewGroup container;
    private Button button;
    private AmountView mAmountView;
    private int position1 = 1;
    private  CardItem cardItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> text = new ArrayList<>();
        text.add("提示卡");
        text.add("藏点图");
        text.add("成就碎片1");
        text.add("成就碎片2");
        button = findViewById(R.id.bt_buy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  for(int i = 0 ; i < text.size();i++){
                DialogUtils.showCompleteDialog(Shop_Main.this,"您成功地购买了"+AmountView.amount+"个"+text.get(mViewPager.getCurrentItem()));
                //  }
                //  DialogUtils.showCompleteDialog(MainActivity.this,"您成功地购买了此商品" );

                final DialogUtils dialogUtils = new DialogUtils();
                final DialogUtils.AutoDismissDialog autoDismissDialog = dialogUtils.new AutoDismissDialog(Shop_Main.this, R.style.MyDialogStyle);
                autoDismissDialog.onStart();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(R.string.title_1, R.string.text_1,R.string.coin_1,R.drawable.goods1));
        mCardAdapter.addCardItem(new CardItem(R.string.title_2, R.string.text_2,R.string.coin_2,R.drawable.goods2));
        mCardAdapter.addCardItem(new CardItem(R.string.title_3, R.string.text_3,R.string.coin_3,R.drawable.goods3));
        mCardAdapter.addCardItem(new CardItem(R.string.title_4, R.string.text_4,R.string.coin_4,R.drawable.goods4));

        mCardShadowtransformer = new Shadowtransformer(mViewPager, mCardAdapter);
        mCardShadowtransformer.enableScaling(true);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowtransformer);
        mViewPager.setOffscreenPageLimit(3);


        mAmountView = findViewById(R.id.amount_view);
        mAmountView.setGoods_storage(100);

        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                Toast.makeText(getApplicationContext(), "Amount->"+amount, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
