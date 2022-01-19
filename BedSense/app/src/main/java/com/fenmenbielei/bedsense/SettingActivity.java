package com.fenmenbielei.bedsense;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


import com.fenmenbielei.bedsense.base.BaseActivity;
import com.fenmenbielei.bedsense.impl.ActionBarClickListener;
import com.fenmenbielei.bedsense.javabean.TestBean;
import com.fenmenbielei.bedsense.ui.ConnectActivity;
import com.fenmenbielei.bedsense.ui.ExplainActivity;
import com.fenmenbielei.bedsense.ui.LanguageActivity;
import com.fenmenbielei.bedsense.ui.OpenBleActivity;
import com.fenmenbielei.bedsense.ui.QrCodeActivity;
import com.fenmenbielei.bedsense.uitls.BroadCastReceiverUtil;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.fenmenbielei.bedsense.uitls.SystemUtils;
import com.fenmenbielei.bedsense.uitls.ToastUtils;
import com.fenmenbielei.bedsense.view.TranslucentActionBar;
import com.wnhz.shidaodianqi.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.ll_connect)
    LinearLayout ll_connect;
    @BindView(R.id.tv_connect)
    TextView tv_connect;
    @BindView(R.id.ll_qr_code)
    LinearLayout ll_qr_code;
    @BindView(R.id.sw_on_off)
    Switch sw_on_off;
    @BindView(R.id.ll_language)
    LinearLayout ll_language;
    @BindView(R.id.ll_explain)
    LinearLayout ll_explain;
    @BindView(R.id.ll_version)
    LinearLayout ll_version;
    @BindView(R.id.tv_version)
    TextView tv_version;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int BLE_STATUS = 222;
    private SharedPreferences mySharedPreferences;
    private List<TestBean> testBeanList = new ArrayList<>();

    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        actionbar.setData("设置", R.mipmap.ic_default_return, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        initView();
    }

    private void initView() {
        //设置连接状态
        tv_connect.setText(Prefer.getInstance().getBleStatus());

        tv_version.setText(SystemUtils.getVersionName(SettingActivity.this));
        ll_connect.setOnClickListener(this);
        ll_qr_code.setOnClickListener(this);
        ll_language.setOnClickListener(this);
        ll_explain.setOnClickListener(this);
        sw_on_off.setOnClickListener(this);
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //连接设备
            case R.id.ll_connect:
                mySharedPreferences = getSharedPreferences("scenelist", Context.MODE_PRIVATE);
                edit = mySharedPreferences.edit();

                BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
                // 检查设备上是否支持蓝牙
                if (blueadapter == null) {
                    ToastUtils.showToast(SettingActivity.this, "该手机暂不支持蓝牙模式");
                    finish();
                    return;
                }

                if (blueadapter.isEnabled()) {
                    final Intent intent = new Intent(SettingActivity.this, ConnectActivity.class);
                    if (tv_connect.getText().toString().equals("已连接")) {
                        showDialogv7("是否断开连接", "取消", "断开", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                testBeanList.clear();
                                edit.clear();
                                MyApplication.getInstance().mBluetoothLeService.disconnect();
                                ToastUtils.showToast(SettingActivity.this, "已断开连接");
                                Prefer.getInstance().setCurrentDecice("");
                                BroadCastReceiverUtil.sendBroadcast(SettingActivity.this, "CONNECTSTATUS");
                                intent.putExtra("isConnected", "2");
                                startActivityForResult(intent, BLE_STATUS);
                            }
                        });
                    } else {
                        intent.putExtra("isConnected", "2");
                        startActivityForResult(intent, BLE_STATUS);
                    }

                } else {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                break;
            //二维码
            case R.id.ll_qr_code:
                startActivity(new Intent(SettingActivity.this, QrCodeActivity.class));
                break;
            //是否震动
            case R.id.sw_on_off:
                sw_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            ToastUtils.showToast(SettingActivity.this, "打开");
                        } else {
                            ToastUtils.showToast(SettingActivity.this, "关闭");
                        }
                    }
                });
                break;
            //切换语言
            case R.id.ll_language:
                startActivity(new Intent(SettingActivity.this, LanguageActivity.class));
                break;
            //使用说明
            case R.id.ll_explain:
                startActivity(new Intent(SettingActivity.this, ExplainActivity.class));
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            startActivity(new Intent(SettingActivity.this, OpenBleActivity.class));
            return;
        }

        if (requestCode == BLE_STATUS && resultCode == RESULT_OK) {
            if (data != null) {
                tv_connect.setText(data.getStringExtra("status"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
