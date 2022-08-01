package com.fenmenbielei.bedsense.base;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.fenmenbielei.bedsense.MyApplication;
import com.fenmenbielei.bedsense.RunningContext;
import com.fenmenbielei.bedsense.uitls.BlueUtils;
import com.fenmenbielei.bedsense.uitls.LogUtils;
import com.fenmenbielei.bedsense.uitls.ToastUtils;
import com.wnhz.shidaodianqi.R;


/**
 * Created by xiayundong on 2021/9/25.
 */
public class BaseBlueActivity extends BaseActivity {

    private final static String TAG = "BaseBlueActivity";

    // 特征值
    protected BluetoothGattCharacteristic characteristic;


    protected boolean checkConnected() {
        // 判断蓝牙是否连接
        if (!BlueUtils.isConnected()) {
            ToastUtils.showToast(RunningContext.sAppContext, getString(R.string.device_no_connected));
            LogUtils.i(TAG, "sendBlueCmd -> 蓝牙未连接");
            return false;
        }
        return true;
    }

    /**
     * 发送蓝牙命令
     *
     * @param cmd
     */
    protected void sendCmd(String cmd) {
        cmd = cmd.replace(" ", "");
        Log.i(TAG, "sendBlueCmd: " + cmd);
        // 判断蓝牙是否连接
        if (!BlueUtils.isConnected()) {
            ToastUtils.showToast(RunningContext.sAppContext, getString(R.string.device_no_connected));
            LogUtils.i(TAG, "sendBlueCmd -> 蓝牙未连接");
            return;
        }
        if (characteristic == null) {
            characteristic = MyApplication.getInstance().gattCharacteristic;
        }
        if (characteristic == null) {
            LogUtils.i(TAG, "sendBlueCmd -> 特征值未获取到");
            return;
        }
        characteristic.setValue(BlueUtils.StringToBytes(cmd));
        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(characteristic);
    }
}
