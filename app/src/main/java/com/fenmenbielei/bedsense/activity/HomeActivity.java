package com.fenmenbielei.bedsense.activity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.fenmenbielei.bedsense.MyApplication;
import com.fenmenbielei.bedsense.RunningContext;
import com.fenmenbielei.bedsense.adapter.TabPagerAdapter;
import com.fenmenbielei.bedsense.base.BaseActivity;
import com.fenmenbielei.bedsense.base.BaseFragment;
import com.fenmenbielei.bedsense.bean.AlarmBean;
import com.fenmenbielei.bedsense.bean.DateBean;
import com.fenmenbielei.bedsense.bean.DeviceBean;
import com.fenmenbielei.bedsense.bean.MessageEvent;
import com.fenmenbielei.bedsense.blue.BluetoothLeService;
import com.fenmenbielei.bedsense.fragment.KuaijieBaseFragment;
import com.fenmenbielei.bedsense.fragment.KuaijieK1Fragment;
import com.fenmenbielei.bedsense.uitls.BlueUtils;
import com.fenmenbielei.bedsense.uitls.LogUtils;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.fenmenbielei.bedsense.uitls.ToastUtils;
import com.fenmenbielei.bedsense.view.NoScrollViewPager;
import com.fenmenbielei.bedsense.view.TranslucentActionBar;
import com.wnhz.shidaodianqi.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements View.OnClickListener, TranslucentActionBar.ActionBarClickListener {

    public static final String TAG = "HomeActivity";

    // 设置tab个数
    private final static int tabCount = 4;

    @BindView(R.id.actionbar)
    TranslucentActionBar actionBar;

    @BindView(R.id.ll_content)
    RelativeLayout relativeLayout;

    @BindView(R.id.vp_home)
    NoScrollViewPager viewPager;

    @BindView(R.id.tab1)
    RelativeLayout tab1;
    @BindView(R.id.tab1_text)
    TextView tab1TextView;

    @BindView(R.id.tab2)
    RelativeLayout tab2;
    @BindView(R.id.tab2_text)
    TextView tab2TextView;


    @BindView(R.id.tab3)
    RelativeLayout tab3;
    @BindView(R.id.tab3_text)
    TextView tab3TextView;


    @BindView(R.id.tab4)
    RelativeLayout tab4;
    @BindView(R.id.tab4_text)
    TextView tab4TextView;


    List<TextView> tabTextViews;

    List<BaseFragment> fragments;
    TabPagerAdapter tabPagerAdapter;


    private String blueName;

    private String deviceAddress;

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
        startActivity(intent);
        sendAlarmInitCmd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        actionBar.setData("", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
        actionBar.setStatusBarHeight(getStatusBarHeight());
        DeviceBean deviceBean = Prefer.getInstance().getConnectedDevice();
        if (deviceBean != null) {
            blueName = deviceBean.getTitle();
            deviceAddress = deviceBean.getAddress();
            actionBar.setTitle(blueName);
        }
        LogUtils.e(TAG, "当前连接的蓝牙名称为：" + blueName);
        initView();
        setCurrentTab(1);

        // 启动蓝牙service
        Intent blueServiceIntent = new Intent(HomeActivity.this, BluetoothLeService.class);
        startService(blueServiceIntent);
        bindService(blueServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        RunningContext.threadPool().execute(new Runnable() {
            @Override
            public void run() {
                askStatus();
            }
        });
    }

    private void initView() {
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);
        tabTextViews = new ArrayList<>();
        tabTextViews.add(tab1TextView);
        tabTextViews.add(tab2TextView);
        tabTextViews.add(tab3TextView);
        tabTextViews.add(tab4TextView);


        fragments = new ArrayList<>();
        setFragments();
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(tabPagerAdapter);
        viewPager.setScroll(true);
        viewPager.setOffscreenPageLimit(3);
    }


    private void setFragments() {
        fragments.add(new KuaijieK1Fragment());
        fragments.add(new KuaijieK1Fragment());
        fragments.add(new KuaijieK1Fragment());
        fragments.add(new KuaijieK1Fragment());
    }

    private void setCurrentTab(int tabIndex) {
        int position = tabIndex - 1;
        for (int i = 0; i < tabCount; i++) {
            if (position == i) {
                tabTextViews.get(i).setBackgroundColor(getColor(R.color.tab_selected));
            } else {
                tabTextViews.get(i).setBackgroundColor(getColor(R.color.bg_hui));
            }
        }
        viewPager.setCurrentItem(position, false);
    }

    private void askStatus() {
        try {
            Thread.sleep(500L);
            // 发送闹钟指令
            sendAlarmInitCmd();
            Thread.sleep(500L);
            sendBlueCmd("FF FF FF FF 01 00 0A 0B 0F 21 04");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送闹钟初始化命令
     */
    private void sendAlarmInitCmd() {
        StringBuilder cmdSB = new StringBuilder();
        cmdSB.append("FFFFFFFF01000111");
        DateBean dateBean = new DateBean(new Date());
        cmdSB.append(dateBean.getHour());
        cmdSB.append(dateBean.getMinute());
        cmdSB.append(dateBean.getSecond());
        cmdSB.append(dateBean.getWeek());
        cmdSB.append(dateBean.getEndYear());
        cmdSB.append(dateBean.getMonth());
        cmdSB.append(dateBean.getDay());
        // 累加校验和
        cmdSB.append(BlueUtils.makeChecksum(cmdSB.toString()));
        LogUtils.i(TAG, "发送闹钟指令：" + cmdSB.toString());
        sendBlueCmd(cmdSB.toString());
    }


    /**
     * 发送蓝牙命令
     *
     * @param cmd
     */
    protected void sendBlueCmd(String cmd) {
        cmd = cmd.replace(" ", "");
        Log.i(TAG, "sendBlueCmd: " + cmd);
        // 判断蓝牙是否连接
        if (!BlueUtils.isConnected()) {
            ToastUtils.showToast(HomeActivity.this, getString(R.string.device_no_connected));
            LogUtils.i(TAG, "sendBlueCmd -> 蓝牙未连接");
            return;
        }
        BluetoothGattCharacteristic characteristic = MyApplication.getInstance().gattCharacteristic;
        if (characteristic == null) {
            LogUtils.i(TAG, "sendBlueCmd -> 特征值未获取到");
            return;
        }
        characteristic.setValue(BlueUtils.StringToBytes(cmd));
        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(characteristic);
    }

    private void handleReceiveData(String cmd) {
        cmd = cmd.toUpperCase().replaceAll(" ", "");
        if (cmd.contains("FFFFFFFF0100030B00")) {
            LogUtils.i(TAG, "收到无闹钟指令：" + cmd);
            // 有闹钟,未设置
            AlarmBean alarmBean = new AlarmBean();
            alarmBean.setAlarmSwitch(false);
            Prefer.getInstance().setAlarm(deviceAddress, alarmBean);
        } else if (cmd.contains("FFFFFFFF01000413")) {
            LogUtils.i(TAG, "收到有闹钟指令：" + cmd);
            setHasAlarm(cmd);
        } else if (cmd.contains("FFFFFFFF01000A0B") || cmd.contains("FFFFFFFF0100090B")) {
            Prefer.getInstance().setTongbukzShow(deviceAddress, true);
            String tongbukzSwitchCmd = cmd.substring(16, 18);
            Prefer.getInstance().setTongbukzSwitch(deviceAddress, "01".equals(tongbukzSwitchCmd) ? true : false);

            MessageEvent messageEvent = new MessageEvent();
            messageEvent.setTongbukzShow(true);
            messageEvent.setTongbukzSwitch("01".equals(tongbukzSwitchCmd) ? true : false);
            EventBus.getDefault().post(messageEvent);
        }
    }

    private void setHasAlarm(String cmd) {
        // 有闹钟，已设置
        AlarmBean alarmBean = new AlarmBean();
        String cmdStatus = cmd.substring(16, 18);
        // 开关
        if (cmdStatus.equals("0F")) {
            alarmBean.setAlarmSwitch(true);
        } else {
            alarmBean.setAlarmSwitch(false);
        }

        // 时间
        String timeHour = cmd.substring(18, 20);
        alarmBean.setHourStr(timeHour);
        String timeMin = cmd.substring(20, 22);
        alarmBean.setMinuteStr(timeMin);

        // 星期
        String cmdWeek = BlueUtils.hexString16To2hexString(cmd.substring(24, 26));
        for (int i = 0; i < 7; i++) {
            char charAt = cmdWeek.charAt(i);
            if (charAt == '1') {
                alarmBean.getWeekCheckBeanMap().put(7 - i, true);
            }
        }

        // 模式
        String cmdMode = cmd.substring(28, 30);
        alarmBean.setModeCode(cmdMode);

        // 按摩
        String cmdAnmo = cmd.substring(30, 32);
        if (cmdAnmo.equals("01")) {
            alarmBean.setAnmo(true);
        } else {
            alarmBean.setAnmo(false);
        }

        // 响铃
        String cmdRing = cmd.substring(32, 34);
        if (cmdRing.equals("01")) {
            alarmBean.setXiangling(true);
        } else {
            alarmBean.setXiangling(false);
        }
        Prefer.getInstance().setAlarm(deviceAddress, alarmBean);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
                setCurrentTab(1);
                break;
            case R.id.tab2:
                setCurrentTab(2);
                break;
            case R.id.tab3:
                setCurrentTab(3);
                break;
            case R.id.tab4:
                setCurrentTab(4);
                // 发送灯光指令
                sendBlueCmd("FFFFFFFF050005FF23C728");
                break;
            default:
                break;
        }
    }

    /* 意图过滤器 */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    /**
     * 广播接收器，负责接收BluetoothLeService类发送的数据
     */
    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            LogUtils.i(TAG, "监听到蓝牙状态 ：" + action);
            if (action == BluetoothLeService.ACTION_GATT_CONNECTED) {
                LogUtils.e(TAG, "监听到蓝牙状态 ：已连接");
            } else if (action == BluetoothLeService.ACTION_GATT_DISCONNECTED) {
                // 监听到蓝牙已断开
                LogUtils.e(TAG, "监听到蓝牙状态 ：已断开");
                Prefer.getInstance().disConnected();
                ToastUtils.showToast(HomeActivity.this, R.string.device_disconnect);
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //处理发送过来的数据  (//有效数据)
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String data = bundle.getString(BluetoothLeService.EXTRA_DATA);
                    if (data != null) {
                        LogUtils.e(TAG, "==首页  接收设备返回的数据==", data);
                        handleReceiveData(data);
                    }
                }
            }
        }
    };


    /* BluetoothLeService绑定的回调函数 */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            LogUtils.d(TAG, "BluetoothLeService 已绑定 HomeActivity");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtils.i(TAG, "BluetoothLeService 已断开");
        }
    };

}