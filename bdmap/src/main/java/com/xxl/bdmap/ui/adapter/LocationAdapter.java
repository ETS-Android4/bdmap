package com.xxl.bdmap.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xxl.bdmap.R;
import com.xxl.bdmap.data.AddressResult;

import org.jetbrains.annotations.NotNull;

/**
 * Title: LocationAdapter.java
 * Description:  地点适配器
 * Copyright (c) 版权所有
 * Created DateTime: 2022/2/25 11:01
 * Created by xueli.
 */
public class LocationAdapter extends BaseQuickAdapter<AddressResult, BaseViewHolder> {
    public LocationAdapter() {
        super(R.layout.item_map_location);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, AddressResult s) {
        holder.setText(R.id.addr, s.getName());

    }
}
