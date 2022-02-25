package com.xxl.bdmap.data;

import java.io.Serializable;
import java.util.List;

/**
 * Title: Data.java
 * Description: 地址列表
 * Copyright (c) 小远机器人版权所有 2021
 * Created DateTime: 2022/2/25 10:46
 * Created by xueli.
 */
public class Data implements Serializable {
    List<?> debug;
    List<AddressResult> result;

    public List<?> getDebug() {
        return debug;
    }

    public void setDebug(List<?> debug) {
        this.debug = debug;
    }

    public List<AddressResult> getResult() {
        return result;
    }

    public void setResult(List<AddressResult> result) {
        this.result = result;
    }
}
