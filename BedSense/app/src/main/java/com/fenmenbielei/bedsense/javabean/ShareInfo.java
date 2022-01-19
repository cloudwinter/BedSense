package com.fenmenbielei.bedsense.javabean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 分享信息
 */
public class ShareInfo implements Parcelable {

    public String title;
    public String text;
    public String targetUrl;
    public String imgUrl;

    public ShareInfo() {

    }

    protected ShareInfo(Parcel in) {
        title = in.readString();
        text = in.readString();
        targetUrl = in.readString();
        imgUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(targetUrl);
        dest.writeString(imgUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShareInfo> CREATOR = new Creator<ShareInfo>() {
        @Override
        public ShareInfo createFromParcel(Parcel in) {
            return new ShareInfo(in);
        }

        @Override
        public ShareInfo[] newArray(int size) {
            return new ShareInfo[size];
        }
    };
}
