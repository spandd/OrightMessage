package com.example.a33626.endhomework2.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.sqllite.SqlUtil;

public class IndexActivity extends AppCompatActivity implements AllActivityBasicTask {
    private TextView indexTextViewLogin;
    private TextView indexTextViewRegister;
    private SqlUtil sqlUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        this.init();
        this.startAllEvent();
        this.autoLogin();
    }

    //初始化所有属性
    @Override
    public void init(){
        this.indexTextViewLogin = findViewById(R.id.index_textview_login);
        this.indexTextViewRegister = findViewById(R.id.index_textview_register);
        this.sqlUtil = new SqlUtil(this);
    }

    //开启所有控件的监听事件
    @Override
    public void startAllEvent(){
        this.indexTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
                intent.putExtra(Constants.SOURCE_ACTIVITY,IndexActivity.class.getName());
                intent.putExtra(Constants.DESTINATION_ACTIVITY,LoginActivity.class.getName());
                startActivity(intent);
            }
        });
        this.indexTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this, RegisterActivity.class);
                intent.putExtra(Constants.SOURCE_ACTIVITY,IndexActivity.class.getName());
                intent.putExtra(Constants.DESTINATION_ACTIVITY,RegisterActivity.class.getName());
                startActivity(intent);
            }
        });
    }

    public void autoLogin(){
        User user = this.sqlUtil.selectUser();
        if (user.getUserId() != null){
            //说明有用户cache
            Intent intent = new Intent(IndexActivity.this, MainActivity.class);
            intent.putExtra(Constants.SOURCE_ACTIVITY,IndexActivity.class.getName());
            intent.putExtra(Constants.DESTINATION_ACTIVITY,MainActivity.class.getName());
            intent.putExtra(Constants.USER_INFO,user);
            startActivity(intent);
        }
    }
}
