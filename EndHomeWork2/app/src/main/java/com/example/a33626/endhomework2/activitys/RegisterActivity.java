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
import android.widget.Spinner;

import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.BaseInfo;
import com.example.a33626.endhomework2.task.RegisterFindUserNameTask;
import com.example.a33626.endhomework2.task.RegisterTask;
import com.example.a33626.endhomework2.toast.MyToast;
import com.example.a33626.endhomework2.utils.MyTextWatcher;
import com.example.a33626.endhomework2.utils.MyThreadPool;

public class RegisterActivity extends AppCompatActivity implements AllActivityBasicTask {
    private ImageView registerImageViewReturn;
    private EditText registerEditTextUserName;
    private EditText registerEditTextPassword;
    private EditText registerEditTextRePassword;
    private Spinner registerSpinnerMonth;
    private Spinner registerSpinnerDay;
    private Button registerButtonSubmit;
    private Handler registerHandler;
    private MyThreadPool threadPool;
    private RegisterTask registerTask;
    private RegisterFindUserNameTask registerFindUserNameTask;
    private MyToast myToast;
    private MyTextWatcher myTextWatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.init();
        this.startAllEvent();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.register_out);
    }

    //初始化所有属性
    @Override
    public void init(){
        this.overridePendingTransition(R.anim.register_in, 0);
        this.registerImageViewReturn = findViewById(R.id.register_imageview_return);
        this.registerEditTextUserName = findViewById(R.id.register_edittext_username);
        this.registerEditTextPassword = findViewById(R.id.register_edittext_password);
        this.registerEditTextRePassword = findViewById(R.id.register_edittext_repassword);
        this.registerButtonSubmit = findViewById(R.id.register_button_submit);
        this.registerSpinnerMonth = findViewById(R.id.register_spinner_month);
        this.registerSpinnerDay = findViewById(R.id.register_spinner_day);
        this.registerHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                BaseInfo baseInfo = (BaseInfo)msg.getData().getSerializable(Constants.BASE_INFO);
                switch (msg.what){
                    case Constants.REGISTER_INFO_USER_NAME_REPEAT_CODE:
                        showMyToast(baseInfo.getRegisterInfo(),Constants.ERROR);
                        registerEditTextUserName.setFocusable(true);
                        registerEditTextUserName.setFocusableInTouchMode(true);
                        registerEditTextUserName.requestFocus();
                        registerEditTextUserName.requestFocusFromTouch();
                        break;
                    case Constants.REGISTER_INFO_USER_NAME_NOT_REPEAT_CODE:
                        showMyToast(baseInfo.getRegisterInfo(),Constants.CORRECT);
                        break;
                    case Constants.REGISTER_INFO_ERROR_CODE:
                        showMyToast(baseInfo.getRegisterInfo(),Constants.ERROR);
                        break;
                    case Constants.REGISTER_INFO_SUCCESS_CODE:
                        showMyToast(baseInfo.getRegisterInfo(),Constants.CORRECT);
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        intent.putExtra(Constants.SOURCE_ACTIVITY,RegisterActivity.class.getName());
                        intent.putExtra(Constants.DESTINATION_ACTIVITY,LoginActivity.class.getName());
                        intent.putExtra(Constants.USER_INFO,msg.getData().getSerializable(Constants.USER_INFO));
                        startActivity(intent);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        this.threadPool = MyThreadPool.getInstance();
        this.registerTask = new RegisterTask(this.registerHandler);
        this.registerFindUserNameTask = new RegisterFindUserNameTask(this.registerHandler);
        //初始化自定义文本监听器
        this.myTextWatcher = new MyTextWatcher(this.registerButtonSubmit,this.registerEditTextUserName,this.registerEditTextPassword,this.registerEditTextRePassword);
    }

    //开启所有控件的监听事件
    @Override
    public void startAllEvent(){
        this.registerImageViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        //注册提交监听事件
        this.registerButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = registerEditTextUserName.getText().toString();
                String password = registerEditTextPassword.getText().toString();
                String rePassword = registerEditTextRePassword.getText().toString();
                String birthdayMonth = registerSpinnerMonth.getSelectedItem().toString();
                String birthdayDay = registerSpinnerDay.getSelectedItem().toString();
                if (password.equals(rePassword)){
                    registerTask.init(userName,password,birthdayMonth,birthdayDay);
                    threadPool.execute(registerTask);
                    threadPool.getActiveCount();
                }
                else {
                    showMyToast(Constants.REGISTER_INFO_REPASSWORD_ERROR,Constants.ERROR);
                    registerEditTextRePassword.setFocusable(true);
                    registerEditTextRePassword.setFocusableInTouchMode(true);
                    registerEditTextRePassword.requestFocus();
                    registerEditTextRePassword.requestFocusFromTouch();
                }
            }
        });
        //自动查询注册时账号是否已经存在
        this.registerEditTextUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String userName = registerEditTextUserName.getText().toString();
                if (userName.length() > 0 && hasFocus == false){
                    registerFindUserNameTask.init(userName);
                    threadPool.execute(registerFindUserNameTask);
                    threadPool.getActiveCount();
                }
            }
        });
    }

    private void showMyToast(String content,String type) {
        if (this.myToast != null) {
            this.myToast.hide();
        }
        this.myToast = new MyToast(RegisterActivity.this,
                (ViewGroup) this.findViewById(R.id.toast_linearlayout_parent), type);
        this.myToast.show(content,1000);
    }
}
