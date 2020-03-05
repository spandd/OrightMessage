package com.example.a33626.endhomework2.fragments;


import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.adapters.FriendAdapter;
import com.example.a33626.endhomework2.broadcast.FriendReceiver;
import com.example.a33626.endhomework2.commoninterface.AllActivityBasicTask;
import com.example.a33626.endhomework2.commoninterface.ListViewCallBack;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.dialog.AddFriendDialog;
import com.example.a33626.endhomework2.dialog.SeeFriendDialog;
import com.example.a33626.endhomework2.models.Friend;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.sqllite.SqlUtil;
import com.example.a33626.endhomework2.task.AcceptAddFriendRequestTask;
import com.example.a33626.endhomework2.task.FindFriendListTask;
import com.example.a33626.endhomework2.toast.MyToast;
import com.example.a33626.endhomework2.utils.MyThreadPool;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFriendsFragment extends Fragment implements AllActivityBasicTask,ListViewCallBack {

    private ListView friendListViewFriendList;
    private FriendAdapter friendAdapter;
    private AddFriendDialog addFriendDialog;
    private SeeFriendDialog seeFriendDialog;
    private LinearLayout friendLinearLayoutSearch;
    private User user;
    private Handler friendHandler;
    private MyThreadPool myThreadPool;
    private FindFriendListTask findFriendListTask;
    private AcceptAddFriendRequestTask acceptAddFriendRequestTask;
    private List<Friend> friends;
    private MyToast myToast;
    private int clickPosition;
    private FriendReceiver friendReceiver;
    private SqlUtil sqlUtil;
    public MainFriendsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_friends, container, false);
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
        this.friendListViewFriendList = getActivity().findViewById(R.id.friend_listview_friendslist);
        this.friendLinearLayoutSearch = getActivity().findViewById(R.id.friend_linearlayout_search);
        clickPosition = -1;
        this.myThreadPool = MyThreadPool.getInstance();
        this.friends = new ArrayList<>();
        this.friendHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    //查询好友列表
                    case Constants.FIND_FRIEND_LIST:
                        friends = (List<Friend>)msg.getData().getSerializable(Constants.FRIEND_INFO_LIST);
                        //更细朋友列表的UI
                        friendAdapter = new FriendAdapter(friends,getContext(),MainFriendsFragment.this);
                        friendListViewFriendList.setAdapter(friendAdapter);
                        break;
                    //更新好友列表
                    case Constants.FIND_FRIEND_LIST_2:
                        friends.add((Friend) msg.getData().getSerializable(Constants.FRIEND_INFO));
                        friendAdapter.notifyDataSetChanged();
                        break;
                    //同意好友添加申请
                    case Constants.ACCEPT_ADD_FRIEND_REQUEST:
                        showMyToast(Constants.ACCEPT_ADD_FRIEND_REQUEST_SUCCEEDED,Constants.CORRECT);
                        break;
                    case Constants.DELETE_FRIEND:
                        //删除好友消息接受
                        if (clickPosition != -1){
                            friends.remove(clickPosition);
                            friendListViewFriendList.setAdapter(friendAdapter);
                            friendAdapter.notifyDataSetChanged();
                            showMyToast(Constants.DELETE_FRIEND_SUCCEEDED,Constants.CORRECT);
                        }
                        break;
                    case Constants.RECEIVE_FRIEND:
                        findFriendListTask = new FindFriendListTask(friendHandler);
                        findFriendListTask.init(user.getUserId());
                        myThreadPool.execute(findFriendListTask);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        this.addFriendDialog = new AddFriendDialog(getContext(),getLayoutInflater().inflate(R.layout.dialog_addfriend_layout,null),user.getUserId(),this.friendHandler);
        this.findFriendListTask = new FindFriendListTask(this.friendHandler);
        this.findFriendListTask.init(user.getUserId());
        this.myThreadPool.execute(findFriendListTask);
        this.friendReceiver = new FriendReceiver(friendHandler);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.FRIEND_ACTION);
        //注册广播
        getActivity().registerReceiver(this.friendReceiver,intentFilter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (sqlUtil != null){
            this.user = sqlUtil.selectUser();
        }
        if (getUserVisibleHint()){
            if (this.findFriendListTask != null && Constants.IS_UPDATE == 1){
                this.findFriendListTask = new FindFriendListTask(this.friendHandler);
                this.findFriendListTask.init(user.getUserId());
                this.myThreadPool.execute(findFriendListTask);
                Constants.IS_UPDATE = 0;
            }

        }
    }



    @Override
    public void startAllEvent() {
        this.friendLinearLayoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendDialog.show();
                addFriendDialog.setCanceledOnTouchOutside(true);
            }
        });

        this.friendListViewFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickPosition = position;
                Friend friend = friends.get(position);
                if (friend.getMeIsAccept() == 1 && friend.getFriendIsAccept() == 1){
                    //这样说明是好友
                    seeFriendDialog = new SeeFriendDialog(getContext(),getLayoutInflater().inflate(R.layout.dailog_seefriend_layout,null),friendHandler,friend,user);
                    Constants.IS_UPDATE = 1;
                    seeFriendDialog.show();
                    seeFriendDialog.setCanceledOnTouchOutside(true);
                }
            }
        });
    }

    @Override
    public void listViewCallBackClick(int position) {
        //同意按被点击
        //初始化同意添加任务
        acceptAddFriendRequestTask = new AcceptAddFriendRequestTask(friendHandler);
        acceptAddFriendRequestTask.init(user.getUserId(),friends.get(position).getFriendId());
        //执行异步任务
        myThreadPool.execute(acceptAddFriendRequestTask);
        friends.get(position).setMeIsAccept(1);
        friendAdapter.notifyDataSetChanged();
    }

    //调用自定义toast
    private void showMyToast(String content,String type) {
        if (this.myToast != null) {
            this.myToast.hide();
        }
        this.myToast = new MyToast(getContext(),
                (ViewGroup) getActivity().findViewById(R.id.toast_linearlayout_parent), type);
        this.myToast.show(content,1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(this.friendReceiver);
    }
}
