package com.example.a33626.endhomework2.task;

import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;

public class AcceptAddFriendRequestTask implements Runnable{
    private Integer userId;
    private Integer friendId;
    private Handler friendHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public AcceptAddFriendRequestTask(Handler friendHandler) {
        this.friendHandler = friendHandler;
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.get(Constants.BASE_URL + "/friend/acceptAddFriendRequest?userId=" + this.userId+ "&friendId=" + this.friendId);
        if (resultInfo.equals("2")){
            Message message = new Message();
            message.what = Constants.ACCEPT_ADD_FRIEND_REQUEST;
            this.friendHandler.sendMessage(message);
        }
        else{
            return;
        }
    }

    public void init(Integer userId,Integer friendId){
        this.userId = userId;
        this.friendId = friendId;
    }
}
