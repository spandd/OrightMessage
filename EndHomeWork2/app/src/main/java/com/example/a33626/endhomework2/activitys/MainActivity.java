package com.example.a33626.endhomework2.activitys;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.fragments.MainFriendsFragment;
import com.example.a33626.endhomework2.fragments.MainMessageFragment;
import com.example.a33626.endhomework2.fragments.MainSquareFragment;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.services.SocketService;
import com.example.a33626.endhomework2.sqllite.SqlUtil;
import com.example.a33626.endhomework2.task.FindUserBaseInfoTask;
import com.example.a33626.endhomework2.utils.MyThreadPool;
import com.example.a33626.endhomework2.utils.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AllActivityBasicTask {
    private MyViewPager myViewPager;
    private List<Fragment> fragmentList;
    private RadioGroup bottomMenu;
    private User user;
    private Handler mainHandler;
    private FindUserBaseInfoTask findUserBaseInfoTask;
    private MyThreadPool myThreadPool;

    private SqlUtil sqlUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        startAllEvent();
    }

    @Override
    public void init() {
        verifyStoragePermissions(this);
        this.sqlUtil = new SqlUtil(this);
        this.myThreadPool = MyThreadPool.getInstance();
        this.mainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case Constants.FIND_USER_BASE_INFO:
                        User resUser = (User) msg.getData().getSerializable(Constants.USER_INFO);
                        if (resUser != null){
                            //数据传过来 才会更新真正的fragmentUI
                            user = resUser;
                            sqlUtil.insertData(user);
                            init2();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
        this.user =  sqlUtil.selectUser();
        Intent intent = getIntent();
        if (intent.getStringExtra(Constants.SOURCE_ACTIVITY).equals(LoginActivity.class.getName())){
            //登陆界面传来的值
            this.user = (User) intent.getExtras().getSerializable(Constants.USER_INFO);
            //初始化用户信息
            this.findUserBaseInfoTask = new FindUserBaseInfoTask(this.mainHandler);
            this.findUserBaseInfoTask.init(user.getUserName());
            this.myThreadPool.execute(this.findUserBaseInfoTask);
        }
        else if (intent.getStringExtra(Constants.SOURCE_ACTIVITY).equals(IndexActivity.class.getName())){
            //首页来的 说明是自动登录
            this.user = (User) intent.getExtras().getSerializable(Constants.USER_INFO);
        init2();
    }


    }

    @Override
    public void startAllEvent() {
    }


    public void init2(){
        myViewPager = (MyViewPager) findViewById(R.id.myviewpager_container);
        fragmentList = new ArrayList<>();
        fragmentList.add(new MainMessageFragment());
        fragmentList.add(new MainFriendsFragment());
        fragmentList.add(new MainSquareFragment());
        bottomMenu = findViewById(R.id.radiogroup_bottommenu);
        myViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });

        startAllEvent2();
        Intent intent = new Intent(MainActivity.this,SocketService.class);
        startService(intent);

    }

    public void startAllEvent2(){
        myViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int current = myViewPager.getCurrentItem();
                switch (current){
                    case 0:
                        bottomMenu.check(R.id.radiobutton_message);
                        break;
                    case 1:
                        bottomMenu.check(R.id.radiobutton_friend);
                        break;
                    case 2:
                        bottomMenu.check((R.id.radiobutton_square));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bottomMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int current = 0;
                switch (checkedId) {
                    case R.id.radiobutton_message:
                        System.out.println("0");
                        current = 0;
                        break;
                    case R.id.radiobutton_friend:
                        current = 1;
                        System.out.println("1");
                        break;
                    case R.id.radiobutton_square:
                        System.out.println("2");
                        current = 2;
                        break;
                }
                if(myViewPager.getCurrentItem()!=current){
                    myViewPager.setCurrentItem(current);
                }
            }
        });
        bottomMenu.check(R.id.radiobutton_message);
    }


    public boolean isServiceWork(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            System.out.println(service.service.getClassName());
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /*
     * android 动态权限申请
     * */
    public static void verifyStoragePermissions(Activity activity) {
        try {


            //检测是否有写的权限
            int permissionRead = ActivityCompat.checkSelfPermission(activity, Constants.PERMISSIONS[0]); //读
            int permissionWrite = ActivityCompat.checkSelfPermission(activity,  Constants.PERMISSIONS[1]); //写
            int permissionCamera = ActivityCompat.checkSelfPermission(activity, Constants.PERMISSIONS[2]); //相机
            if (permissionRead != PackageManager.PERMISSION_GRANTED ||
                    permissionWrite != PackageManager.PERMISSION_GRANTED ||
                    permissionCamera != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity,Constants.PERMISSIONS,Constants.REQUEST_EXTERNAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
