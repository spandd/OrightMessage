package com.example.a33626.endhomework2.services;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.activitys.ChatActivity;
import com.example.a33626.endhomework2.activitys.IndexActivity;
import com.example.a33626.endhomework2.activitys.MainActivity;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Chat;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.sqllite.SqlUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;
import com.example.a33626.endhomework2.utils.MyThreadPool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class SocketService extends Service {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private MyThreadPool myThreadPool;
    private Runnable runnable;
    private String moduleName = "";
    private User user;
    //构造方法里去访问 服务器进行连接
    //并获取I/O  进行数据的发送和接受
    public SocketService() {
       this. myThreadPool = MyThreadPool.getInstance();
       this.runnable = new Runnable() {
           @Override
           public void run() {
               try {
                   socket = new Socket(Constants.SERVER_ADDRESS,Constants.SERVER_PORT);
                   reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                   writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                   sendMessage(String.valueOf(user.getUserId()));
                   while (true){
                       if (!socket.isClosed()) {//如果服务器没有关闭
                           if (socket.isConnected()) {//连接正常
                               if (!socket.isInputShutdown()) {//如果输入流没有断开
                                   String message = receiveMessage();
                                   if (message != null){
                                       if (message.equals(Constants.MODULE_CHAT) ||
                                               message.equals(Constants.MODULE_FRIEND)||
                                               message.equals(Constants.MODULE_MESSAGE)){
                                           moduleName = message;
                                           continue;
                                       }
                                   }
                                   if (!moduleName.equals("")){
                                       int appStatus = isAppAlive(SocketService.this);
                                       if (moduleName.equals(Constants.MODULE_CHAT)){
                                           Chat chat = (Chat)JsonUtil.jsonToObject(message,Chat.class);
                                           if (appStatus != Constants.APP_IN_STACK_TOP || !screenIsClosed()){
                                               setNotification(chat);
                                           }
                                           if (appStatus == Constants.APP_IN_STACK || appStatus == Constants.APP_IN_STACK_TOP){
                                               //这里就是 这个分支 是消息广播发送 的分支
                                               Intent intent = new Intent();
                                               intent.putExtra(Constants.CHAT_INFO, chat);
                                               intent.setAction(Constants.MESSAGE_ACTION);
                                               //发送消息广播
                                               sendBroadcast(intent);
                                           }

                                       }
                                       else if (moduleName.equals(Constants.MODULE_FRIEND)){
                                           Intent intent = new Intent();
                                           intent.setAction(Constants.FRIEND_ACTION);
                                           sendBroadcast(intent);
                                       }
                                       else if (moduleName.equals(Constants.MODULE_MESSAGE)){

                                       }
                                        moduleName = "";
                                   }
                               }
                           }
                       }

                   }

               } catch (IOException e) {
                   closeConnection();
                   e.printStackTrace();
               }
           }
       };

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SqlUtil sqlUtil = new SqlUtil(this);
        this.user = sqlUtil.selectUser();
        myThreadPool.execute(runnable);
        return START_STICKY;
    }


    /**
     * 发送数据
     */
    public void sendMessage(String msg) {
        this.writer.println(msg);
        this.writer.flush();
    }

    /**
     * 接受数据
     */
    public String receiveMessage() {
        try {
            return this.reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭连接
     * 一把不卸载程序是不会停止的
     */
    public void closeConnection(){
        try {
            if (this.writer != null){
                this.writer.close();
            }
            if (this.reader != null){
                this.reader.close();
            }
            if (this.socket.isInputShutdown()){
                this.socket.shutdownInput();
            }
            if (this.socket.isOutputShutdown()){
                this.socket.shutdownOutput();
            }
            if (this.socket.isConnected()){
                this.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //设置通知栏消息样式
    private void setNotification(Chat chat) {
        if (chat.getChatType() == 1){
            chat.setChatContent("[图片]");
        }
        else if (chat.getChatType() == 2){
            chat.setChatContent("[定位]");
        }
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("chatChannel", "message", importance);
            manager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.edittext_search)
                .setContentTitle(chat.getFriendId() + "")
                .setAutoCancel(true)
                .setVibrate(new long[]{500, 500})
                .setOnlyAlertOnce(true)
                .setContentText(chat.getChatContent())
                .setChannelId("chatChannel")
                .setPriority(NotificationManager.IMPORTANCE_HIGH);


        int mNotificationId = (int) System.currentTimeMillis();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra(Constants.USER_INFO,);
//        intent.putExtra(Constants.FRIEND_INFO,);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, mNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        manager.notify(mNotificationId, builder.build());


        PowerManager powerManager = (PowerManager) SocketService.this.getSystemService(Context.POWER_SERVICE);
        boolean screenOn = powerManager.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK,"Message:SocketService");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
        KeyguardManager keyguardManager = (KeyguardManager) SocketService.this.getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        keyguardLock.disableKeyguard();
    }


    /**
     * 判断屏幕是否关闭
     */

    public boolean screenIsClosed(){
        PowerManager powerManager = (PowerManager) SocketService.this.getSystemService(Context.POWER_SERVICE);
        return powerManager.isScreenOn();
    }

    /**
     * 判断程序是不是在运行
     */
    public static int isAppAlive(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appAliveList = activityManager.getRunningTasks(20);
        // 判断程序是否在栈顶
        if (appAliveList.get(0).topActivity.getPackageName().equals(context.getPackageName())) {
            return Constants.APP_IN_STACK_TOP;
        }
        else {
            // 判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : appAliveList) {
                if (info.topActivity.getPackageName().equals(context.getPackageName())) {
                    return Constants.APP_IN_STACK;
                }
            }
            return Constants.APP_DIE;// 栈里找不到
        }
    }

}
