//package com.fenmenbielei.bedsense.uitls;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.provider.Settings;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//
///**
// * 作者：chuang on 2017/2/16
// * 邮箱：844285775@qq.com
// */
//
//public class UserPermissionUtils {
//
//    // 要申请的权限
//    //本权限为获取到当前的位置,如果你有需要可以添加其他权限
//    public  String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
//    public Dialog dialog;
//    //传递过来的activity
//    public Activity mActivity;
//
//    /**
//     * 构造函数
//     * @param mActivity 为传递过来的上下文
//     */
//    public UserPermissionUtils(Activity mActivity) {
//        this.mActivity = mActivity;
//    }
//
//    /**
//     * 判断当前的权限是否满足条件
//     */
//    public  void getVersion() {
//        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            // 检查该权限是否已经获取
//            int i = ContextCompat.checkSelfPermission(mActivity, permissions[0]);
//            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
//            if (i != PackageManager.PERMISSION_GRANTED) {
//                // 如果没有授予该权限，就去提示用户请求
//                showDialogTipUserGoToAppSettting();
//            }
//        }
//    }
//
//    /**
//     * 弹出对话框.首都添加权限
//     */
//    public void showDialogTipUserGoToAppSettting() {
//
//        dialog = new AlertDialog.Builder(mActivity)
//                .setTitle("获取当前位置权限")
//                .setMessage("请在-应用设置-权限-中，允许应用获取手机的位置权限")
//                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 跳转到应用设置界面
//                        goToAppSetting();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).setCancelable(false).show();
//    }
//
//
//    /**
//     * 跳转到手动开启权限页面
//     */
//    public void goToAppSetting() {
//        //通过意图打开权限页面,手动的开启权限
//        Intent intent = new Intent();
//        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
//        intent.setData(uri);
//        //第二种开启activity的方式,不详细介绍.可以自己查一下
//        mActivity.startActivityForResult(intent, 123);
//    }
//}
