package com.example.a33626.endhomework2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.activitys.ChatActivity;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Friend;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.task.DeleteFriendTask;
import com.example.a33626.endhomework2.toast.MyToast;
import com.example.a33626.endhomework2.utils.MyThreadPool;

public class SeeFriendDialog extends Dialog implements AllActivityBasicTask {
    private ImageView seeFriendImageViewHeadPortrait;
    private TextView seeFriendTextViewUserId;
    private TextView seeFriendTextViewUserName;
    private TextView seeFriendTextViewBirthdayMonth;
    private TextView seeFriendTextViewBirthdayDay;
    private Button seeFriendButtonMessage;
    private Button seeFriendButtonDelete;
    private Handler friendHandler;
    private MyThreadPool myThreadPool;
    private DeleteFriendTask deleteFriendTask;
    private MyToast myToast;
    private Context context;
    private Friend friend;
    private User user;

    /**
     * 这个就是自己封装dialog  继承android dialog
     */
    public SeeFriendDialog(Context context, View layout, Handler friendHandler, Friend friend, User user) {

        //构造函数初始化
        super(context, R.style.AddFriendDialogStyle);
        this.context = context;
        setContentView(layout);
        this.friendHandler = friendHandler;
        this.friend = friend;
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        startAllEvent();
    }

    @Override
    public void init() {
        //初始化
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        params.gravity = Gravity.CENTER;
        params.width = width / 4 * 3;
        params.height = height  / 2 * 1;
        window.setAttributes(params);
        this.seeFriendImageViewHeadPortrait = findViewById(R.id.seefriend_imageview_headportrait);
        this.seeFriendTextViewUserId = findViewById(R.id.seefriend_textview_userid);
        this.seeFriendTextViewUserName = findViewById(R.id.seefriend_textview_username);
        this.seeFriendTextViewBirthdayMonth = findViewById(R.id.seefriend_textview_birthdaymonth);
        this.seeFriendTextViewBirthdayDay = findViewById(R.id.seefriend_textview_birthdayday);
        this.seeFriendButtonMessage = findViewById(R.id.seefriend_button_message);
        this.seeFriendButtonDelete = findViewById(R.id.seefriend_button_delete);
        this.seeFriendTextViewUserId.setText(String.valueOf(friend.getFriendId()));
        this.seeFriendTextViewUserName.setText(friend.getFriendName());
        this.seeFriendTextViewBirthdayMonth.setText(friend.getFriendBirthdayMonth());
        this.seeFriendTextViewBirthdayDay.setText(friend.getFriendBirthdayDay());
        this.myThreadPool = MyThreadPool.getInstance();
    }

    @Override
    public void startAllEvent() {
        //所有的事件监听

        //发消息
        this.seeFriendButtonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra(Constants.USER_INFO,user);
                intent.putExtra(Constants.FRIEND_INFO,friend);
                context.startActivity(intent);
            }
        });

        //删除
        this.seeFriendButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFriendTask = new DeleteFriendTask(friendHandler);
                deleteFriendTask.init(friend.getUserId(),friend.getFriendId());
                myThreadPool.execute(deleteFriendTask);
                cancel();
            }
        });
    }
    //调用自定义toast
    private void showMyToast(String content,String type) {
        if (this.myToast != null) {
            this.myToast.hide();
        }
        this.myToast = new MyToast(context,
                (ViewGroup) this.findViewById(R.id.toast_linearlayout_parent), type);
        this.myToast.show(content,1000);
    }

    @Override
    public void cancel() {
        //cancel回掉
        super.cancel();
    }
}
