package com.example.a33626.endhomework2.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUrlConnectionUtil {
    private HttpURLConnection connection;
    private URL url; //连接url
    private StringBuilder resultInfo; //返回信息
    private BufferedWriter writer; //字符写入流
    private BufferedReader reader; //字符读取流
    public HttpUrlConnectionUtil(){
    }
    public String post(String strUrl,JSONObject params){
        try {
            this.url = new URL(strUrl);
            this.resultInfo = new StringBuilder();
            this.connection = (HttpURLConnection)this.url.openConnection();
            this.connection.setRequestMethod("POST");
            this.connection.setReadTimeout(6000);
            this.connection.setConnectTimeout(6000);
            this.connection.setDoInput(true);
            this.connection.setDoOutput(true);
            this.connection.setUseCaches(false);
            this.connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            this.connection.connect();
            this.writer = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));
            this.writer.write(params.toString());
            System.out.println(params.toString());
            this.writer.flush();//刷新缓存
            this.writer.close();//关闭写入流
            if (this.connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                this.reader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
                String tempStr;
                while ((tempStr = this.reader.readLine()) != null){
                    this.resultInfo.append(tempStr);
                }
                //关闭读取流
                this.reader.close();
            }
            else{
                return "Connection ERROR:" + this.connection.getResponseCode();
            }
            //关闭连接
            this.connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.resultInfo.toString();
    }
    public String get(String strUrl){
        try {
            this.url = new URL(strUrl);
            this.resultInfo = new StringBuilder();
            this.connection = (HttpURLConnection)this.url.openConnection();
            this.connection.setRequestMethod("GET");
            this.connection.setReadTimeout(6000);
            this.connection.setConnectTimeout(6000);
            this.connection.setDoInput(true);
            this.connection.connect();
            if (this.connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                this.reader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
                String tempStr;
                while ((tempStr = this.reader.readLine()) != null){
                    this.resultInfo.append(tempStr);
                }
                //关闭读取流
                this.reader.close();
            }
            else{
                return "Connection ERROR:" + this.connection.getResponseCode();
            }
            //关闭连接
            this.connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.resultInfo.toString();
    }



}
