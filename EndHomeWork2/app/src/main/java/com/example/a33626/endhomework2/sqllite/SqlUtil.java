package com.example.a33626.endhomework2.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.utils.ImageUtil;

import java.sql.Blob;

public class SqlUtil {
    //这个是 sqlite 用于本地cache  没啥好说的  上学期 就学过
    private SQLiteDatabase wdb;
    private SQLiteDatabase rdb;
    private Context context;
    public SqlUtil(Context context){
        this.context = context;
        UserBaseHelper userBaseHelper = new UserBaseHelper(context);
        this.wdb = userBaseHelper.getWritableDatabase();
        this.rdb = userBaseHelper.getReadableDatabase();
    }
    /**
     * 查询数据
     * 返回List
     */
    public User selectUser() {
        User user = new User();
        String sql = "select * from message_user";
        Cursor cursor = rdb.rawQuery(sql,null);
        while (cursor.moveToNext()){
            user.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
            user.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setBirthdayMonth(cursor.getString(cursor.getColumnIndex("birthday_month")));
            user.setBirthdayDay(cursor.getString(cursor.getColumnIndex("birthday_day")));
            byte[] a = cursor.getBlob(cursor.getColumnIndex("head_portrait"));
            user.setBlobHeadPortrait(cursor.getBlob(cursor.getColumnIndex("head_portrait")));
        }
        if(cursor != null){
            cursor.close();
        }
        return user;
    }


    /**
     * 添加数据
     */
    public void insertData(User user){
        String sql = "insert into message_user(user_id,user_name,password,birthday_month,birthday_day,head_portrait) values(?,?,?,?,?,?)";
        Object[] params = new Object[6];
        params[0] = user.getUserId();
        params[1] = user.getUserName();
        params[2] = user.getPassword();
        params[3] = user.getBirthdayMonth();
        params[4] = user.getBirthdayDay();
        params[5] = user.getBlobHeadPortrait();
        this.wdb.execSQL(sql,params);
    }

    /**
     * 更新
     */
    /**
     * 添加数据
     */
    public int updateData(User user){
        ContentValues values = new ContentValues();
        values.put("user_name", user.getUserName());
        values.put("password", user.getPassword());
        values.put("birthday_month", user.getBirthdayMonth());
        values.put("birthday_day", user.getBirthdayDay());
        values.put("head_portrait", user.getBlobHeadPortrait());
        return this.wdb.update("message_user",values,"user_id=?",new String[] {String.valueOf(user.getUserId())});
    }


}
