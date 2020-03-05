package com.example.a33626.endhomework2.activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.adapters.ChatAdapter;
import com.example.a33626.endhomework2.adapters.EmojiAdapter;
import com.example.a33626.endhomework2.broadcast.MessageReceiver;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.commoninterface.GridViewCallBack;
import com.example.a33626.endhomework2.commoninterface.ListViewCallBack;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Chat;
import com.example.a33626.endhomework2.models.Emoji;
import com.example.a33626.endhomework2.models.Friend;
import com.example.a33626.endhomework2.models.Location;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.sqllite.SqlUtil;
import com.example.a33626.endhomework2.task.FindChatListTask;
import com.example.a33626.endhomework2.task.SendMessageTask;
import com.example.a33626.endhomework2.toast.MyToast;
import com.example.a33626.endhomework2.utils.ImageUtil;
import com.example.a33626.endhomework2.utils.MyThreadPool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity implements AllActivityBasicTask,GridViewCallBack,ListViewCallBack {

    private ListView chatListViewContent;
    private TextView chatTextViewFriendName;
    private Button chatButtonSend;
    private EditText chatEditTextChatContent;
    private User user;
    private Friend friend;
    private List<Chat> chats;
    private Handler chatHandler;
    private MyThreadPool myThreadPool;
    private FindChatListTask findChatListTask;
    private SendMessageTask sendMessageTask;
    private ChatAdapter chatAdapter;
    private MyToast myToast;
    private MessageReceiver messageReceiver;
    private List<Emoji> emojis;
    private EmojiAdapter emojiAdapter;
    private GridView chatGridLayoutEmojiContainer;
    private LinearLayout chatLinearLayoutEmojiContainer;
    private ImageView chatImageViewCamera;
    private ImageView chatImageViewEmoji;
    private ImageView chatImageViewLocal;
    private Location locationData;
    private SqlUtil sqlUtil;
    private Uri photoUri;
    private String path = Environment.getExternalStorageDirectory() +
            File.separator + Environment.DIRECTORY_DCIM + File.separator; //照片存取路径
    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        startAllEvent();
    }

    @Override
    public void init() {
        Constants.IS_UPDATE = 1;
        this.sqlUtil = new SqlUtil(this);
        Intent intent = getIntent();
        //初始化基础信息
        this.user = this.sqlUtil.selectUser();
        this.friend = (Friend) intent.getSerializableExtra(Constants.FRIEND_INFO);
        this.chats = new ArrayList<>();
        this.chatListViewContent = findViewById(R.id.chat_listview_content);
        this.chatTextViewFriendName = findViewById(R.id.chat_textview_friendname);
        this.chatTextViewFriendName.setText(this.friend.getFriendName());
        this.chatButtonSend = findViewById(R.id.chat_button_send);
        this.chatEditTextChatContent = findViewById(R.id.chat_edittext_chatcontent);
        this.chatGridLayoutEmojiContainer = findViewById(R.id.chat_gridview_emojicontainer);
        this.chatLinearLayoutEmojiContainer = findViewById(R.id.chat_linearlayout_emojicontainer);
        this.chatImageViewCamera = findViewById(R.id.chat_imageview_camera);
        this.chatImageViewEmoji = findViewById(R.id.chat_imageview_emoji);
        this.chatImageViewLocal = findViewById(R.id.chat_imageview_local);
        //表情信息
        initEmojis();
        emojiAdapter = new EmojiAdapter(emojis,ChatActivity.this,ChatActivity.this);
        chatGridLayoutEmojiContainer.setAdapter(emojiAdapter);
        this.chatHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    //在这里  这里就是接受各种 子线程 访问完发来的数据
                    case Constants.FIND_CHAT_LIST:
                        chats = (List<Chat>) msg.getData().getSerializable(Constants.CHAT_INFO_LIST);
                        chatAdapter = new ChatAdapter(chats,friend,user,ChatActivity.this,ChatActivity.this);
                        chatListViewContent.setAdapter(chatAdapter);
                        chatListViewContent.setSelection(chats.size());
                        break;
                    case Constants.SEND_MESSAGE:
                        //发送消息成功 更新UI
                        chatAdapter.notifyDataSetChanged();
                        chatEditTextChatContent.setText("");
                        chatListViewContent.setSelection(chats.size());
                        break;
                    case Constants.RECEIVE_MESSAGE:
                        Chat chat = (Chat)msg.getData().getSerializable(Constants.CHAT_INFO);
                        if (chat != null){
                            if (Integer.valueOf(friend.getFriendId()) == Integer.valueOf(chat.getUserId())){
                                chat.setUserId(user.getUserId());
                                chat.setFriendId(friend.getFriendId());
                                chat.setIsLast(Constants.TRUES);
                                chat.setIsRead(Constants.TRUES);
                                chat.setIsMe(Constants.FALSES);
                                chats.add(chat);
                                chatAdapter.notifyDataSetChanged();
                                chatListViewContent.setSelection(chats.size());
                            }
                        }

                        break;
                }
            }
        };
        /**
         * 1.首先会初始化线程池
         * 2.然后会初始化 handler 和 对应的 task
         * 每个task都会执行封装好的业务逻辑
         * 3.在关键的时候会触发监听 或者是 在生命周期的某一时刻来进行任务执行
         * 4.然后 在任务类里会把返回的数据 发送到主线程 有 handler的消息队列接受
         *
         */
        this.myThreadPool = MyThreadPool.getInstance();
        this.findChatListTask = new FindChatListTask(this.chatHandler);
        this.findChatListTask.init(this.friend);
        this.myThreadPool.execute(findChatListTask);
        this.messageReceiver = new MessageReceiver(this.chatHandler);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MESSAGE_ACTION);
        //注册广播
        registerReceiver(messageReceiver,intentFilter);
    }

    @Override
    public void startAllEvent() {

        //发送消息监听
        this.chatButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断
                if (chatEditTextChatContent.getText().length() > 0){
                    Chat chat = newChat();
                    chat.setChatContent(chatEditTextChatContent.getText().toString());
                    //发送普通消息
                    chat.setChatType(Constants.CHAT_TYPE_COMMON);
                    sendMessageTask = new SendMessageTask(chatHandler);
                    sendMessageTask.init(chat);
                    chats.add(chat);
                    //比如说这里  这里就是 执行的一个 任务 用来进行 接口的访问
                    //看名字可以看出 这是 信息发送的任务
                    myThreadPool.execute(sendMessageTask);
                }
                else{
                    showMyToast(Constants.SEND_CONTENT_NOT_NULL,Constants.ERROR);
                }
            }
        });

        this.chatEditTextChatContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.chatImageViewEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatLinearLayoutEmojiContainer.getVisibility() == View.GONE){
                    chatLinearLayoutEmojiContainer.setVisibility(View.VISIBLE);
                    chatListViewContent.setSelection(chats.size());
                }
                else{
                    chatLinearLayoutEmojiContainer.setVisibility(View.GONE);
                }
            }
        });


        this.chatImageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相机
                PackageManager pm = getPackageManager();
                if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                    //没有相机
                    return;
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //判断图片存储的路径存不存在
                File sdPath = new File(path);
                if (!sdPath.exists()){
                    sdPath.mkdir(); //路径不存在创建路径
                }
                imagePath = path + "chat_img_" + UUID.randomUUID() + ".jpg";
                File photoOutputFile = new File(imagePath);
                //根据路径创建图片URI
                if (Build.VERSION.SDK_INT >= 24) {
                    //因为Google更新 所以得用FileProvider创建URi
                    photoUri = FileProvider.getUriForFile(ChatActivity.this,
                            ChatActivity.this.getPackageName() + ".fileprovider",
                            photoOutputFile);
                } else {
                    //从文件中创建uri
                    photoUri = Uri.fromFile(photoOutputFile);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                //调用相机
                startActivityForResult(intent,Constants.CAMERA_PHOTO_REQUEST_CODE);
            }
        });

        this.chatImageViewLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用定位
                Intent intent = new Intent(ChatActivity.this,LocationActivity.class);
                startActivityForResult(intent,Constants.LOCATION_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.CAMERA_PHOTO_REQUEST_CODE) {
            //压缩
            Bitmap photo = ImageUtil.compressImageFromFile(imagePath);
            String strPhoto = ImageUtil.imageToBase64(ImageUtil.bitMapToBlob(photo));
            Chat clientChat = newChat(); //前端显示
            clientChat.setChatType(Constants.CHAT_TYPE_PHOTO);
            Chat serverChat = newChat(); //后端存储
            serverChat.setChatType(Constants.CHAT_TYPE_PHOTO);
            clientChat.setChatPhotoB(photo);
            serverChat.setChatPhotoS(strPhoto);
            chats.add(clientChat);
            //发送请求
            sendMessageTask = new SendMessageTask(chatHandler);
            sendMessageTask.init(serverChat);
            myThreadPool.execute(sendMessageTask);
        }
        else if (requestCode == Constants.LOCATION_REQUEST_CODE){
            locationData = (Location) data.getSerializableExtra(Constants.LOCATION_INFO);
            Chat chat = newChat();
            //定位信息
            chat.setChatType(Constants.CHAT_TYPE_LOCATION);
            chat.setChatAccuracy(locationData.getmAccuracy());
            chat.setChatDirection(locationData.getmDirection());
            chat.setChatLatitude(locationData.getmLatitude());
            chat.setChatLongitude(locationData.getmLongitude());
            chats.add(chat);
            //发送请求
            sendMessageTask = new SendMessageTask(chatHandler);
            sendMessageTask.init(chat);
            myThreadPool.execute(sendMessageTask);
        }
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(this.messageReceiver);
        super.onDestroy();
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

    public void initEmojis(){
        this.emojis = new ArrayList<>();
        int[] emojisIds = {
                R.drawable.e2,
                R.drawable.e3,
                R.drawable.e4,
                R.drawable.e5,
                R.drawable.e6,
                R.drawable.e7,
                R.drawable.e8,
                R.drawable.e9,
                R.drawable.e10,
                R.drawable.e11,
                R.drawable.e12,
                R.drawable.e13,
                R.drawable.e14,
                R.drawable.e15,
                R.drawable.e16,
                R.drawable.e17,
                R.drawable.e18,
                R.drawable.e19,
                R.drawable.e20,
                R.drawable.e21,
                R.drawable.e22,
                R.drawable.e23,
                R.drawable.e24
        };
        String[] emojiNames = getResources().getStringArray(R.array.default_emoji_key);
        for (int i = 0;i < emojiNames.length;i++){
            Emoji emoji = new Emoji();
            emoji.setEmojiName(emojiNames[i]);
            emoji.setEmojiId(emojisIds[i]);
            this.emojis.add(emoji);
        }
    }

    public Chat newChat(){
        Chat chat = new Chat();
        chat.setUserId(user.getUserId());
        chat.setFriendId(friend.getFriendId());
        chat.setIsLast(Constants.TRUES);
        chat.setIsRead(Constants.TRUES);
        chat.setIsMe(Constants.TRUES);
        return chat;
    }

    @Override
    public void listViewCallBackClick(int position) {
        Intent intent = new Intent(ChatActivity.this,ShowLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.CHAT_INFO,this.chats.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void gridViewCallBackClick(int position) {
        Emoji emoji = emojis.get(position);
        int resourceId = emoji.getEmojiId();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        ImageSpan imageSpan = new ImageSpan(ChatActivity.this, bitmap);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(emoji.getEmojiName());//图片的前缀名

        spannableStringBuilder.setSpan(imageSpan, 0, emoji.getEmojiName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (chatEditTextChatContent.getSelectionStart() == chatEditTextChatContent.getText().length()){
            chatEditTextChatContent.append(spannableStringBuilder);
        }
        else{
            int index = chatEditTextChatContent.getSelectionStart();
            Editable editable = chatEditTextChatContent.getText();
            editable.insert(index,spannableStringBuilder);
        }
    }
}
