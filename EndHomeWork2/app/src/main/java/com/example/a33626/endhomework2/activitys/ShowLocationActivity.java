package com.example.a33626.endhomework2.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Chat;

/**
 * 这个是定位模块
 */
public class ShowLocationActivity extends AppCompatActivity implements AllActivityBasicTask {
    private Chat chat;
    private MapView showLocationMapViewShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlocation);
        init();
        startAllEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.showLocationMapViewShow.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.showLocationMapViewShow.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.showLocationMapViewShow.onDestroy();
    }

    @Override
    public void init() {
        this.chat = (Chat)getIntent().getSerializableExtra(Constants.CHAT_INFO);
        this.showLocationMapViewShow = findViewById(R.id.showlocation_mapview_show);
        BaiduMap baiduMap = this.showLocationMapViewShow.getMap();
        baiduMap.setMyLocationEnabled(true);
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(Float.valueOf(chat.getChatAccuracy()))
                .direction(Float.valueOf(chat.getChatDirection()))
                .latitude(Double.valueOf(chat.getChatLatitude()))
                .longitude(Double.valueOf(chat.getChatLongitude()))
                .build();
        baiduMap.setMyLocationData(locData);
        LatLng latLng = new LatLng(Double.valueOf(chat.getChatLatitude()), Double.valueOf(chat.getChatLongitude()));
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(mapStatusUpdate);
    }

    @Override
    public void startAllEvent() {

    }
}
