package com.example.a41448.huawu.view.sideslip.Shop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.bean.Players;
import com.example.a41448.huawu.view.activity.MainActivity;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class Shop_Main  extends AppCompatActivity{

    private ViewPager mViewPager;
    private Players players;

    private CardPagerAdapter mCardAdapter;
    private Shadowtransformer mCardShadowtransformer;
    private Context context;
    private ViewGroup container;
    private Button button;
    private AmountView mAmountView;
    private CardItem cardItem;
    private int need_coin;

    private static TextView tipTextView;
    private static Dialog ProgressDialog;
    private  static  CustomStatusView customStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_main);

        players = BmobUser.getCurrentUser(Players.class);
        final ArrayList<String> text = new ArrayList<>();
        text.add("提示卡");
        text.add("藏点图");
        text.add("成就碎片1");
        text.add("成就碎片2");
        final ArrayList<CardItem> cardItemArrayList = new ArrayList<>();
        cardItemArrayList.add(new CardItem(R.string.title_1, R.string.text_1,R.string.coin_1,R.drawable.goods1));
        cardItemArrayList.add(new CardItem(R.string.title_2, R.string.text_2,R.string.coin_2,R.drawable.goods2));
        cardItemArrayList.add(new CardItem(R.string.title_3, R.string.text_3,R.string.coin_3,R.drawable.goods3));
        cardItemArrayList.add(new CardItem(R.string.title_4, R.string.text_4,R.string.coin_4,R.drawable.goods4));

        button = (Button) findViewById(R.id.bt_buy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardItem = cardItemArrayList.get(mViewPager.getCurrentItem());
                need_coin = AmountView.amount * cardItem.getmText_coin();
                showCompleteDialog(Shop_Main.this,
                        "您正在购买"+AmountView.amount+"个"+text.get(mViewPager.getCurrentItem()),
                        players, need_coin);
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mCardAdapter = new CardPagerAdapter();
        for (CardItem carditems :
                cardItemArrayList) {
            mCardAdapter.addCardItem(carditems);
        }

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

    void showCompleteDialog(final Context context, String msg, final Players players, final int need_coin){

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.shop_dialog_toast, null);// 得到加载view
        final int default_icon = players.getCoins();
        final int in_coin = default_icon - need_coin;
        final boolean can_buy = (in_coin >= 0 ? true : false);

        tipTextView = v.findViewById(R.id.tv_toast_content);// 提示文字
        tipTextView.setText(msg);// 设置加载信息
        customStatusView = v.findViewById(R.id.as_status);
        customStatusView.loadLoading();
        ProgressDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        ProgressDialog.setCancelable(true); // 是否可以按“返回键”消失
        ProgressDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        ProgressDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        /**
         *将显示Dialog的方法封装在这里面
         */
        final Window window = ProgressDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width =1000;
        lp.height =800;
        lp.y=-150;
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        ProgressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (can_buy) {
                    players.setCoins(in_coin);
                    players.update(players.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                tipTextView.setText("购买成功");
                            }else {
                                tipTextView.setText("服务器连接失败\n未能完成购买操作");
                                players.setCoins(default_icon);
                            }
                        }
                    });
                    customStatusView.loadSuccess();
                }
                else
                    tipTextView.setText("金币不足\n未能完成购买操作");
                    customStatusView.loadFailure();
            }
        },1000);
        //这里用到了handler的定时器效果 延迟2秒执行dismiss();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressDialog.dismiss();
            }
        }, 3000);
    }

    //其他activity跳转到ShopActivity的函数
    public static void startActivity(Context context){
        Intent intent = new Intent(context, Shop_Main.class);
        context.startActivity(intent);
    }

}
