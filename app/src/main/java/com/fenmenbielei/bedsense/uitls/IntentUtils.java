package com.fenmenbielei.bedsense.uitls;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

/**
 * [跳转工具]
 *  Created by wanghongchuang
 *  on 2016/8/25.
 *  email:844285775@qq.com
 */
public class IntentUtils {

    /**
     * 浏览器
     *
     * @param context
     * @param urlStr
     */
    public static void openBrowser(Context context, String urlStr) {
        if (!TextUtils.isEmpty(urlStr)) {
            openBrowser(context, Uri.parse(urlStr));
        }
    }

    public static void openBrowser(Context context, Uri uri) {
        if (uri != null) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(uri);
            context.startActivity(intent);
        }
    }

    /**
     * 拍照
     *
     * @param filePath
     * @return
     */
    public static Intent getImageCapture(String filePath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        return intent;
    }

    /**
     * 选照
     *
     * @return
     */
    public static Intent getImagePick() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static Intent getImageCrop(String filePath, int width, int height) {
        Uri uri = Uri.fromFile(new File(filePath));
        return getImageCrop(uri, null, width, height);
    }

    /**
     * 选照片回调时使用
     *
     * @param uri
     * @param outputPath
     * @param width
     * @param height
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static Intent getImageCrop(Uri uri, String outputPath, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        if (width != 0 && height != 0) {
            intent.putExtra("aspectX", width);
            intent.putExtra("aspectY", height);
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
        }
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        if (outputPath == null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputPath)));
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }


}
