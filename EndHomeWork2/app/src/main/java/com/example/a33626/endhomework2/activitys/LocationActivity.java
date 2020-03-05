package com.example.a33626.endhomework2.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.baidu.mapapi.map.MapView;
import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.application.MyApplication;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.commoninterface.LocationCallBack;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Location;
import com.example.a33626.endhomework2.toast.MyToast;


public class LocationActivity extends AppCompatActivity implements AllActivityBasicTask, LocationCallBack {
    private Button locationButtonSend;
    private ImageView locationImageViewReturn;
    private MapView locationMapViewShow;
    private Location locationData;
    private MyToast myToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        init();
        startAllEvent();
    }

    @Override
    protected void onResume() {
        locationMapViewShow.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        locationMapViewShow.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        locationMapViewShow.onDestroy();
        super.onDestroy();
    }

    @Override
    public void init() {
        MyApplication myApplication = new MyApplication(this, this);
        myApplication.initLocationOption();
        this.locationMapViewShow = findViewById(R.id.location_mapview_show);
        this.locationButtonSend = findViewById(R.id.location_button_send);
        this.locationImageViewReturn = findViewById(R.id.location_imageview_return);
    }

    @Override
    public void startAllEvent() {
        this.locationButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送定位信息
                if (locationData == null){
                    showMyToast(Constants.ERROR,Constants.ERROR);
                }
                else{
                    Intent intent = new Intent(LocationActivity.this,ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.LOCATION_INFO,locationData);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
        this.locationImageViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                finish();
            }
        });
    }

    @Override
    public void receiveLocationData(Location locationData) {
        //赋值
        this.locationData = locationData;
    }

    //调用自定义toast
    private void showMyToast(String content,String type) {
        if (this.myToast != null) {
            this.myToast.hide();
        }
        this.myToast = new MyToast(this,
                (ViewGroup) findViewById(R.id.toast_linearlayout_parent), type);
        this.myToast.show(content,1000);
    }
}
