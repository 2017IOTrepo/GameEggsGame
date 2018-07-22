package com.example.a41448.huawu.view.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.a41448.huawu.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.b.V;

public class TrackActivity extends AppCompatActivity implements SensorEventListener {


    /*定位相关*/
    LocationClient mLocationClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;

    MapView mMapView;
    BaiduMap mBaiduMap;

    private TextView info;
    private RelativeLayout progressBarRl;


    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    float mCurrentZoom = 18f;//默认地图缩放比例值

    private SensorManager mSensorManager;

    //起点图标
    BitmapDescriptor startBD = BitmapDescriptorFactory.fromResource(R.drawable.ic_me_history_startpoint);
    //终点图标
    BitmapDescriptor finishBD = BitmapDescriptorFactory.fromResource(R.drawable.ic_me_history_finishpoint);

    List<LatLng> points = new ArrayList<LatLng>();//位置点集合
    Polyline mPolyline;//运动轨迹图层
    LatLng last = new LatLng(0, 0);//上一个定位点
    MapStatus.Builder builder;


    /*与悬浮按钮相关*/
    private FloatingActionsMenu mFloatingActionsMenu;
    private FloatingActionButton mAction_d;
    private FloatingActionButton mAction_e;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_track );


        initView();
        Toolbar toolbar1 = (Toolbar) findViewById( R.id.action_toolbar );
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById( R.id.collapsing_toolbar_layout );
        setSupportActionBar( toolbar1 );
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled( true );
        }
        mSensorManager = (SensorManager) getSystemService( SENSOR_SERVICE ); //获取传感器管理服务

        //地图初始化
        mMapView = (MapView) findViewById( R.id.bmapView );
        mBaiduMap = mMapView.getMap();
        //开始定位图层
        mBaiduMap.setMyLocationEnabled( true );
        mBaiduMap.setMyLocationConfiguration( new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING,true,null
        ) );

        /**
         * 添加地图缩放状态变化监听，当手动放大或缩小地图时，拿到缩放后的比例，然后获取到下次定位，
         *  给地图重新设置缩放比例，否则地图会重新回到默认的mCurrentZoom缩放比例
         */
        mBaiduMap.setOnMapStatusChangeListener( new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mCurrentZoom = mapStatus.zoom;
            }
        } );

        //定位初始化
        mLocationClient = new LocationClient( this );
        mLocationClient.registerLocationListener( myListener );
        LocationClientOption option = new LocationClientOption( );
        option.setLocationMode( LocationClientOption.LocationMode.Device_Sensors );  //只用gps定位，需要在室外定位
        option.setOpenGps( true ); //打开GPS
        option.setCoorType( "bd09ll" ); //设置坐标类型
        option.setScanSpan( 1000 );
        //设置进去
        mLocationClient.setLocOption( option );
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(){
        info = (TextView) findViewById( R.id.info );
        progressBarRl = (RelativeLayout) findViewById( R.id.progressBarRl );
        initFloatButton();
    }

    double lastX;

    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[SensorManager.DATA_X];

        if (Math.abs( x - lastX) > 1.0){
            mCurrentDirection = (int) x;

            if (isFirstLoc){
                lastX = x;
                return;
            }

            locData = new MyLocationData.Builder().accuracy( 0 )
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction( mCurrentDirection ).latitude( mCurrentLat ).longitude( mCurrentLon ).build();
            mBaiduMap.setMyLocationData( locData );
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {

            if (location == null || mMapView == null) {
                return;
            }

            //注意这里只接受gps点，需要在室外定位。
            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                info.setText("GPS信号弱，请稍后...");

                if (isFirstLoc) {//首次定位
                    //第一个点很重要，决定了轨迹的效果，gps刚开始返回的一些点精度不高，尽量选一个精度相对较高的起始点
                    LatLng ll = null;

                    ll = getMostAccuracyLocation(location);
                    if(ll == null){
                        return;
                    }
                    isFirstLoc = false;
                    points.add(ll);//加入集合
                    last = ll;

                    //显示当前定位点，缩放地图
                    locateAndZoom(location, ll);

                    //标记起点图层位置
                    MarkerOptions oStart = new MarkerOptions();// 地图标记覆盖物参数配置类
                    oStart.position(points.get(0));// 覆盖物位置点，第一个点为起点
                    oStart.icon(startBD);// 设置覆盖物图片
                    mBaiduMap.addOverlay(oStart); // 在地图上添加此图层

                    progressBarRl.setVisibility(View.GONE);

                    return;//画轨迹最少得2个点，首地定位到这里就可以返回了
                }

                //从第二个点开始
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                //sdk回调gps位置的频率是1秒1个，位置点太近动态画在图上不是很明显，可以设置点之间距离大于为5米才添加到集合中
                if (DistanceUtil.getDistance(last, ll) < 5) {
                    return;
                }

                points.add(ll);//如果要运动完成后画整个轨迹，位置点都在这个集合中

                last = ll;

                //显示当前定位点，缩放地图
                locateAndZoom(location, ll);

                //清除上一次轨迹，避免重叠绘画
                mMapView.getMap().clear();

                //起始点图层也会被清除，重新绘画
                MarkerOptions oStart = new MarkerOptions();
                oStart.position(points.get(0));
                oStart.icon(startBD);
                mBaiduMap.addOverlay(oStart);

                //将points集合中的点绘制轨迹线条图层，显示在地图上
                /**
                 * 配置线段图层参数类： PolylineOptions
                 * ooPolyline.width(13)：线宽
                 * ooPolyline.color(0xAAFF0000)：线条颜色红色
                 * ooPolyline.points(latLngs)：List<LatLng> latLngs位置点，将相邻点与点连成线就成了轨迹了
                 */
                OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(points);
                mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
            }
        }

    }

    private void locateAndZoom(final BDLocation location,LatLng ll){
        mCurrentLat = location.getLatitude();
        mCurrentLon = location.getLongitude();
        locData = new MyLocationData.Builder().accuracy( 0 )
                .direction( mCurrentDirection ).latitude( location.getLatitude() )
                .longitude( mCurrentDirection ).longitude( location.getLongitude() ).build();
        mBaiduMap.setMyLocationData( locData );

        builder = new MapStatus.Builder( );
        builder.target( ll ).zoom( mCurrentZoom );
        mBaiduMap.animateMapStatus( MapStatusUpdateFactory.newMapStatus( builder.build() ));
    }


    /**
     * 首次定位很重要，选一个精度相对较高的起始点
     * 注意：如果一直显示gps信号弱，说明过滤的标准过高了，
     你可以将location.getRadius()>25中的过滤半径调大，比如>40，
     并且将连续5个点之间的距离DistanceUtil.getDistance(last, ll ) > 5也调大一点，比如>10，
     这里不是固定死的，你可以根据你的需求调整，如果你的轨迹刚开始效果不是很好，你可以将半径调小，两点之间距离也调小，
     gps的精度半径一般是10-50米
     */

    private LatLng getMostAccuracyLocation(BDLocation location){
        if (location.getRadius() > 40){
            return null;
        }

        LatLng ll = new LatLng( location.getLatitude(),location.getLongitude() );

        if (DistanceUtil.getDistance( last,ll ) > 10){
            last = ll;
            points.clear();//有任意连续两点位置大于10，重新取点
            return null;
        }
        points.add( ll );
        last = ll;
        //有5个连续的点之间的距离小于10，认为gps已稳定，以最新的点为起始点
        if (points.size() >= 5){
            points.clear();
            return ll;
        }
        return ll;
    }

    //悬浮按钮的一些点击事件
    private void initFloatButton(){
        mFloatingActionsMenu = (FloatingActionsMenu) findViewById( R.id.main_actions_menu );
        /*轨迹开始与结束*/
        mAction_d = (FloatingActionButton) findViewById( R.id.action_d );
        mAction_d.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingActionsMenu.toggle();
                if (mAction_d.getTitle().equals( "开始轨迹" )){
                    mAction_d.setTitle( "结束轨迹" );
                    if (mLocationClient != null && !mLocationClient.isStarted()) {
                        mLocationClient.start();
                        progressBarRl.setVisibility( View.VISIBLE );
                        info.setText( "GPS信号搜索中，请稍后..." );
                        mBaiduMap.clear();
                    }
                }else {
                    mAction_d.setTitle( "开始轨迹" );
                    if (mLocationClient != null && mLocationClient.isStarted()){
                        mLocationClient.stop();
                        progressBarRl.setVisibility( View.GONE );
                        if (isFirstLoc){
                            points.clear();
                            last = new LatLng( 0,0 );
                            return;
                        }

                        MarkerOptions oFinish  = new MarkerOptions();// 地图标记覆盖物参数配置类
                        oFinish.position( points.get( points.size() - 1 ) );
                        oFinish.icon( finishBD );// 设置覆盖物图片
                        mBaiduMap.addOverlay( oFinish );// 在地图上添加此图层

                        //复位
                        points.clear();
                        last = new LatLng( 0,0 );
                        isFirstLoc = true;
                    }
                }

            }
        } );
        //地图模式的切换
        mAction_e = (FloatingActionButton) findViewById( R.id.action_e );
        mAction_e.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBaiduMap.getMapType() == BaiduMap.MAP_TYPE_NORMAL){
                    mBaiduMap.setMapType( BaiduMap.MAP_TYPE_SATELLITE );
                    mAction_e.setTitle( getResources().getString( R.string.fab_exchange_map_normal ) );
                }else {
                    mBaiduMap.setMapType( BaiduMap.MAP_TYPE_NORMAL );
                    mAction_e.setTitle( getResources().getString( R.string.fab_exchange_map_site ) );
                }
                mFloatingActionsMenu.toggle();
            }
        } );
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();

        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        // 取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocationClient.unRegisterLocationListener(myListener);
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.getMap().clear();
        mMapView.onDestroy();
        mMapView = null;
        startBD.recycle();
        finishBD.recycle();
        super.onDestroy();

    }
}
