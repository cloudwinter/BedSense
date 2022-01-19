package com.fenmenbielei.bedsense.uitls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.fenmenbielei.bedsense.javabean.OperaType;
import com.wnhz.shidaodianqi.R;


/**
 * [图片工具类]
 *  Created by wanghongchuang
 *  on 2016/8/25.
 *  email:844285775@qq.com
 */
public class ImageUtils {
    /**
     *[图片工具类]
     */
    public OperaType currentType = OperaType.NONE;
    public int currentCode = -1;
    private int width = 500;
    private int height = 400;
    private String currentPath;
    private Activity activity;

    public ImageUtils(Activity activity) {
        this.activity = activity;
    }

    public ImageUtils(Activity activity, int width, int height) {
        this.activity = activity;
        this.width = width;
        this.height = height;
    }

    public void showDialog(final String filePath, final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(new CharSequence[]{activity.getString(R.string.take_photo), activity.getString(R.string.photo_gallery)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentCode = requestCode;
                currentPath = filePath;
                if (which == 0) {
                    currentType = OperaType.TAKE;
                    activity.startActivityForResult(IntentUtils.getImageCapture(filePath), requestCode);
                } else {
                    currentType = OperaType.CLIP;
                    activity.startActivityForResult(IntentUtils.getImagePick(), requestCode);
                }
            }
        }).show();
    }

    /**
     * 获取回调
     *
     * @param requestCode
     * @param data
     * @return
     */
    public String getStringOnResult(int requestCode, Intent data) {
        switch (currentType) {
            case TAKE: {
                currentType = OperaType.CROP;
                activity.startActivityForResult(IntentUtils.getImageCrop(currentPath, width, height), requestCode);
            }
            break;
            case CLIP: {
                currentType = OperaType.CROP;
                activity.startActivityForResult(IntentUtils.getImageCrop(data.getData(), currentPath, width, height), requestCode);
            }
            break;
            case CROP: {
                return currentPath;
            }
            case NONE: {

            }
            break;
        }
        return null;
    }

    /**
     * 是否归我处理
     *
     * @param requestCode
     * @return
     */
    public boolean isMyTask(int requestCode) {
        return this.currentCode == requestCode;
    }

}
