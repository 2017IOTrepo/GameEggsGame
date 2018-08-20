package com.example.a41448.huawu.view.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.service.media.MediaBrowserService;
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


import com.cocosw.bottomsheet.BottomSheet;
import com.example.a41448.huawu.Communication.utils.PathUtils;

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
import com.example.a41448.huawu.utils.FileUtils;
import com.example.a41448.huawu.utils.FragmentUtils;
import com.google.zxing.activity.CaptureActivity;

import com.example.a41448.huawu.utils.NetUtil;
import com.example.a41448.huawu.utils.PlayersUtils;
import com.example.a41448.huawu.view.sideslip.Achievement;
import com.example.a41448.huawu.view.sideslip.Shop.Shop_Main;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.security.acl.Group;
import java.util.List;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.http.HEAD;
import retrofit2.http.Url;

import static android.net.sip.SipErrorCode.SERVER_ERROR;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener ,
        Toolbar.OnMenuItemClickListener,IOnSearchClickListener,View.OnClickListener{

    private final static int CAMERA_REQUEST = 2;
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

    private FragmentManager mFragmentManager;

    //照相调用变量
    private Intent openCameraIntent;
    private String camPicPath;
    private Uri uri;
    private ContentValues contentValues;

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
    private TextView sex;
    private CircleImageView avatar;
    private View headerLayout;
    //头像文件
    private File avatarFile;

    //定义滑动菜单的实例
    private DrawerLayout mDrawerLayout;

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
//          FragmentUtils.replaceFragment(getSupportFragmentManager(), new QuestionFragment(), R.id.fragment_question);
        }
        //文字提取初始化
        initAccessToken();
    }

    /*
    *
    * 侧边栏的布局逻辑
    * */
    private void setView() {
        headerLayout = navigationView.getHeaderView(0);
        points = (TextView)headerLayout.findViewById(R.id.player_point_text);
        coins = (TextView)headerLayout.findViewById(R.id.player_coins_text);
        name = (TextView)headerLayout.findViewById(R.id.player_name_text);
        labels = (TextView)headerLayout.findViewById(R.id.player_label_text);
        sex = (TextView)headerLayout.findViewById(R.id.player_sex_text);
        avatar = (CircleImageView)headerLayout.findViewById(R.id.player_avatar_image);
        final Handler handler = new Handler();

        points.setText("游戏点数：" + players.getPoints());
        coins.setText("金币数：" + players.getCoins());
        labels.setText("用户标签：" + players.getLables().toString());
        name.setText("用户名：" + players.getUserAccontId());

        sex.setText(PlayersUtils.setSex(players.isSex()));
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheet.Builder(MainActivity.this)
                        .title("选择性别")
                        .sheet(R.menu.sex_chosing_menu)
                        .listener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case R.id.male_chose:
                                        players.setSex(true);
                                        break;

                                    case R.id.female_chose:
                                        players.setSex(false);
                                        break;
                                }
                                players.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            Toast.makeText(mContext, "更改成功", Toast.LENGTH_SHORT).show();
                                            sex.setText(PlayersUtils.setSex(players.isSex()));
                                        }else {
                                            Toast.makeText(mContext, "更改失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .show();
            }
        });

        if (players.getAvatar() != null) {
        }



        avatar.setOnClickListener(new View.OnClickListener() {//头像选择
            @Override
            public void onClick(View view) {
                new BottomSheet.Builder(MainActivity.this)
                        .title("请选择头像来源")
                        .sheet(R.menu.select_pic_menu)
                        .listener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case R.id.camera_chose:
                                        openCameraIntent = new Intent(
                                                MediaStore.ACTION_IMAGE_CAPTURE);
                                        camPicPath = PathUtils.getSavePicPath(mContext);
                                        if (Build.VERSION.SDK_INT < 24){//根据安卓版本适配
                                            uri = Uri.fromFile(new File(camPicPath));
                                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                            startActivityForResult(openCameraIntent, CAMERA_REQUEST);
                                        }else{//适配7.0
                                            contentValues = new ContentValues(1);
                                            contentValues.put(MediaStore.Images.Media.DATA, camPicPath);
                                            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                    contentValues);
                                            grantUriPermission( "com.example.lab.android.nuc.chat",uri,Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                                            openCameraIntent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
                                            openCameraIntent.addFlags( Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                            openCameraIntent.putExtra( MediaStore.EXTRA_OUTPUT,uri);
                                            startActivityForResult( openCameraIntent,CAMERA_REQUEST);
                                        }
                                        break;

                                    case R.id.gallery_chose:
                                        PictureSelector
                                                .create(MainActivity.this)
                                                .openGallery(PictureMimeType.ofImage())
                                                .imageSpanCount(4)
                                                .selectionMode(PictureConfig.SINGLE)
                                                .previewImage(true)
                                                .compress(false)
                                                .isCamera(false)
                                                .forResult(PictureConfig.CHOOSE_REQUEST);
                                        break;

                                    case R.id.cancel_chose:
                                        startActivity(NetUtil.shareNet());
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
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
                Snackbar.make(mTabLayout, "再按一次返回键退出", Snackbar.LENGTH_SHORT).show();
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

    /*
    *
    * 侧滑栏的选项选择逻辑
    * */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.player_achievement:
                Achievement.startActivity(mContext);
                break;

            case R.id.player_shop:
                Shop_Main.startActivity(mContext);
                break;

            case R.id.player_help:
                break;
            case R.id.app_about:
                break;
        }
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
     * 初始化视图
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(){
        //找到各自的实例
        mContext = MainActivity.this;
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

                    }else {
                        //Toast.makeText(MainActivity.this, "错误：" + e.toString(), Toast.LENGTH_SHORT).show();
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

    /*
    * 通过implement OnClickListener接口获得的点击事件方法
    * 暂时没有用处
    * */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
        }
    }

    @Override
    public void OnSearchClick(String keyword) {
    }

    /*
    *
    * 回调接口覆写
    * */
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

            case PictureConfig.CHOOSE_REQUEST:
                // 图片选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                LocalMedia media = selectList.get(0);
                String path = media.getPath();
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
//                    mDesignCenterView.refreshSelectedPicture(selectList);
                //resizeImage(Uri.parse(path));
                updateAvatar(path);
                break;

            //调用摄像头的回调
            case CAMERA_REQUEST:
                FileInputStream is = null;
                try {
                    is = new FileInputStream(camPicPath);
                    File camFile = new File(camPicPath); // 图片文件路径
                    if (camFile.exists()) {
                        //resizeImage(Uri.parse(camPicPath));
                        //检测是否有足够内存存储 头大懒得写了
//                        int size = ImageCheckoutUtil
//                                .getImageSize(ImageCheckoutUtil
//                                        .getLoacalBitmap(camPicPath));
                        updateAvatar(camPicPath);
                    } else {
                        Toast.makeText(this, "该文件不存在", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    // 关闭流
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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

    private void updateAvatar(final String path) {
        players.setAvatar(new BmobFile(players.getUserAccontId(), null, new File(path).toString()));
        players.getAvatar().upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
        players.update(new UpdateListener() {
                           @Override
                           public void done(BmobException e) {
                               if (e == null) {
                                   avatar.setImageURI( Uri.parse( path ) );
                                   //Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                               } else {
                                   Toast.makeText( mContext, "头像上传到服务器", Toast.LENGTH_SHORT ).show();
                               }
                           }
                       });
    }
    /**
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
