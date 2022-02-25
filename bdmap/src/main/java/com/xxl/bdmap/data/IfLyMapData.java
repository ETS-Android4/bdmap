package com.xxl.bdmap.data;

import java.io.Serializable;

/**
 * Title: IfLyMapData
 * Description: 讯飞地图返回数据实体
 * Copyright (c) 版权所有 2022
 * Created DateTime: 2022/2/14$ 15:40$
 * Created by xueli
 */
public class IfLyMapData implements Serializable {

    Answer answer;
    String category;
    Data data;

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
