package com.fenmenbielei.bedsense.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;


import com.fenmenbielei.bedsense.MyApplication;
import com.fenmenbielei.bedsense.RunningContext;
import com.fenmenbielei.bedsense.base.BaseFragment;
import com.fenmenbielei.bedsense.bean.DeviceBean;
import com.fenmenbielei.bedsense.blue.BluetoothLeService;
import com.fenmenbielei.bedsense.uitls.BlueUtils;
import com.fenmenbielei.bedsense.uitls.LogUtils;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.fenmenbielei.bedsense.uitls.ToastUtils;
import com.wnhz.shidaodianqi.R;

import androidx.annotation.Nullable;

public abstract class KuaijieBaseFragment extends BaseFragment {

    public static final String TAG = "KuaijieBaseFragment";

    /**
     * 记忆询问码code
     */
    protected final static long MGS_ASK_STATUS_CODE = 7;

    /**
     * 默认间隔
     */
    protected final static long DEFAULT_INTERVAL = 2000;

    // 蓝牙设备名称
    protected String blueDeviceName;


    /**
     * 蓝牙回传数据
     *
     * @param data
     */
    abstract void handleReceiveData(String data);

    /**
     * 发起记忆询问码
     */
    abstract void askStatus();

    @Override
    public void onTongbukzEvent(boolean show, boolean open) {
        // do something
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(mKuaijieReceiver, makeGattUpdateIntentFilter());
        DeviceBean deviceBean = Prefer.getInstance().getConnectedDevice();
        if (deviceBean != null) {
            blueDeviceName = deviceBean.getTitle();
            LogUtils.e(TAG, "blueDeviceName名称：" + blueDeviceName);
        }
        // 发码的操作不能放在主线程中处理，不然会导致未知的问题
        RunningContext.threadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300L);
                    askStatus();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mKuaijieReceiver);
        super.onDestroy();
    }


    /**
     * 发送记忆询问码的命令
     *
     * @param cmd
     */
    protected void sendAskBlueCmd(final String cmd) {
            sendBlueCmd(cmd);
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
            ToastUtils.showToast(getContext(), getString(R.string.device_no_connected));
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


    /**
     * 广播接收器，负责接收BluetoothLeService类发送的数据
     */
    private final BroadcastReceiver mKuaijieReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //发现GATT服务器
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //处理发送过来的数据  (//有效数据)
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String data = bundle.getString(BluetoothLeService.EXTRA_DATA);
                    if (data != null) {
                        LogUtils.e("KuaijieBaseFragment","==快捷  接收设备返回的数据==", data);
                        handleReceiveData(data);
                    }
                }
            }
        }
    };


    /* 意图过滤器 */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

}
