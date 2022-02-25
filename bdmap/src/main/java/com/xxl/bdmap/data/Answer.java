package com.xxl.bdmap.data;

import java.io.Serializable;

/**
 * Title: Answer
 * Description:
 * Copyright (c) 版权所有 2022
 * Created DateTime: 2022/2/14$ 16:01$
 * Created by xueli
 */
public class Answer implements Serializable {
    String text;
    String type;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
