package com.xxl.bdmap.ui.map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.xxl.bdmap.R;

/**
 * Title: CustomWalkingRouteOverlay
 * Description:
 * Copyright (c) 版权所有 2022
 * Created DateTime: 2022/1/26$ 16:46$
 * Created by xueli
 */
public class CustomWalkingRouteOverlay extends WalkingRouteOverlay{
    public CustomWalkingRouteOverlay(BaiduMap baiduMap) {
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
