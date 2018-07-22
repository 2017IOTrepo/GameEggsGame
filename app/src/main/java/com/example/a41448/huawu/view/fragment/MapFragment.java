package com.example.a41448.huawu.view.fragment;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.utils.MyOrientationListener;
import com.example.a41448.huawu.view.activity.TrackActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MapFragment extends Fragment {


    private static final String TAG = "MapFragment";
    /*与地图相关的量*/
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private boolean firstLocation = true;
    private BitmapDescriptor mCurrentMarker;
    private Geocoder mSearch;
    private MyLocationConfiguration.LocationMode mLocationMode;
    private double mLatitude;
    private double mLongitude;



    /*自定义图标*/
    private BitmapDescriptor mIconLocation;
    private float mCurrentX;
    private boolean isLastTrackExist = true;

    /*与悬浮按钮相关*/
    private FloatingActionsMenu mFloatingActionsMenu;
    private FloatingActionButton mActionMap;
    private FloatingActionButton mActionModel;
    private FloatingActionButton mAction_c;
    private FloatingActionButton mAction_d;
    private FloatingActionButton mAction_e;

    public static String location;
    private MyOrientationListener mMyOrientationListener;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private String mStatus;   /*用户当前位置*/
    public static MapFragment newInstance() {
        Bundle bundle = new Bundle();
        MapFragment fragment = new MapFragment();
        fragment.setArguments( bundle );
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_map, container, false );
        initMap(view);
        initFloatButton( view );

        return view;
    }

    private void initMap(View view){
        mMapView = (MapView) view.findViewById( R.id.main_map_view );
        mBaiduMap = mMapView.getMap();

        //隐藏百度地图logo
        View child = mMapView.getChildAt( 1 );
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility( View.INVISIBLE );
        }
        // 隐藏缩放控件
        mMapView.showZoomControls( true );
        /*放大地图倍数，标尺为50米*/
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo( 18.0f );
        mBaiduMap.setMapStatus( msu );

        /*定义初始化*/
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL; /*定位模式*/
        mLocationClient = new LocationClient( getContext() );
        mLocationClient.registerLocationListener( new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null || mMapView == null)
                    return;
                MyLocationData locData = new MyLocationData
                        .Builder()
                        .direction( mCurrentX)
                        .accuracy( bdLocation.getRadius() )
                        .latitude( bdLocation.getLatitude() )
                        .longitude( bdLocation.getLongitude() )
                        .build();
                mBaiduMap.setMyLocationData( locData );

                /*设置自定义图标*/
                MyLocationConfiguration configuration = new MyLocationConfiguration( mLocationMode,true,mIconLocation );
                mBaiduMap.setMyLocationConfiguration( configuration );

                /*更新经纬度*/
                mLatitude = bdLocation.getLatitude();
                mLongitude = bdLocation.getLongitude();

                if (firstLocation) {
                    //第一定位时，将地图定位到当前位置
                    LatLng xy = new LatLng( bdLocation.getLatitude(),
                            bdLocation.getLongitude() );
                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng( xy );
                    mBaiduMap.animateMapStatus( status );
                    firstLocation = false;
                }
                setStatus( bdLocation.getAddrStr() );
            }
        } );
        // 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);

        /*初始化图标*/
        mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);

        mMyOrientationListener = new MyOrientationListener(getContext());

        mMyOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // 如果要显示位置图标,必须先开启图层定位
        mBaiduMap.setMyLocationEnabled( true );
        if (!mLocationClient.isStarted()){
            Log.i( TAG,"onStart is called" );
            mLocationClient.start();
        }
        /*开启方向传感器*/
        mMyOrientationListener.start();

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        Log.i( TAG,"OnResume: is called" );

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        Log.i( TAG,"onPause：is called" );
    }

    @Override
    public void onStop() {
        super.onStop();
        /*停止定位*/
        mBaiduMap.setMyLocationEnabled( false );
        mLocationClient.stop();
        mMyOrientationListener.stop();
        Log.i( TAG,"onStop is called" );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        Log.i(TAG,"onDestory is called");
    }



    public void setStatus(String status) {
        mStatus = status;
        location = status;
        Log.d(TAG, mStatus);
    }


    //悬浮按钮的一些点击事件
    private void initFloatButton(View view){
        mFloatingActionsMenu = (FloatingActionsMenu) view.findViewById( R.id.main_actions_menu );
        /*地图类型*/
        mActionMap = (FloatingActionButton) view.findViewById( R.id.action_a );
        mActionMap.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBaiduMap.getMapType() == BaiduMap.MAP_TYPE_NORMAL){
                    mBaiduMap.setMapType( BaiduMap.MAP_TYPE_SATELLITE );
                    mActionMap.setTitle( getResources().getString( R.string.fab_exchange_map_normal ) );
                }else {
                    mBaiduMap.setMapType( BaiduMap.MAP_TYPE_NORMAL );
                    mActionMap.setTitle( getResources().getString( R.string.fab_exchange_map_site ) );
                }
                mFloatingActionsMenu.toggle();
            }
        } );

        mActionModel = (FloatingActionButton) view.findViewById( R.id.action_b );
        mActionModel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocationMode == MyLocationConfiguration.LocationMode.NORMAL){
                    mLocationMode = MyLocationConfiguration.LocationMode.COMPASS;
                    mActionModel.setTitle( getResources().getString( R.string.fab_exchange_model_common ) );
                }else {
                    mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
                    mActionModel.setTitle( getResources().getString( R.string.fab_exchange_model_compass ) );
                }
                mFloatingActionsMenu.toggle();
            }
        } );

        mAction_c = (FloatingActionButton) view.findViewById( R.id.action_c );
        mAction_c.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( getContext(), TrackActivity.class ) );
                mFloatingActionsMenu.toggle();
            }
        } );
        mAction_d = (FloatingActionButton) view.findViewById( R.id.action_d );
        mAction_d.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mFloatingActionsMenu.toggle();
            }
        } );
        mAction_e = (FloatingActionButton) view.findViewById( R.id.action_e );
        mAction_e.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*定位到我的位置*/
                centerToMyLocation();
                mFloatingActionsMenu.toggle();
            }
        } );
    }

    /*定位到我的位置*/
    private void centerToMyLocation() {
//        LatLng latLng = new LatLng(mLongitude, mLatitude);
        LatLng latLng = new LatLng(mLatitude, mLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }


}
