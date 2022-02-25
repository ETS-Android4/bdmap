package com.xxl.bdmap.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lxj.xpopup.core.BottomPopupView;
import com.xxl.bdmap.R;
import com.xxl.bdmap.data.AddressResult;
import com.xxl.bdmap.ui.adapter.LocationAdapter;

import java.util.ArrayList;

/**
 * Title: LocationList
 * Description: 地点底部弹窗
 * Copyright (c) 版权所有 2022
 * Created DateTime: 2022/2/18$ 11:15$
 * Created by xueli
 */
public class LocationListBottomPop extends BottomPopupView {
    RecyclerView recyclerView;
    private ArrayList<AddressResult> addrs;
    private LocationAdapter locationAdapter;
    private OnItemClickListener mItemClickListener;

    public ArrayList<AddressResult> getAddrs() {
        return addrs;
    }

    public void setAddrs(ArrayList<AddressResult> addrs) {
        this.addrs = addrs;
        this.locationAdapter.notifyDataSetChanged();
    }

    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public LocationListBottomPop(@NonNull Context context) {
        super(context);
    }

    public LocationListBottomPop(@NonNull Context context, ArrayList<AddressResult> data) {
        super(context);
        this.addrs = data;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_map_location;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        recyclerView = findViewById(R.id.recyclerView);
        initAdapter();
    }

    private void initAdapter() {
        if (addrs != null && addrs.size() > 0) {
            Log.d("xxl_addr", addrs.size() + "");
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            locationAdapter = new LocationAdapter();
            locationAdapter.setNewInstance(addrs);
            recyclerView.setAdapter(locationAdapter);
            locationAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(adapter, view, position);
                    }
                }
            });
        }
    }
}
