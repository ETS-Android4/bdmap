package com.xxl.bdmap.ui.map;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Title: BDLocation.java
 * Description: 百度地图
 * Copyright (c) 小远机器人版权所有 2021
 * Created DateTime: 2022/1/8 14:18
 * Created by xueli.
 */
public class BDLocationUtil {

    public static String TAG = BDLocationUtil.class.getSimpleName();
    private LocationListener locationListener;
    LocationClient locationClient;
    private static BDLocationUtil instance;

    public static BDLocationUtil getInstance() {
        if (instance == null) {
            instance = new BDLocationUtil();
        }
        return instance;
    }

    public void startLocation(Context context, LocationListener locationListener) {
        this.locationListener = locationListener;
        startLocation(context);
    }

    private void startLocation(Context context) {
        locationClient = new LocationClient(context);
        MyLocationListener locationListener = new MyLocationListener();
        locationClient.registerLocationListener(locationListener);
        initLocation(locationClient);
        locationClient.start();
    }

    public void stop() {
        locationClient.stop();
    }

    public static class Location {
        public String cityName;
        public String cityCode;
    }

    public interface LocationListener {
        void onLocation(BDLocation location);

        void onLocationFailed();
    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null) {
                if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
                    locationListener.onLocation(bdLocation);
                } else {
                    Log.d(TAG, "onReceiveLocation: 百度定位 失败：" + bdLocation.getLocTypeDescription());
                    locationListener.onLocationFailed();

                }
            }

        }
    }


    private void initLocation(LocationClient locationClient) {
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        // 可选，默认false，设置是否开启Gps定位
        option.setOpenGps(true);
        // 设置坐标类型
        option.setCoorType("bd09ll");
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(true);
        //默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(5000);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，
        // 该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        //setOpenAutoNotifyMode(int minTimeInterval, int minDistance, int locSensitivity)
        //option.setOpenAutoNotifyMode(5000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //设置locationClientOption
        locationClient.setLocOption(option);
    }

}
