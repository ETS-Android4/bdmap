package com.xxl.bdmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xxl.bdmap.data.IfLyMapData;
import com.xxl.bdmap.ui.act.WalkingSearchActivity;
import com.xxl.bdmap.ui.act.WalkingSearchListActivity;

/**
 * Title: ActivityHelper
 * Description:
 * Copyright (c) 版权所有 2022
 * Created DateTime: 2022/2/15$ 17:27$
 * Created by xueli
 */
public class ActivityHelper {

    public static void startWalkSearchActivity(Context context, Double latitude, Double Longitude) {
        Bundle args = new Bundle();
        args.putDouble("latitude", latitude);
        args.putDouble("longitude", Longitude);
        Intent intent = new Intent(context, WalkingSearchActivity.class);
        intent.putExtra("map_data", args);
        context.startActivity(intent);
    }

    public static void startWalkListActivity(Context context, IfLyMapData data) {
        Bundle args = new Bundle();
        args.putSerializable("iflymapdata", data);
        Intent intent = new Intent(context, WalkingSearchListActivity.class);
        intent.putExtras(args);
        context.startActivity(intent);
    }
}
