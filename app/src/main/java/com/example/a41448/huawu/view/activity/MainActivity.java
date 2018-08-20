package com.example.a41448.huawu.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.example.a41448.huawu.adapter.PageAdapter;

import com.example.a41448.huawu.R;
import com.example.a41448.huawu.adapter.PageAdapter;
import com.example.a41448.huawu.base.BaseActivity;
import com.example.a41448.huawu.bean.Players;
import com.example.a41448.huawu.utils.ActivityCollector;
import com.example.a41448.huawu.utils.FragmentUtils;
import com.google.zxing.activity.CaptureActivity;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.io.File;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.http.HEAD;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener ,
        Toolbar.OnMenuItemClickListener,IOnSearchClickListener,View.OnClickListener{

    public static File NEWFILE;
    private AlertDialog.Builder alertDialog;
    private boolean hasGotToken = false;
    private long customTime = 0;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private Players players;
    private final static int REQ_CODE = 1028;
    //添加得实例
    public static Context mContext;
    Toolbar mToolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    private SearchFragment searchFragment;
    private TextView points;
    private TextView coins;
    private TextView name;
    private TextView labels;
    private CircleImageView avatar;
    private View headerLayout;

    //拍照的Uri
    private Uri imageUri;
    //定义滑动菜单的实例
    private DrawerLayout mDrawerLayout;

    private static final int TAKE_PHOTO = 1;



    private RelativeLayout rootView;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实现透明状态栏效果  并且toolbar 需要设置  android:fitsSystemWindows="true"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
        }
        setContentView(R.layout.activity_main);

        //初始化控件
        initView();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened( drawerView );
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed( drawerView );
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //初始化人物界面
        setView();

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            //添加返回功能
            actionBar.setDisplayHomeAsUpEnabled(true);
            //默认的是返回键，更改为菜单键
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        mToolbar.setOnMenuItemClickListener(this);
        searchFragment.setOnSearchClickListener(this);

        int id = getIntent().getIntExtra( "id",0 );
        if (id == 1){

            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack( null )
                    .commit();

//            FragmentUtils.replaceFragment(getSupportFragmentManager(), new QuestionFragment(), R.id.fragment_question);

        }
        //文字提取初始化
        initAccessToken();
    }

    private void setView() {
        headerLayout = navigationView.getHeaderView(0);
        points = (TextView)headerLayout.findViewById(R.id.player_point_text);
        coins = (TextView)headerLayout.findViewById(R.id.player_coins_text);
        name = (TextView)headerLayout.findViewById(R.id.player_name_text);
        labels = (TextView)headerLayout.findViewById(R.id.player_label_text);
        avatar = (CircleImageView)headerLayout.findViewById(R.id.player_avatar_image);

        points.setText("游戏点数：" + players.getPoints());
        coins.setText("金币数：" + players.getCoins());
        labels.setText("用户标签：" + players.getLables().toString());
        name.setText("用户名：" + players.getUserAccontId());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //软件退出逻辑
            if (Math.abs(customTime - System.currentTimeMillis()) < 2000) {
                ActivityCollector.finishiAll();
            } else {// 提示用户退出
                customTime = System.currentTimeMillis();
                Snackbar.make(mToolbar, "再按一次返回键退出", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (toggle.onOptionsItemSelected( item )){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String mString = null;
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.player_achievement:
                break;

            case R.id.player_shop:
                break;

            case R.id.player_help:
                break;

            case R.id.app_about:
                break;
        }
        mToolbar.setTitle(mString);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //其他activity跳转到MainActivity的函数
    public static void startActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 创建视图
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(){
        //找到各自的实例
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        searchFragment = SearchFragment.newInstance();
        players = BmobUser.getCurrentUser(Players.class);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(),this);
        mTabLayout.setTabMode( TabLayout.MODE_FIXED );
        ///为viewPager设置adapter
        mViewPager.setAdapter(adapter);

        //将viewPager添加进leLayout当中
        mTabLayout.setupWithViewPager(mViewPager);

        for (int i= 0;i < mTabLayout.getTabCount();i ++){
            TabLayout.Tab tab = mTabLayout.getTabAt( i );
            //为每个tab设置自定义视图，获取自定视图的方法写在Adapter里面
            //同样也可以直接写在Activity里面
            tab.setCustomView(adapter.getTabView( i ));
        }
        //设置toolbar标题文本
        mToolbar.setTitle("首页");
        //设置toolbar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //设置左上角图标是否可点击
            actionBar.setHomeButtonEnabled(true);
            //左上角加上一个返回图标
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(players.getObjectId())){
            BmobIM.connect(players.getObjectId(), new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        Toast.makeText(MainActivity.this, "conect successful", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "错误：" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search: //点击搜索
                searchFragment.show(getSupportFragmentManager(),SearchFragment.TAG);
                break;
            case R.id.scan:
                Intent intent = new Intent( MainActivity.this, CaptureActivity.class );
                startActivityForResult(intent,REQ_CODE);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }

    @Override
    public void OnSearchClick(String keyword) {

    }

    private FragmentManager mFragmentManager;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (requestCode == RESULT_OK){
                    String question_link = data.getStringExtra( "question_link" );
                    String question_title = data.getStringExtra("question_title");
                    String question_detail = data.getStringExtra( "question_detail" );

                    mFragmentManager = this.getSupportFragmentManager();
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    //创建Bundle来封装传递给fragment
                    Bundle bundle = new Bundle( );
                    bundle.putString("link",question_link);
                    bundle.putString("title",question_title  );
                    bundle.putString( "detail",question_detail );
                    //设置传递的对象

                }
                break;
            case REQ_CODE:
                if (CaptureActivity.SCAN_QRCODE_RESULT != "qrcode_result"){
                    String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
                    Toast.makeText( this, result, Toast.LENGTH_LONG ).show();
                }
                if (CaptureActivity.SCAN_QRCODE_BITMAP != "qrcode_bitmap") {
                    Bitmap bitmap = data.getParcelableExtra( CaptureActivity.SCAN_QRCODE_BITMAP );
                }
                break;
            default:
        }
    }

    /**、
     * 
     * 以license文件方式初始化
     */
    private void initAccessToken() {
        OCR.getInstance(this).initAccessToken( new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getApplicationContext());
    }

    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

}
