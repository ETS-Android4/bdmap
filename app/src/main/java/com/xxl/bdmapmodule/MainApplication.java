package com.xxl.bdmapmodule;

import android.app.Application;

import com.xxl.bdmap.MapApplication;
import com.xxl.bdmap.OuterUtil;

/**
 * Title: MapApplication
 * Description:
 * Copyright (c) 版权所有 2022
 * Created DateTime: 2022/2/15$ 14:53$
 * Created by xueli
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OuterUtil.initMap(this);
//        SDKInitializer.initialize(getApplicationContext());
    }
}
