package com.example.a33626.endhomework2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Friend;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.task.AddFriendTask;
import com.example.a33626.endhomework2.task.FindDisAddFriendTask;
import com.example.a33626.endhomework2.toast.MyToast;
import com.example.a33626.endhomework2.utils.ImageUtil;
import com.example.a33626.endhomework2.utils.MyThreadPool;

public class AddFriendDialog extends Dialog implements AllActivityBasicTask {

    private LinearLayout addFriendLinearLayoutSearchInfo;
    private EditText addFriendEditTextSearch;
    private ImageView addFriendImageViewHeadPortrait;
    private TextView addFriendTextViewUserId;
    private TextView addFriendTextViewUserName;
    private TextView addFriendTextViewBirthdayMonth;
    private TextView addFriendTextViewBirthdayDay;
    private Button addFriendButtonAdd;
    private Handler addFriendHandler;
    private Handler friendHandler;
    private FindDisAddFriendTask findDisAddFriendTask;
    private AddFriendTask addFriendTask;
    private MyThreadPool myThreadPool;
    private Integer userId;
    private MyToast myToast;
    private Context context;
    public AddFriendDialog(Context context, View layout,Integer userId,Handler friendHandler) {

        //构造函数初始化
        super(context, R.style.AddFriendDialogStyle);
        this.context = context;
        setContentView(layout);
        this.userId = userId;
        this.friendHandler = friendHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        startAllEvent();
    }

    @Override
    public void init() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        this.addFriendLinearLayoutSearchInfo = findViewById(R.id.addfriend_linearlayout_searchinfo);
        this.addFriendEditTextSearch = findViewById(R.id.addfriend_edittext_search);
        this.addFriendLinearLayoutSearchInfo.setVisibility(View.INVISIBLE);
        this.addFriendImageViewHeadPortrait = findViewById(R.id.addfriend_imageview_headportrait);
        this.addFriendTextViewUserId = findViewById(R.id.addfriend_textview_userid);
        this.addFriendTextViewUserName = findViewById(R.id.addfriend_textview_username);
        this.addFriendTextViewBirthdayMonth = findViewById(R.id.addfriend_textview_birthdaymonth);
        this.addFriendTextViewBirthdayDay = findViewById(R.id.addfriend_textview_birthdayday);
        this.addFriendButtonAdd = findViewById(R.id.addfriend_button_add);
        this.myThreadPool = MyThreadPool.getInstance();
        this.addFriendHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case Constants.FIND_DIS_ADD_FRIEND:
                        User user = (User)msg.getData().getSerializable(Constants.USER_INFO);
                        if (user != null){
                            addFriendTextViewUserId.setText(user.getUserId() + "");
                            addFriendTextViewUserName.setText(user.getUserName());
                            addFriendTextViewBirthdayMonth.setText(user.getBirthdayMonth());
                            addFriendTextViewBirthdayDay.setText(user.getBirthdayDay());
                            if (user.getStringHeadPortrait() != null){
                                addFriendImageViewHeadPortrait.setImageBitmap(ImageUtil.blobToBitMap(ImageUtil.base64ToImage(user.getStringHeadPortrait())));
                            }
                            else{
                                addFriendImageViewHeadPortrait.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.emoji));
                            }
                            addFriendLinearLayoutSearchInfo.setVisibility(View.VISIBLE);
                        }
                        else{
                            addFriendLinearLayoutSearchInfo.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case Constants.ADD_FRIEND:
                        //消息送到 说明添加的数据插入成功
                        showMyToast(Constants.SEND_ADD_FRIEND_REQUEST_SUCCEEDED,Constants.CORRECT);
                        addFriendEditTextSearch.setText("");
                        //给friendFragment主线程发送更新好友列表消息
                        Friend friend = new Friend();
                        friend.setUserId(userId);
                        friend.setFriendId(Integer.valueOf(addFriendTextViewUserId.getText().toString()));
                        friend.setFriendName(addFriendTextViewUserName.getText().toString());
                        friend.setFriendBirthdayMonth(addFriendTextViewBirthdayMonth.getText().toString());
                        friend.setFriendBirthdayDay(addFriendTextViewBirthdayDay.getText().toString());
                        //friend.setFriendHeadPortrait(((BitmapDrawable)addFriendImageViewHeadPortrait.getDrawable()).getBitmap());
                        friend.setMeIsAccept(1);
                        friend.setFriendIsAccept(0);
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.FRIEND_INFO,friend);
                        message.setData(bundle);
                        message.what = Constants.FIND_FRIEND_LIST_2;
                        friendHandler.sendMessage(message);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        this.addFriendTask = new AddFriendTask(this.addFriendHandler);
    }

    @Override
    public void startAllEvent() {

        //查询未添加好友事件监听
        this.addFriendEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (addFriendEditTextSearch.getText().length() > 0){
                    if (userId != Integer.valueOf(addFriendEditTextSearch.getText().toString())){
                        findDisAddFriendTask = new FindDisAddFriendTask(addFriendHandler);
                        findDisAddFriendTask.init(userId,Integer.valueOf(addFriendEditTextSearch.getText().toString()));
                        myThreadPool.execute(findDisAddFriendTask);
                    }
                }
                else{
                    addFriendLinearLayoutSearchInfo.setVisibility(View.INVISIBLE);
                }

            }
        });

        //添加好友事件监听
        this.addFriendButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendTask.init(userId,Integer.valueOf(addFriendEditTextSearch.getText().toString()));
                myThreadPool.execute(addFriendTask);
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
}
