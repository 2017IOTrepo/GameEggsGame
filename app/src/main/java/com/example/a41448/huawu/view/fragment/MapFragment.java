package com.example.a41448.huawu.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.a41448.huawu.R;
import com.example.a41448.huawu.utils.MyOrientationListener;
import com.example.a41448.huawu.utils.NetUtil;
import com.example.a41448.huawu.utils.map_utils.showImageInMap;
import com.example.a41448.huawu.view.activity.LocationActivity;
import com.example.a41448.huawu.view.activity.PointActivity;
import com.example.a41448.huawu.view.activity.TrackActivity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements MKOfflineMapListener,OnGetGeoCoderResultListener{


    private static final String TAG = "MapFragment";
    /*与地图相关的量*/
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private boolean firstLocation = true;
    private BitmapDescriptor mCurrentMarker;
    private GeoCoder mSearch; // 搜索模块，也可去掉地图模块独立使用
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


    private static final String LOG_CAT = "net_work";
    private MapView mapView = null;
    private MKOfflineMap mOffline = null;
    private MKOLUpdateElement update;
    private int cityid;
    private Context context;
    private Activity activity;


    public OnMapStatusChangeListener mOnMapStatusChangeListener = new OnMapStatusChangeListener();
    public OnMarkerClickListener mOnMarkerClickListener = new OnMarkerClickListener();
    public OnMapClickListener mOnMapClickListener = new OnMapClickListener();
    private boolean isFirstLoc = true;
    private Boolean isLocation = false;
    private List<String> urlList = new ArrayList<String>();

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
        context = getContext();
        activity = getActivity();
        addurlList();
        initMap(view);
        initFloatButton( view );
        return view;
    }


    private void addurlList(){
        urlList.add("https://h5.m.kuosanyun.com/static/images/portrait/rand_user_26.jpg");
        urlList.add("https://h5.m.kuosanyun.com/static/images/portrait/rand_user_2.jpg");
        urlList.add("https://h5.m.kuosanyun.com/static/images/portrait/rand_user_137.jpg");
        urlList.add("https://upload-10014880.file.myqcloud.com/upload/20170412/1944034054.png");
        urlList.add("https://h5.m.kuosanyun.com/static/images/portrait/rand_user_113.jpg");
        urlList.add("https://h5.m.kuosanyun.com/static/images/portrait/rand_user_68.jpg");
        urlList.add("https://h5.m.kuosanyun.com/static/images/portrait/rand_user_176.jpg");
        urlList.add("https://h5.m.kuosanyun.com/static/images/portrait/rand_user_55.jpg");
        urlList.add("https://cvideo.kuosanyun.com/20170906082703444382");
        urlList.add("https://h5.m.kuosanyun.com/static/images/portrait/rand_user_30.jpg");
        urlList.add("https://upload-10014880.file.myqcloud.com/static/images/portrait/rand_user_23.jpg");
        urlList.add("https://h5.m.kuosanyun.com/static/images/portrait/rand_user_84.jpg");
        Log.e(LOG_CAT, "urlList:" + urlList.size());
    }

    private void initMap(View view){
        mOffline = new MKOfflineMap();
        // 传入接口事件，离线地图更新会触发该回调
//        mOffline.init( (MKOfflineMapListener) getContext() );
        judgeNetWorkStatus();//判断网络状况
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
//        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled( false );

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
//        mSearch.setOnGetGeoCodeResultListener( (OnGetGeoCoderResultListener) getActivity() );


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
                    double latitude = bdLocation.getLatitude();//纬度
                    double longitude = bdLocation.getLongitude();//经度
                    String actual_point = latitude + "," + longitude;
                    String vir_point = latitude + "," + longitude;
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(xy).zoom(18.0f);
                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng( xy );
                    mBaiduMap.animateMapStatus( status );
                    firstLocation = false;
                    if (!isLocation) {
                        isLocation = true;

                        // 反Geo搜索
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(xy));
                        loadMapPoint(vir_point);
                    }
                }
                setStatus( bdLocation.getAddrStr() );


            }
        } );

        /*初始化图标*/
        mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);

        mMyOrientationListener = new MyOrientationListener(getContext());

        mMyOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });

        //地图点击事件
        mBaiduMap.setOnMapClickListener(mOnMapClickListener);
        //marker点击事件
        mBaiduMap.setOnMarkerClickListener(mOnMarkerClickListener);
        //监听地图发生变化的事件
        mBaiduMap.setOnMapStatusChangeListener(mOnMapStatusChangeListener);
//        mLocationClient.registerLocationListener(myListener);
        // 设置定位的相关配置
        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);

        mLocationClient.start();//开始定位
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
        if (update != null) {
//            Log.d(LOG_CAT, "update_resume:" + update.ratio);
            if (update.ratio != 100) {
                mOffline.start(cityid);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        Log.i( TAG,"onPause：is called" );
        if (update != null) {
//            Log.d(LOG_CAT, "update_pause:" + update.ratio);
            if (update.ratio != 100) {
                mOffline.pause(cityid);
            }
        }
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
        mOffline.destroy();
        mSearch.destroy();
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
                startActivity( new Intent( getContext(), PointActivity.class ) );
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


    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                update = mOffline.getUpdateInfo(state);

                // 处理下载进度更新提示
                if (update != null) {
//                    Log.d(LOG_CAT, getString(R.string.all_download) + String.valueOf(update.ratio) + "%");
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                break;
            default:

                break;
        }
    }

    private void judgeNetWorkStatus() {
        int netWorkState = NetUtil.getNetWrokState(context);
        switch (netWorkState) {
            case NetUtil.NETWORK_NONE:
                Log.d(LOG_CAT, "当前网络不可用");
                break;
            case NetUtil.NETWORK_MOBILE:
                Log.d(LOG_CAT, "当前正在使用移动网络");
                break;
            case NetUtil.NETWORK_WIFI:
                Log.d(LOG_CAT, "当前正在使用WIFI");
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    private void loadMapPoint(String vir_point) {
        for (int i = 0; i < urlList.size(); i++) {
            String lagLng = NetUtil.getLagLng(vir_point);
            addMarker(lagLng, urlList.get(i));
        }
    }

    public class OnMapClickListener implements BaiduMap.OnMapClickListener {

        @Override
        public void onMapClick(LatLng latLng) {
            mBaiduMap.hideInfoWindow();
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi) {
            return false;
        }
    }

    public class OnMarkerClickListener implements BaiduMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {
            //进入高级群
            Bundle bundle = marker.getExtraInfo();

            if (bundle != null) {

            }
            return true;
        }
    }

    public class OnMapStatusChangeListener implements BaiduMap.OnMapStatusChangeListener {
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
            float zoom = mapStatus.zoom;
            int round = Math.round(zoom);
            Log.i(LOG_CAT, "zoom:" + zoom + "," + round + "," + mapStatus.target);
            LatLng latLng_whole = mapStatus.target;
            // 反Geo搜索
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng_whole));

        }
    }

    //动态添加marker
    public void addMarker(String actual_point, final String link) {

        if (!TextUtils.isEmpty(actual_point)) {
            String[] str = actual_point.split(",");

            Double latitude = Double.parseDouble(str[0]);
            Double longitude = Double.parseDouble(str[1]);

            final LatLng latLng = new LatLng(latitude, longitude);
            final Bundle bundle = new Bundle();
            bundle.putString("link", link);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    drawMapSpot(latLng, bundle, link);
                }
            }.start();
        }
    }

    private void drawMapSpot(final LatLng latLng, final Bundle bundle, final String link) {
        View view = null;
        ImageView img_head = null;
        Bitmap loadBitmap = null;
        view = View.inflate(context, R.layout.layout_item, null);
        img_head = view.findViewById(R.id.iv_p_v);

        if (TextUtils.isEmpty(link)) {
            loadBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.nim_avatar_default);
        } else {
            loadBitmap = showImageInMap.loadBitmap(link, true, false, R.mipmap.nim_avatar_default);
        }

        img_head.setImageBitmap(loadBitmap);

        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(view);
        MarkerOptions options = new MarkerOptions().position(latLng).icon(descriptor).extraInfo(bundle);
        Marker marker = (Marker) mBaiduMap.addOverlay(options);

        if (loadBitmap != null && !loadBitmap.isRecycled()) {
            loadBitmap.isRecycled();
        }

    }
}
