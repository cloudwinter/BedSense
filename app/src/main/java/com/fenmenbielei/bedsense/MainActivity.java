package com.fenmenbielei.bedsense;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.fenmenbielei.bedsense.base.BaseActivity;
import com.fenmenbielei.bedsense.dialog.WaitDialog;
import com.fenmenbielei.bedsense.javabean.TestBean;
import com.fenmenbielei.bedsense.uitls.LocaleUtils;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.wnhz.shidaodianqi.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    Activity activity;
    private int REQUEST_ENABLE_BT = 1;
    private WaitDialog waitDialog;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor edit;
    private List<TestBean> testBeanList = new ArrayList<>();
    private static final int BLE_STATUS = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        init_ble();
        initView();
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: init_ble
     * @Description: TODO(初始化蓝牙)
     */
    private void init_ble() {
        // 手机硬件支持蓝牙
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持BLE模式", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 打开蓝牙权限
        if (MyApplication.getInstance().mBluetoothAdapter == null || !MyApplication.getInstance().mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        //申请扫描蓝牙权限
        if (Build.VERSION.SDK_INT >= 23) {
            //判断是否有权限
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        0x001);
                //向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(MainActivity.this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void initView() {
        findViewById(R.id.tv_shushi).setOnClickListener(this);
        findViewById(R.id.tv_haohua).setOnClickListener(this);
        findViewById(R.id.tv_lianjie).setOnClickListener(this);

        if (!TextUtils.isEmpty(Prefer.getInstance().isTypeLan()) && Prefer.getInstance().isTypeLan().equals("1")) {
            if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_CHINESE)) {
                LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_CHINESE);
            }
        } else {
            if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_ENGLISH)) {
                LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_ENGLISH);
            }
        }
        if (!Prefer.getInstance().getCurrentDecice().equals("")) {
            startActivity(new Intent(this, ShuShiBanActivity.class));
            // finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shushi:
                //舒适版
                startActivity(new Intent(this, ShuShiBanActivity.class));
                // switchLanguage("en");
                // MyApplication.getInstance().exit(activity);
                break;

            case R.id.tv_haohua:
                //豪华版
                startActivity(new Intent(this, HaoHuaBanActivity.class));

                // switchLanguage("zh");
                // MyApplication.getInstance().exit(activity);
                break;
            case R.id.tv_lianjie:
                startActivity(new Intent(this, ShuShiBanActivity.class));
                finish();
                break;
        }
    }


    private long exitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                // 退出代码
                // MyApplication.isLogin = false;
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
