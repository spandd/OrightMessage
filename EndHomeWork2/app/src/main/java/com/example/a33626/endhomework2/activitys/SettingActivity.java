package com.example.a33626.endhomework2.activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.sqllite.SqlUtil;
import com.example.a33626.endhomework2.utils.ImageUtil;

public class SettingActivity extends AppCompatActivity implements AllActivityBasicTask {

    private ImageView settingImageViewHeadPortrait;
    private ImageView settingImageViewReturn;
    private TextView settingTextViewUserId;
    private TextView settingTextViewUserName;
    private TextView settingTextViewPassword;
    private TextView settingTextViewBirthdayMonth;
    private TextView settingTextViewBirthdayDay;
    private User user;
    private SqlUtil sqlUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        startAllEvent();
    }

    @Override
    public void init() {
        this.sqlUtil = new SqlUtil(this);
        this.user = this.sqlUtil.selectUser();
        this.settingImageViewHeadPortrait = findViewById(R.id.setting_imageview_headportrait);
        this.settingTextViewUserId = findViewById(R.id.setting_textview_userid);
        this.settingTextViewUserName = findViewById(R.id.setting_textview_username);
        this.settingTextViewPassword = findViewById(R.id.setting_textview_password);
        this.settingTextViewBirthdayMonth = findViewById(R.id.setting_textview_birthdaymonth);
        this.settingTextViewBirthdayDay = findViewById(R.id.setting_textview_birthdayday);
        this.settingImageViewReturn = findViewById(R.id.setting_imageview_return);
        this.settingTextViewUserId.setText("" + this.user.getUserId());
        this.settingTextViewUserName.setText(this.user.getUserName());
        if (this.user.getBlobHeadPortrait() == null){
            this.settingImageViewHeadPortrait.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.emoji));
        }
        else{
            this.settingImageViewHeadPortrait.setImageBitmap(ImageUtil.blobToBitMap(this.user.getBlobHeadPortrait()));
        }
        this.settingTextViewPassword.setText(user.getPassword());
        this.settingTextViewBirthdayMonth.setText(user.getBirthdayMonth());
        this.settingTextViewBirthdayDay.setText(user.getBirthdayDay());
    }

    @Override
    public void startAllEvent() {
        this.settingImageViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.settingImageViewHeadPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,HeadPortraitActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.USER_INFO,user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
