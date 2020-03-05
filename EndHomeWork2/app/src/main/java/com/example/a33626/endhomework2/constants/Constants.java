package com.example.a33626.endhomework2.constants;

public class Constants {
    /**
     *
     *
     * 这个是常量表 所有用到的常量都在这里面
     */
    //线程池模块常量

    public static final int DEFAULT_CORE_SIZE = 20; //核心线程数
    public static final int MAX_QUEUE_SIZE = 20; //最大县城数

    //接口连接模块常量
    public static final String BASE_URL = "http://101.200.91.136:80/";
    public static final int LOGIN_INFO_USER_NAME_NOT_FOUND_CODE = 0;
    public static final int LOGIN_INFO_PASSWORD_ERROR_CODE = 1;
    public static final int LOGIN_INFO_SUCCESS_CODE = 2;
    public static final int REGISTER_INFO_USER_NAME_REPEAT_CODE = 0;
    public static final int REGISTER_INFO_USER_NAME_NOT_REPEAT_CODE = 1;
    public static final int REGISTER_INFO_ERROR_CODE = 2;
    public static final int REGISTER_INFO_SUCCESS_CODE = 3;
    public static final String REGISTER_INFO_REPASSWORD_ERROR = "RePassword is not equal to password";
    public static final String SOURCE_ACTIVITY = "source_activity";
    public static final String DESTINATION_ACTIVITY = "destination_activity";
    public static final String BASE_INFO = "base_info";
    public static final String USER_INFO = "user_info";
    public static final String FRIEND_INFO = "friend_info";
    public static final String CHAT_INFO = "chat_info";
    public static final String CHAT_INFO_LIST = "chat_info_list";
    public static final String FRIEND_INFO_LIST = "friend_info_list";
    public static final String MESSAGE_INFO_LIST = "message_info_list";
    public static final String LOCATION_INFO = "location_info";
    public static final String ERROR = "ERROR";
    public static final String CORRECT = "CORRECT";

    //socket服务模块常量
    public static final String SERVER_ADDRESS = "101.200.91.136";
    public static final int SERVER_PORT = 8866;
    public static final String MODULE_CHAT = "chat";
    public static final String MODULE_FRIEND = "friend";
    public static final String MODULE_MESSAGE = "message";
    public static final int APP_IN_STACK_TOP = 0;
    public static final int APP_IN_STACK = 1;
    public static final int APP_DIE = 2;

    //friend模块常量
    public static final int FIND_DIS_ADD_FRIEND = 0;
    public static final int ADD_FRIEND = 1;
    public static final int FIND_FRIEND_LIST = 2;
    public static final int FIND_FRIEND_LIST_2 = 3;
    public static final int ACCEPT_ADD_FRIEND_REQUEST = 4;
    public static final int DELETE_FRIEND = 5;
    public static final int RECEIVE_FRIEND= 6;
    public static final String SEND_ADD_FRIEND_REQUEST_SUCCEEDED = "Send add friend request succeeded";
    public static final String ACCEPT_ADD_FRIEND_REQUEST_SUCCEEDED = "Accept add friend request succeeded";
    public static final String DELETE_FRIEND_SUCCEEDED = "Delete friend succeeded";
    //主模块常量

    public static final int FIND_USER_BASE_INFO = 0;

    //聊天模块常量
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TRUES = 1;
    public static final int FALSES = 0;
    //------------------handler--------------------
    public static final int FIND_CHAT_LIST = 0;
    public static final int SEND_MESSAGE = 1;
    public static final int RECEIVE_MESSAGE = 2;
    //--------------------------------------------
    public static final String SEND_CONTENT_NOT_NULL = "The send content can't null";
    public static final int CAMERA_PHOTO_REQUEST_CODE = 2;
    public static final int LOCATION_REQUEST_CODE = 3;
    //--------------------------------------------
    public static final int CHAT_TYPE_COMMON = 0;
    public static final int CHAT_TYPE_PHOTO = 1;
    public static final int CHAT_TYPE_LOCATION = 2;

    //消息模块常量
    public static final int FIND_MESSAGE_LIST = 0;



    //广播常量
    public static final String MESSAGE_ACTION = "message_Action";
    public static final String FRIEND_ACTION = "friend_Action";
    public static int IS_UPDATE = 0;

    // 权限
    public static final int REQUEST_EXTERNAL= 1;
    public static final String[] PERMISSIONS = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMERA",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"};

    //广场模块常量
    public static final int SETTING_ALBUM_REQUEST_CODE = 2;
    public static final int SETTING_CORP_REQUEST_CODE = 3;
    public static final int EDIT_USER_HEAD_PORTRAIT = 0;

}
