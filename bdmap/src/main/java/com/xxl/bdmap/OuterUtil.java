package com.xxl.bdmap;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Title: OuterUtil
 * Description:
 * Copyright (c) 版权所有 2022
 * Created DateTime: 2022/2/15$ 14:57$
 * Created by xueli
 */
public class OuterUtil {
    public static void initMap(Application con) {
        SDKInitializer.initialize(con);
    }
}
