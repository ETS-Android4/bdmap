package com.xxl.bdmap;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Title: MapApplication
 * Description:
 * Copyright (c) 版权所有 2022
 * Created DateTime: 2022/2/15$ 14:53$
 * Created by xueli
 */
public class MapApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
