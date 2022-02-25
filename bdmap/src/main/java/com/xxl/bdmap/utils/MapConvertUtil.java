package com.xxl.bdmap.utils;

import java.util.HashMap;

/**
 * Title: MapConvertUtil
 * Description:
 * Copyright (c) 版权所有 2022
 * Created DateTime: 2022/2/24$ 17:10$
 * Created by xueli
 */
public class MapConvertUtil {
    public static HashMap bd_encrypt(double gg_lat, double gg_lon) {

        double x = gg_lon, y = gg_lat;

        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);

        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);

        double bd_lon = z * Math.cos(theta) + 0.0065;

        double bd_lat = z * Math.sin(theta) + 0.006;

        HashMap map = new HashMap();
        map.put("bd_lon", bd_lon);
        map.put("bd_lat", bd_lat);
        return map;
    }
}
