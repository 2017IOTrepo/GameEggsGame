package com.example.a41448.huawu.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a41448.huawu.Manifest;
import com.example.a41448.huawu.adapter.PageAdapter;
import com.example.a41448.huawu.utils.FragmentUtils;
import com.example.a41448.huawu.view.fragment.QuestionFragment;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.utils.PermissionUtil;
import com.example.a41448.huawu.base.BaseActivity;
import com.example.a41448.huawu.bean.Players;
import com.example.a41448.huawu.base.RoundImageView.RoundImageView;
import com.example.a41448.huawu.utils.ActivityCollector;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener ,
        Toolbar.OnMenuItemClickListener,IOnSearchClickListener,View.OnClickListener{

    private long customTime = 0;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private Players players;

    //添加得实例
    public static Context mContext;
    Toolbar mToolbar;
    TabLayout mTabLayout;

    ViewPager mViewPager;

    private SearchFragment searchFragment;

    RoundImageView icon;


    //拍照的Uri
    private Uri imageUri;
    //定义滑动菜单的实例
    private DrawerLayout mDrawerLayout;

    private static final int TAKE_PHOTO = 1;

    private FloatingActionButton addQuestionBtn;


    private RelativeLayout rootView;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //添加的
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

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            //添加返回功能
            actionBar.setDisplayHomeAsUpEnabled(true);
            //默认的是返回键，更改为菜单键
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        addQuestionBtn = (FloatingActionButton) findViewById( R.id.fab_add_question );

        mToolbar.setOnMenuItemClickListener(this);
        searchFragment.setOnSearchClickListener(this);

        int id = getIntent().getIntExtra( "id",0 );
        if (id == 1){
            FragmentUtils.replaceFragment(getSupportFragmentManager(), new QuestionFragment(), R.id.fragment_question);
        }
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
        switch (item.getItemId()){
            case R.id.action_settings:

                break;
            default:
                break;

        }
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


    //在此基础上添加的

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if (grantResults.length > 0){
                    //循环遍历
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有权限才能使用该功能", Toast.LENGTH_SHORT).show();

                            return;
                        }
                    }
                    Toast.makeText( this, "权限申请成功", Toast.LENGTH_SHORT ).show();
                }else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
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
                    QuestionFragment questionFragment = new QuestionFragment();

                    mFragmentManager = this.getSupportFragmentManager();
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    //创建Bundle来封装传递给fragment
                    Bundle bundle = new Bundle( );
                    bundle.putString("link",question_link);
                    bundle.putString("title",question_title  );
                    bundle.putString( "detail",question_detail );
                    //设置传递的对象
                    questionFragment.setArguments( bundle );
                    ft.add( R.id.fragment_question,questionFragment,"questionFragment" );
                    ft.commit();

                }
                break;
            default:
        }
    }

}
