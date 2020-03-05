package com.example.a33626.endhomework2.task;

import android.os.Handler;
import android.os.Message;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Chat;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;

public class SendMessageTask implements Runnable {
    private Chat chat;
    private Handler chatHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public SendMessageTask(Handler chatHandler) {
        this.chatHandler = chatHandler;
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }
    @Override
    public void run() {
        //这个是放到线程池里会自动执行的方法  也就是相当于 thread{ run(){}}.start();
        //这里就是http访问  这个任务类里明显是  post方法访问  数据的 传输格式 application/json
        //我自己用java反射写了一个 实体类(javabean/pojo)和json之间 相互转换的工具类
        String resultInfo = this.httpUrlConnectionUtil.post(Constants.BASE_URL + "/chat/sendMessage",JsonUtil.objectToJson(chat,Chat.class));
        Message message = new Message();
        message.what = Constants.SEND_MESSAGE;
        if (resultInfo.equals("2")){
            //影响两条说明插入成功
            this.chatHandler.sendMessage(message);
        }
        else {
            //插入失败
            //具体逻辑懒得写 没有营养 直接return 了
            return;//结束任务
        }
    }

    public void init(Chat chat){
        this.chat = chat;
    }
}
