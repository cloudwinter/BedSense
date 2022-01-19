package com.fenmenbielei.bedsense.uitls;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wnhz.shidaodianqi.R;

/**
 *  Created by wanghongchuang
 *  on 2016/8/25.
 *  email:844285775@qq.com
 */
public class MyImageLoader {

    public static final String Default_uri = "http://xiangniu.unohacha.com/Public/Home/Images/82_img.png";

    public static void displayImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView);
    }

    public static void displayImage(String uri, ImageView imageView, boolean fromSDCard) {
        if (fromSDCard || !uri.startsWith("http")) {
            uri = "file:///mnt" + uri;
        }
        ImageLoader.getInstance().displayImage(uri, imageView);
    }

    public static void displayCircleImage(String uri, ImageView imageView, boolean fromSDCard) {
        if (fromSDCard || !uri.startsWith("http")) {
            uri = "file:///mnt" + uri;
        }
        ImageLoader.getInstance().displayImage(uri, imageView);
    }

    public static void displayCircleImage(String uri, ImageView imageView, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(uri, imageView, getCircleDisplayImageOptions(), listener);
    }

    public static void displayRoundImage(String uri, int cornerRadiusPixels, ImageView imageView) {
        displayRoundImage(uri, cornerRadiusPixels, imageView, null);
    }

    public static void displayRoundImage(String uri, int cornerRadiusPixels, ImageView imageView, ImageLoadingListener listener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
                .build();
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
    }

    public static void displayAvatar(String uri, ImageView imageView) {
        if (TextUtils.isEmpty(uri)) {
            uri = Default_uri;
        }
        ImageLoader.getInstance().displayImage(uri, imageView, new AvatarImageLoadingListener());
    }

    public static void displayCircleAvatar(String uri, ImageView imageView) {
        if (TextUtils.isEmpty(uri)) {
            uri = Default_uri;
        }
        displayCircleImage(uri, imageView, new AvatarImageLoadingListener());
    }

    public static void displayRoundAvatar(String uri, int cornerRadiusPixels, ImageView imageView) {
        if (TextUtils.isEmpty(uri)) {
            uri = Default_uri;
        }
        displayRoundImage(uri, cornerRadiusPixels, imageView, new AvatarImageLoadingListener());
    }

    static class AvatarImageLoadingListener implements ImageLoadingListener {
        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            view.setBackgroundResource(R.drawable.test_head);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }

    public static Bitmap getBitmap(String uri) {
        return ImageLoader.getInstance().loadImageSync(uri);
    }

    public static Bitmap getCircleBitmap(String uri) {
        return ImageLoader.getInstance().loadImageSync(uri, getCircleDisplayImageOptions());
    }

    private static DisplayImageOptions getCircleDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new CircleBitmapDisplayer())
                .build();
        return options;
    }

}
