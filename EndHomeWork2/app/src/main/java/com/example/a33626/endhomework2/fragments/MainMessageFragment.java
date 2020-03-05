package com.example.a33626.endhomework2.fragments;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.activitys.ChatActivity;
import com.example.a33626.endhomework2.adapters.MessageAdapter;
import com.example.a33626.endhomework2.broadcast.MessageReceiver;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Chat;
import com.example.a33626.endhomework2.models.Friend;
import com.example.a33626.endhomework2.models.MessageInfo;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.sqllite.SqlUtil;
import com.example.a33626.endhomework2.task.FindMessageListTask;
import com.example.a33626.endhomework2.utils.MyThreadPool;

import java.util.ArrayList;
import java.util.List;


public class MainMessageFragment extends Fragment implements AllActivityBasicTask {

    private User user;
    private ListView messageListView;
    private List<MessageInfo> messageInfos;
    private MessageAdapter messageAdapter;
    private SwipeRefreshLayout messageSwipeRefreshLayout;
    private MyThreadPool myThreadPool;
    private Handler messageHandler;
    private FindMessageListTask findMessageListTask;
    private MessageReceiver messageReceiver; //消息接收器
    private SqlUtil sqlUtil;
    public MainMessageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_message, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (sqlUtil != null){
            this.user = sqlUtil.selectUser();
        }

        if (getUserVisibleHint()){
            if (findMessageListTask != null && Constants.IS_UPDATE == 1){
                this.findMessageListTask = new FindMessageListTask(messageHandler);
                this.findMessageListTask.init(user.getUserId());
                this.myThreadPool.execute(findMessageListTask);
                Constants.IS_UPDATE = 0;
            }
        }
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
        this.messageListView = getActivity().findViewById(R.id.message_listview_messagelist);
        this.messageInfos = new ArrayList<>();
        this.messageSwipeRefreshLayout = getActivity().findViewById(R.id.message_swipefreshlayout);
        this.messageHandler = new Handler(){
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Constants.FIND_MESSAGE_LIST:
                        //收到查询成攻消息 更新UI
                        messageInfos = (List<MessageInfo>) msg.getData().getSerializable(Constants.MESSAGE_INFO_LIST);
                        messageAdapter = new MessageAdapter(messageInfos,getContext());
                        messageListView.setAdapter(messageAdapter);
                        break;
                    case Constants.RECEIVE_MESSAGE:
                        Chat chat = (Chat)msg.getData().getSerializable(Constants.CHAT_INFO);
                        if (chat != null){
                            for (int i = 0;i < messageInfos.size();i++){
                                if (messageInfos.get(i).getFriendId() == Integer.valueOf(chat.getUserId())){
                                    messageInfos.get(i).setChatContent(chat.getChatContent());
                                    messageAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                        break;
                }
            }
        };
        this.myThreadPool = MyThreadPool.getInstance();
        this.findMessageListTask = new FindMessageListTask(messageHandler);
        this.findMessageListTask.init(user.getUserId());
        this.myThreadPool.execute(findMessageListTask);
        //注册消息接收广播
        this.messageReceiver = new MessageReceiver(this.messageHandler);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MESSAGE_ACTION);
        //注册广播
        getActivity().registerReceiver(messageReceiver,intentFilter);
    }

    @Override
    public void startAllEvent() {
        this.messageSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messageSwipeRefreshLayout.setRefreshing(false);
                    }
                },500);
            }
        });

        this.messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = new Friend();
                friend.setUserId(user.getUserId());
                friend.setFriendId(messageInfos.get(position).getFriendId());
                friend.setFriendName(messageInfos.get(position).getFriendName());
                friend.setFriendHeadPortrait(messageInfos.get(position).getFriendHeadPortrait());
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                intent.putExtra(Constants.USER_INFO,user);
                intent.putExtra(Constants.FRIEND_INFO,friend);
                getActivity().startActivity(intent);
            }
        });
    }
}
