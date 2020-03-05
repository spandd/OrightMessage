package com.example.a33626.endhomework2.task;

import android.os.Handler;
import android.os.Message;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Friend;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;

public class AddFriendTask implements Runnable{
    private Friend friend;
    private Handler addFriendHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public AddFriendTask(Handler addFriendHandler) {
        this.addFriendHandler = addFriendHandler;
        this.friend = new Friend();
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.get(Constants.BASE_URL + "/friend/addFriend?userId=" + this.friend.getUserId() + "&friendId=" + this.friend.getFriendId());
        if (resultInfo.equals("2")){
            Message message = new Message();
            message.what = Constants.ADD_FRIEND;
            this.addFriendHandler.sendMessage(message);
        }
        else{
            return;
        }
    }

    public void init(Integer userId,Integer friendId){
        this.friend.setUserId(userId);
        this.friend.setFriendId(friendId);
    }
}
