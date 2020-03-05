package com.example.a33626.endhomework2.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.BaseInfo;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.task.LoginTask;
import com.example.a33626.endhomework2.toast.MyToast;
import com.example.a33626.endhomework2.utils.MyTextWatcher;
import com.example.a33626.endhomework2.utils.MyThreadPool;

public class LoginActivity extends AppCompatActivity implements AllActivityBasicTask {
    private ImageView loginImageViewReturn;
    private EditText loginEditTextUserName;
    private EditText loginEditTextPassword;
    private Button loginButtonSubmit;
    private Handler loginHandler;
    private MyThreadPool threadPool;
    private LoginTask loginTask;
    private MyTextWatcher myTextWatcher;
    private MyToast myToast;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.init();
        this.startAllEvent();

    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.login_out);
    }

    //初始化所有属性
    @Override
    public void init(){
        this.overridePendingTransition(R.anim.login_in, 0);
        this.loginImageViewReturn = findViewById(R.id.login_imageview_return);
        this.loginButtonSubmit = findViewById(R.id.login_button_submit);
        this.loginEditTextUserName = findViewById(R.id.login_edittext_username);
        this.loginEditTextPassword = findViewById(R.id.login_edittext_password);
        this.loginHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                BaseInfo baseInfo = (BaseInfo)msg.getData().getSerializable(Constants.BASE_INFO);
                switch (msg.what){
                    case Constants.LOGIN_INFO_USER_NAME_NOT_FOUND_CODE:
                        showMyToast(baseInfo.getLoginInfo(),Constants.ERROR);
                        break;
                    case Constants.LOGIN_INFO_PASSWORD_ERROR_CODE:
                        showMyToast(baseInfo.getLoginInfo(),Constants.ERROR);
                        break;
                    case Constants.LOGIN_INFO_SUCCESS_CODE:
                        showMyToast(baseInfo.getLoginInfo(),Constants.CORRECT);

                        //先存
                        user.setUserName(loginEditTextUserName.getText().toString());

                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra(Constants.USER_INFO,user);
                        intent.putExtra(Constants.SOURCE_ACTIVITY,LoginActivity.class.getName());
                        intent.putExtra(Constants.DESTINATION_ACTIVITY,MainActivity.class.getName());
                        startActivity(intent);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        this.threadPool = MyThreadPool.getInstance();
        this.loginTask = new LoginTask(this.loginHandler);
        //开启文字监听器
        this.myTextWatcher = new MyTextWatcher(this.loginButtonSubmit,this.loginEditTextUserName,this.loginEditTextPassword);
        //接受参数 设置初始值
        this.user = new User();
        Intent intent = getIntent();
        if (intent.getStringExtra(Constants.SOURCE_ACTIVITY).equals(RegisterActivity.class.getName())){
            //如果是注册传来的
            //取值
            User userInfo = (User)intent.getExtras().getSerializable(Constants.USER_INFO);
            this.loginEditTextUserName.setText(userInfo.getUserName());
            this.loginEditTextPassword.setText(userInfo.getPassword());
        }
    }

    //开启所有控件的监听事件
    @Override
    public void startAllEvent(){
        //返回上个activity
        this.loginImageViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });

        //登陆
        this.loginButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = loginEditTextUserName.getText().toString();
                String password = loginEditTextPassword.getText().toString();
                loginTask.init(userName,password);
                threadPool.execute(loginTask);
                threadPool.getActiveCount();
            }
        });
    }
    //调用自定义toast
    private void showMyToast(String content,String type) {
        if (this.myToast != null) {
            this.myToast.hide();
        }
        this.myToast = new MyToast(LoginActivity.this,
                (ViewGroup) this.findViewById(R.id.toast_linearlayout_parent), type);
        this.myToast.show(content,1000);
    }
}
