package com.xxl.bdmap;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Title: OnItemClickListener
 * Description:
 * Copyright (c) 版权所有 2022
 * Created DateTime: 2022/2/25$ 10:58$
 * Created by xueli
 */
public interface OnItemClickListener {
    void onItemClick(@NonNull BaseQuickAdapter<?,?> adapter, @NonNull View view, int position);
}
