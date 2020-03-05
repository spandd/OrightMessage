package com.example.a33626.endhomework2.models;

import java.io.Serializable;

public class BaseInfo implements Serializable {
    //这些是所有的实体类 都 实现类 序列化接口  可以进行二进制传输
    private Integer loginStatus;
    private Integer registerStatus;
    private String loginInfo;
    private String registerInfo;
    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Integer getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(Integer registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(String loginInfo) {
        this.loginInfo = loginInfo;
    }

    public String getRegisterInfo() {
        return registerInfo;
    }

    public void setRegisterInfo(String registerInfo) {
        this.registerInfo = registerInfo;
    }
}
