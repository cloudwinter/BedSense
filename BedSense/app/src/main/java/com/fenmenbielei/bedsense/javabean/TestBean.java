package com.fenmenbielei.bedsense.javabean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/22 0022.
 */

public class TestBean extends JSONObject implements Serializable {
    private String title;
    private String address;
    private Boolean  isShuShi;
    private Boolean  isTriMix4;
    private Boolean  isLQ;
    private Boolean  isIQ;
    private Boolean  isKQ;
    private Boolean  isMQ;
    private Boolean  isKQH;
    private Boolean  isS4=false;
    private boolean connected;
    public void setIsIQ(Boolean isIQ) {
        this.isIQ = isIQ;
    }

    public boolean isIQ() {
        return isIQ;
    }
    public void setIsS4(Boolean isS4) {
        this.isS4 = isS4;
    }

    public boolean isS4() {
        return isS4;
    }
    public void setIsMQ(Boolean isMQ) {
        this.isMQ = isMQ;
    }

    public boolean isMQ() {
        return isMQ;
    }

    public void setIsKQ(Boolean isKQ) {
        this.isKQ = isKQ;
    }

    public boolean isKQ() {
        return isKQ;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isShuShi() {
        return isShuShi;
    }

    public void setIsShuShi(boolean isShuShi) {
        this.isShuShi = isShuShi;
    }

    public boolean isTriMix4() {
        return isTriMix4;
    }

    public void setIsTriMix4(boolean isTriMix4) {
        this.isTriMix4 = isTriMix4;
    }
    public boolean isLQ() {
        return isLQ;
    }

    public void setIsLQ(boolean isLQ) {
        this.isLQ = isLQ;
    }
    public boolean isKQH() {
        return isKQH;
    }

    public void setIsKQH(boolean isKQH) {
        this.isKQH = isKQH;
    }
    @Override
    public String toString() {
        return "TestBean{" +
                "title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", connected=" + connected +
                '}';
    }
}
