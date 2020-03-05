package com.example.a33626.endhomework2.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtil {


    /**
     *
     * blob转bitmap
     */

    public static Bitmap blobToBitMap(byte[] image){
        if (image == null){
            return null;
        }
        return BitmapFactory.decodeByteArray(image,0,image.length);
    }

    /**
     *
     * bitmap转blob
     */
    public static byte[] bitMapToBlob(Bitmap image){
        if (image == null){
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,30,byteArrayOutputStream);
            byte[] bImage = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return bImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(byte[] image){
        String result = null;
        try{
            //用默认的编码格式进行编码
            result = Base64.encodeToString(image,Base64.NO_CLOSE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将Base64编码转换为图片
     */
    public static byte[] base64ToImage(String base64Str) {
        byte[] data = Base64.decode(base64Str,Base64.NO_WRAP);
        for (int i = 0; i < data.length; i++) {
            if(data[i] < 0){
                //调整异常数据
                data[i] += 256;
            }
        }
        return data;
    }

    public static Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 480f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }


}
