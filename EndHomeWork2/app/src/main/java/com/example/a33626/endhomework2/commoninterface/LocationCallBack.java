package com.example.a33626.endhomework2.commoninterface;

import com.example.a33626.endhomework2.models.Location;

public interface LocationCallBack  {
    /**
     * 定位模块 回调数据接口
     */
    public void receiveLocationData(Location locationData);
}
