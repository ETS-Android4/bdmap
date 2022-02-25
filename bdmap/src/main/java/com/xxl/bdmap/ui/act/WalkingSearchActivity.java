/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.xxl.bdmap.ui.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xxl.bdmap.ui.map.BDLocationUtil;
import com.xxl.bdmap.R;
import com.xxl.bdmap.data.AddressResult;
import com.xxl.bdmap.data.IfLyMapData;
import com.xxl.bdmap.ui.map.CustomWalkingRouteOverlay;
import com.xxl.bdmap.ui.map.RouteLineAdapter;
import com.xxl.bdmap.ui.map.SelectRouteDialog;
import com.xxl.bdmap.ui.map.WalkingRouteOverlay;

import java.util.ArrayList;


/**
 * Title: WalkingRouteSearchDemos.java
 * Description: 步行路线规划
 * Copyright (c) 小远机器人版权所有 2021
 * Created DateTime: 2022/2/14 16:15
 * Created by xueli.
 */
public class WalkingSearchActivity extends AppCompatActivity implements BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener {
    boolean isFirstLocate = true;
    // 浏览路线节点相关

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    private MapView mMapView = null;    // 地图View
    private BaiduMap mBaiduMap = null;
    // 搜索模块，也可去掉地图模块独立使用
    private RoutePlanSearch mSearch = null;
    private WalkingRouteResult mWalkingRouteResult = null;
    private boolean hasShowDialog = false;
    //目的地坐标
    private Double latitude;
    private Double longitude;
    //机器人当前坐标
    private LatLng mCurrentLatlng;

    private IfLyMapData mapData;
    ArrayList<AddressResult> locationList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_route);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("WalkingRouteSearchDemos", "onclick");
                finish();
            }
        });
        mBaiduMap = mMapView.getMap();
        getBundleData();
        initMap();


    }

    private void initMap() {
        BDLocationUtil.getInstance().startLocation(this, new BDLocationUtil.LocationListener() {
            @Override
            public void onLocation(BDLocation location) {
                Log.d("WalkingRouteSearchDemos", "long:" + location.getLongitude() + "lat:" + location.getLatitude());
                mCurrentLatlng = new LatLng(
                        location.getLatitude(),
                        location.getLongitude());
                // 如果是第一次定位
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

                if (isFirstLocate) {
                    isFirstLocate = false;
                    // 设置地图中心点以及缩放级别
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(ll, 19f));
                }
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection()).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
            }

            @Override
            public void onLocationFailed() {
                mCurrentLatlng = new LatLng(
                        30.19319,
                        120.191436);
            }
        });

        //开启地图的定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 地图点击事件处理
        mBaiduMap.setOnMapClickListener(this);

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);


    }

    private void getBundleData() {
        Bundle b = getIntent().getBundleExtra("map_data");
        if (b != null) {
            latitude = b.getDouble("latitude", 0);
            longitude = b.getDouble("longitude", 0);
            mMapView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchButtonProcess(latitude, longitude);
                }
            }, 1 * 1000);

        }
    }

    /**
     * 发起路线规划搜索示例
     */
    public void searchButtonProcess(Double lati, Double longi) {
        // 清除之前的覆盖物
        mBaiduMap.clear();
        // 设置起终点信息 起点参数
//        PlanNode startNode = PlanNode.withCityNameAndPlaceName("杭州",
//                "小远机器人");
        PlanNode startNode = PlanNode.withLocation(mCurrentLatlng);
//         终点参数
//        PlanNode endNode = PlanNode.withCityNameAndPlaceName("杭州", "嘉里中心"
//        );
        LatLng endLoc = new LatLng(lati, longi);
        PlanNode endNode = PlanNode.withLocation(endLoc);
        // 实际使用中请对起点终点城市进行正确的设定
        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(startNode) // 起点
                .to(endNode)); // 终点

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 步行路线结果回调
     *
     * @param result 步行路线结果
     */
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (null == result) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Log.d("walkroute", result.getSuggestAddrInfo().getSuggestStartNode().size() + "");
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            result.getSuggestAddrInfo();
            AlertDialog.Builder builder = new AlertDialog.Builder(WalkingSearchActivity.this);
            builder.setTitle("提示");
            builder.setMessage("检索地址有歧义，请重新设置。\n可通过getSuggestAddrInfo()接口获得建议查询信息");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return;
        }

        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(WalkingSearchActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {

            if (result.getRouteLines().size() > 1) {
                mWalkingRouteResult = result;
                if (!hasShowDialog) {
                    SelectRouteDialog selectRouteDialog = new SelectRouteDialog(WalkingSearchActivity.this,
                            result.getRouteLines(), RouteLineAdapter.Type.WALKING_ROUTE);
                    selectRouteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            hasShowDialog = false;
                        }
                    });

                    selectRouteDialog.show();
                    hasShowDialog = true;
                }
            } else if (result.getRouteLines().size() == 1) {
                // 直接显示
//                mRouteLine = result.getRouteLines().get(0);
                Log.d("walkroute", result.getRouteLines().get(0).describeContents() + "");
                WalkingRouteOverlay overlay = new CustomWalkingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
//                mRouteOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("route result", "结果数<0");
            }
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {

    }


    @Override
    public void onMapClick(LatLng point) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public void onMapPoiClick(MapPoi poi) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放检索对象
        if (mSearch != null) {
            mSearch.destroy();
        }
        BDLocationUtil.getInstance().stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.clear();
        mMapView.onDestroy();
    }

}
