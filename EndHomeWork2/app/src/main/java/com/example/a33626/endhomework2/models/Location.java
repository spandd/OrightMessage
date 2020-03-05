package com.example.a33626.endhomework2.models;

import java.io.Serializable;

public class Location implements Serializable {
    private String mLatitude;
    private String mLongitude;
    private String mDirection;
    private String mAccuracy;

    public String getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmDirection() {
        return mDirection;
    }

    public void setmDirection(String mDirection) {
        this.mDirection = mDirection;
    }

    public String getmAccuracy() {
        return mAccuracy;
    }

    public void setmAccuracy(String mAccuracy) {
        this.mAccuracy = mAccuracy;
    }
}
