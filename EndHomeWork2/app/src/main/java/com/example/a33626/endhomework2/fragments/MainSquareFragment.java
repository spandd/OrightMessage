package com.example.a33626.endhomework2.fragments;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.activitys.SettingActivity;
import com.example.a33626.endhomework2.activitys.ShakeActivity;
import com.example.a33626.endhomework2.activitys.ZoneActivity;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.sqllite.SqlUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainSquareFragment extends Fragment implements AllActivityBasicTask {

    private Button squareButtonZone;
    private Button squareButtonShake;
    private Button squareButtonSetting;
    private User user;
    private SqlUtil sqlUtil;
    public MainSquareFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_square, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        startAllEvent();
    }

    @Override
    public void init() {
        this.sqlUtil = new SqlUtil(getContext());
        this.user = this.sqlUtil.selectUser();
        this.squareButtonZone = getActivity().findViewById(R.id.square_button_zone);
        this.squareButtonShake = getActivity().findViewById(R.id.square_button_shake);
        this.squareButtonSetting = getActivity().findViewById(R.id.square_button_setting);
    }

    @Override
    public void startAllEvent() {
        this.squareButtonZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ZoneActivity.class);
                startActivity(intent);
            }
        });
        this.squareButtonShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShakeActivity.class);
                startActivity(intent);
            }
        });
        this.squareButtonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.USER_INFO,user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (sqlUtil != null){
            this.user = sqlUtil.selectUser();
        }
    }
}
