package com.fenmenbielei.bedsense.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dinuscxj.progressbar.CircleProgressBar;

import com.fenmenbielei.bedsense.MyApplication;
import com.fenmenbielei.bedsense.base.BaseFragment;
import com.fenmenbielei.bedsense.service.BluetoothLeService;
import com.fenmenbielei.bedsense.uitls.LogUtils;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.fenmenbielei.bedsense.uitls.ToastUtils;
import com.wnhz.shidaodianqi.R;


import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HHDengGuangFragment extends BaseFragment implements OnClickListener {

    @BindView(R.id.img_light)
    ImageView img_light;
    @BindView(R.id.custom_progress4)
    CircleProgressBar mCustomProgressBar4;
    @BindView(R.id.ll_first)
    LinearLayout ll_first;
    @BindView(R.id.ll_two)
    LinearLayout ll_two;
    @BindView(R.id.ll_third)
    LinearLayout ll_third;

    @BindView(R.id.ll_dengguang)
    LinearLayout ll_dengguang;

    private int second = 0;

    //蓝牙特征值
    private static BluetoothGattCharacteristic target_chara = null;

    public static HHDengGuangFragment newInstance() {
        HHDengGuangFragment homeFragment4 = new HHDengGuangFragment();
        return homeFragment4;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hhkuai_jie, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ll_first.setOnClickListener(this);
        ll_third.setOnClickListener(this);
        ll_two.setOnClickListener(this);
        ll_dengguang.setOnClickListener(this);

        mCustomProgressBar4.setProgress(100);
//        second = 60;
//        mCustomProgressBar4.setMax(60 * 100);
//        //根据倒计时ProgressBar动画
//        Timer timer = new Timer();
//        timer.schedule(timerTask, 0, 1000);
//        ValueAnimator animator = ValueAnimator.ofInt(1, 60 * 100);
//
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int animatedValue = (int) animation.getAnimatedValue();
//                Log.e("=====剩余时间====", "" + animatedValue);
//
//
//                mCustomProgressBar4.setProgress(60 * 100 - animatedValue);
//            }
//        });
//        animator.setDuration(20000);
//        animator.start();
    }

    @Override
    public void onClick(View view) {
        target_chara = MyApplication.getInstance().gattCharacteristic;
        switch (view.getId()) {
            case R.id.ll_dengguang:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), "未连接相关蓝牙设备");
                    return;
                }

                byte[] value9 = new byte[11];
                value9[0] = (byte) 0xff;
                value9[1] = (byte) 0xff;
                value9[2] = (byte) 0xff;
                value9[3] = (byte) 0xff;
                value9[4] = (byte) 0x05;
                value9[5] = (byte) 0x00;
                value9[6] = (byte) 0x00;
                value9[7] = (byte) 0x00;
                value9[8] = (byte) 0x00;
                value9[9] = (byte) 0xd7;
                value9[10] = (byte) 0x00;
                target_chara.setValue(value9);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
            case R.id.ll_first:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), "未连接相关蓝牙设备");
                    return;
                }

                ll_first.setBackgroundResource(R.drawable.ic_light_liang);
                ll_two.setBackgroundResource(R.drawable.ic_light_an);
                ll_third.setBackgroundResource(R.drawable.ic_light_an);

                byte[] value6 = new byte[11];
                value6[0] = (byte) 0xff;
                value6[1] = (byte) 0xff;
                value6[2] = (byte) 0xff;
                value6[3] = (byte) 0xff;
                value6[4] = (byte) 0x05;
                value6[5] = (byte) 0x00;
                value6[6] = (byte) 0x00;
                value6[7] = (byte) 0x00;
                value6[8] = (byte) 0x19;
                value6[9] = (byte) 0x16;
                value6[10] = (byte) 0xca;
                target_chara.setValue(value6);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
            case R.id.ll_two:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), "未连接相关蓝牙设备");
                    return;
                }
                ll_first.setBackgroundResource(R.drawable.ic_light_an);
                ll_two.setBackgroundResource(R.drawable.ic_light_liang);
                ll_third.setBackgroundResource(R.drawable.ic_light_an);

                byte[] value8 = new byte[11];
                value8[0] = (byte) 0xff;
                value8[1] = (byte) 0xff;
                value8[2] = (byte) 0xff;
                value8[3] = (byte) 0xff;
                value8[4] = (byte) 0x05;
                value8[5] = (byte) 0x00;
                value8[6] = (byte) 0x00;
                value8[7] = (byte) 0x00;
                value8[8] = (byte) 0x1a;
                value8[9] = (byte) 0x56;
                value8[10] = (byte) 0xcb;
                target_chara.setValue(value8);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
            case R.id.ll_third:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), "未连接相关蓝牙设备");
                    return;
                }
                ll_first.setBackgroundResource(R.drawable.ic_light_an);
                ll_two.setBackgroundResource(R.drawable.ic_light_an);
                ll_third.setBackgroundResource(R.drawable.ic_light_liang);

                byte[] value10 = new byte[11];
                value10[0] = (byte) 0xff;
                value10[1] = (byte) 0xff;
                value10[2] = (byte) 0xff;
                value10[3] = (byte) 0xff;
                value10[4] = (byte) 0x05;
                value10[5] = (byte) 0x00;
                value10[6] = (byte) 0x00;
                value10[7] = (byte) 0x00;
                value10[8] = (byte) 0x1b;
                value10[9] = (byte) 0x97;
                value10[10] = (byte) 0x0b;
                target_chara.setValue(value10);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
        }
    }


    //发送消息
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                timerTask.cancel();
            }
        }
    };

    //读秒线程
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Log.e("====开始=== 进来了", "秒数：" + second);
            Message msg = new Message();
            msg.what = second;
            handler2.sendEmptyMessageDelayed(msg.what, 1000);
            Log.e("=====剩余时间====", "" + second);
            second--;
        }
    };

    /**
     * 广播接收器，负责接收BluetoothLeService类发送的数据
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //发现GATT服务器
                // Show all the supported services and characteristics on the
                // user interface.
                //获取设备的所有蓝牙服务
//                displayGattServices(MyApplication.getInstance().mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //处理发送过来的数据  (//有效数据)
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String data = bundle.getString(BluetoothLeService.EXTRA_DATA);
                    if (data != null) {
                        displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                    }
                }
                // Log.e("==处理发送过来的数据==", "" + intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    /**
     * @param @param rev_string(接受的数据)
     * @return void
     * @throws
     * @Title: displayData
     * @Description: TODO(接收到的数据在scrollview上显示)
     */
    private void displayData(String rev_string) {
        LogUtils.e("==灯光  接收设备返回的数据==", "" + rev_string);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除广播接收器
        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    // Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
    @Override
    public void onResume() {
        super.onResume();
        //绑定广播接收器
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    /* 意图过滤器 */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
