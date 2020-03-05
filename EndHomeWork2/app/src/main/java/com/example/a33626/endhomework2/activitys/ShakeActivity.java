package com.example.a33626.endhomework2.activitys;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;

public class ShakeActivity extends AppCompatActivity implements AllActivityBasicTask {
    private ImageView up, down,shakeImageViewReturn;
    private SensorManager sm;
    private Sensor sensor;
    private Vibrator vibrator;
    private SoundPool soundPool;
    private int sound;
    private long lastTime;
    private SensorEventListener sensorEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        init();
        startAllEvent();
    }

    @Override
    public void init() {
        this.shakeImageViewReturn = findViewById(R.id.shake_imageview_return);
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = Math.abs(event.values[0]);
                float y = Math.abs(event.values[1]);
                float z = Math.abs(event.values[2]);
                if (x > 25 || y > 25 || z > 25) {
                    //绘画
                    draw();
                    //声音
                    playSound();
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        up = findViewById(R.id.upImg);
        down = findViewById(R.id.downImg);
        initSensor();
        initSoundPool();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void startAllEvent() {
        this.shakeImageViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initSensor() {
        this.sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = this.sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.sm.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void initSoundPool(){
        this.soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,1);
        this.sound = soundPool.load(this,R.raw.shake_sound_male,1);
    }

    public void draw(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastTime < 1000){
            return;
        }
        lastTime = currentTime;
        AnimationSet upSet = new AnimationSet(true);
        AnimationSet downSet = new AnimationSet(true);
        TranslateAnimation upUp = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,-1
        );
        upUp.setDuration(1000);
        TranslateAnimation upDown = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,1
        );
        upDown.setDuration(1000);
        upDown.setStartOffset(1000);
        upSet.addAnimation(upUp);
        upSet.addAnimation(upDown);
        up.startAnimation(upSet);
        TranslateAnimation downDown = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,1
        );
        downDown.setDuration(1000);
        TranslateAnimation downUp = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,-1
        );
        downUp.setDuration(1000);
        downUp.setStartOffset(1000);
        downSet.addAnimation(downDown);
        downSet.addAnimation(downUp);
        down.startAnimation(downSet);
    }
    public void playSound(){
        soundPool.play(sound,1,1,0,0,1);
        vibrator.vibrate(new long[]{200,100,200,100,200,100},-1);
    }

}
