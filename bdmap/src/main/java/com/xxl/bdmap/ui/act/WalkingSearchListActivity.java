/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.xxl.bdmap.ui.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lxj.xpopup.XPopup;
import com.xxl.bdmap.ui.map.BDLocationUtil;
import com.xxl.bdmap.R;
import com.xxl.bdmap.data.AddressResult;
import com.xxl.bdmap.data.IfLyMapData;
import com.xxl.bdmap.ui.LocationListBottomPop;
import com.xxl.bdmap.ui.map.RouteLineAdapter;
import com.xxl.bdmap.ui.map.SelectRouteDialog;
import com.xxl.bdmap.ui.map.WalkingRouteOverlay;
import com.xxl.bdmap.utils.MapConvertUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Title: WalkingRouteSearchDemos.java
 * Description: 步行路线规划
 * Copyright (c) 小远机器人版权所有 2021
 * Created DateTime: 2022/2/14 16:15
 * Created by xueli.
 */
public class WalkingSearchListActivity extends AppCompatActivity implements BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener {

    // 浏览路线节点相关

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    private MapView mMapView = null;    // 地图View
    private BaiduMap mBaiduMap = null;
    // 搜索模块，也可去掉地图模块独立使用
    private RoutePlanSearch mSearch = null;
    private WalkingRouteResult mWalkingRouteResult = null;
    private boolean hasShowDialog = false;
    private IfLyMapData mapData;
    ArrayList<AddressResult> locationList;
    private MyLocationData myLocationData;


    //机器人当前坐标
    private LatLng mCurrentLatlng;

    LocationListBottomPop pop;

    boolean isFirstLocate = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_route);


        // 初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pop.isShow()) {
                    pop.dismiss();
                }
                finish();
            }
        });

        getBundleData();
        initMap();


    }

    private void initMap() {
        BDLocationUtil.getInstance().startLocation(this, new BDLocationUtil.LocationListener() {
            @Override
            public void onLocation(BDLocation location) {
                Log.d("xxl", location.getCity() + "latu:" + location.getLatitude());
                mCurrentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                // 如果是第一次定位
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

                if (isFirstLocate) {
                    isFirstLocate = false;
                    //给地图设置状态
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
        //地图中心点
        MapStatusUpdate mMapCenter = MapStatusUpdateFactory.newLatLngZoom(mCurrentLatlng, 19f);

        // 设置地图的可移动区域
        //mOverlayBounds = new LatLngBounds.Builder()
        //        .include(new LatLng(30.252782, 120.163385))// 东北角坐标
        //        .include(new LatLng(30.265519, 120.173382))// 西南角坐标
        //        .build();

        // 设置地图中心点以及缩放级别
        mBaiduMap.setMapStatus(mMapCenter);

        // 地图点击事件处理
        mBaiduMap.setOnMapClickListener(this);

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        //设置我的位置
        mBaiduMap.setMyLocationData(getMyLocationData());
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                true, null));
    }

    private void getBundleData() {
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if (intent != null && bundle != null) {
            mapData = (IfLyMapData) bundle.getSerializable("iflymapdata");
            if (mapData != null && mapData.getData() != null) {
                locationList = (ArrayList<AddressResult>) mapData.getData().getResult();
                if (locationList.size() <= 0) return;
                //底部显示列表
                pop = new LocationListBottomPop(this, locationList);
                if (!pop.isShow()) {
//                    pop.show();
                    new XPopup.Builder(WalkingSearchListActivity.this)
                            .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                            .dismissOnTouchOutside(false)
                            .enableDrag(true)
                            .isThreeDrag(true)
                            .asCustom(pop/*.enableDrag(false)*/)
                            .show();
                }
                pop.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        showRoad(locationList.get(position));
                        pop.dismiss();
                    }
                });

                showRoad(locationList.get(0));
            }


        }
    }

    public void showRoad(AddressResult result) {
        mMapView.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchButtonProcess(result);
            }
        }, 1 * 1000);
    }

    /**
     * 发起路线规划搜索示例
     */
    public void searchButtonProcess(AddressResult addr) {
        if (addr == null)
            return;
        // 清除之前的覆盖物
        mBaiduMap.clear();
        // 设置起终点信息 起点参数
//        PlanNode startNode = PlanNode.withCityNameAndPlaceName("杭州",
//                "小远机器人");
        PlanNode startNode = PlanNode.withLocation(mCurrentLatlng);
        // 终点参数
        PlanNode endNode;
        if (!TextUtils.isEmpty(addr.getLatitude())) {//有经纬度优先使用经纬度，更准确
            HashMap map = MapConvertUtil.bd_encrypt(Double.parseDouble(addr.getLatitude()), Double.parseDouble(addr.getLongitude()));
            LatLng endLoc = new LatLng((Double) map.get("bd_lat"), (Double) map.get("bd_lon"));
            ;
            endNode = PlanNode.withLocation(endLoc);
        } else {
            endNode = PlanNode.withCityNameAndPlaceName("杭州", "嘉里中心");
        }
//
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
            Log.d("xxl", result.getSuggestAddrInfo().getSuggestStartNode().size() + "");
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            result.getSuggestAddrInfo();
            AlertDialog.Builder builder = new AlertDialog.Builder(WalkingSearchListActivity.this);
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
            Toast.makeText(WalkingSearchListActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {

            if (result.getRouteLines().size() > 1) {
                mWalkingRouteResult = result;
                if (!hasShowDialog) {
                    SelectRouteDialog selectRouteDialog = new SelectRouteDialog(WalkingSearchListActivity.this,
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
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
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

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        private MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (true) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (true) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }

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
        mBaiduMap.clear();
        mMapView.onDestroy();
    }

    public MyLocationData getMyLocationData() {
        if (null == myLocationData) {
            //120.169558,30.258713——喜茶和南宋胡记之间
            //120.169553,30.258841
            myLocationData = new MyLocationData.Builder()
                    //获取定位精度，默认值为0.0f
                    .accuracy(0)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(270)
                    //获取纬度信息30.25965, 120.167734
                    .latitude(mCurrentLatlng.latitude)
                    //获取经度信息
                    .longitude(mCurrentLatlng.longitude)
                    .build();
        }
        return myLocationData;
    }
}
