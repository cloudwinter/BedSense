package com.fenmenbielei.bedsense.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;

import com.fenmenbielei.bedsense.MyApplication;
import com.fenmenbielei.bedsense.base.BaseFragment;
import com.fenmenbielei.bedsense.blue.BluetoothLeService;
import com.fenmenbielei.bedsense.uitls.LogUtils;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.fenmenbielei.bedsense.uitls.ToastUtils;
import com.wnhz.shidaodianqi.R;






import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnMoFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.img_light)
    ImageView img_light;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.custom_progress4)
    CircleProgressBar mCustomProgressBar4;
    @BindView(R.id.img_10min)
    LinearLayout img_10min;
    @BindView(R.id.img_20min)
    LinearLayout img_20min;
    @BindView(R.id.img_30min)
    LinearLayout img_30min;
    @BindView(R.id.img_pl_down)
    ImageView img_pl_down;
    @BindView(R.id.img_pl_up)
    ImageView img_pl_up;
    @BindView(R.id.img_head_down)
    ImageView img_head_down;
    @BindView(R.id.img_head_up)
    ImageView img_head_up;
    @BindView(R.id.img_foot_down)
    ImageView img_foot_down;
    @BindView(R.id.img_foot_up)
    ImageView img_foot_up;

    //高亮图
    @BindView(R.id.img_10mins)
    ImageView img_10mins;
    @BindView(R.id.img_20mins)
    ImageView img_20mins;
    @BindView(R.id.img_30mins)
    ImageView img_30mins;


    @BindView(R.id.ll_anmo)
    LinearLayout ll_anmo;

    @BindView(R.id.sw_on_off)
    ImageView sw_on_off;
    @BindView(R.id.invest_bg)
    RelativeLayout bg;
    @BindView(R.id.invest_fg)
    View fg;

    @BindView(R.id.toubu_bg)
    RelativeLayout toubu_bg;
    @BindView(R.id.toubu_fg)
    View toubu_fg;
    @BindView(R.id.foot_bg)
    RelativeLayout foot_bg;
    @BindView(R.id.foot_fg)
    View foot_fg;
    private int second = 0;
    int wid;
    private boolean isCanTauch10 = false;
    private boolean isCanTauch20 = false;
    private boolean isCanTauch30 = false;
    int pinlvCount = 0, toubuCount = 0, zhubuCount = 0;
    //蓝牙特征值
    private static BluetoothGattCharacteristic target_chara = null;

    public static AnMoFragment newInstance() {
        AnMoFragment homeFragment3 = new AnMoFragment();
        return homeFragment3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hhan_mo, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
//        Glide.with(HHAnMoFragment.this).load(R.drawable.gifshow)
//                .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        wid = dm.widthPixels;
        img_pl_down.setOnClickListener(this);
        img_pl_up.setOnClickListener(this);
        img_foot_up.setOnClickListener(this);
        img_foot_down.setOnClickListener(this);
        img_head_down.setOnClickListener(this);
        img_head_up.setOnClickListener(this);
        img_10min.setOnClickListener(this);
        img_20min.setOnClickListener(this);
        img_30min.setOnClickListener(this);
        ll_anmo.setOnClickListener(this);

        sw_on_off.setOnClickListener(this);

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
            case R.id.sw_on_off:
                if (isCanTauch10) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

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
                    value9[8] = (byte) 0x1c;
                    value9[9] = (byte) 0xd9;
                    value9[10] = (byte) 0xdd;
                    target_chara.setValue(value9);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);

                    sw_on_off.setBackgroundResource(R.drawable.ic_off);
                    isCanTauch10 = false;
                } else {
                    sw_on_off.setBackgroundResource(R.drawable.ic_on);
                    isCanTauch10 = true;
                }
                break;
            case R.id.ll_anmo:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

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
                value9[9] = (byte) 0xd8;
                value9[10] = (byte) 0x14;
                target_chara.setValue(value9);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
            case R.id.img_10min:



                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }

                if (!isCanTauch10) {
                    img_10mins.setBackgroundResource(R.drawable.light10);
                    img_20mins.setBackgroundResource(R.drawable.ic_20min);
                    img_30mins.setBackgroundResource(R.drawable.ic_30min);
                    byte[] value10 = new byte[11];
                    value10[0] = (byte) 0xff;
                    value10[1] = (byte) 0xff;
                    value10[2] = (byte) 0xff;
                    value10[3] = (byte) 0xff;
                    value10[4] = (byte) 0x05;
                    value10[5] = (byte) 0x00;
                    value10[6] = (byte) 0x00;
                    value10[7] = (byte) 0x00;
                    value10[8] = (byte) 0x16;
                    value10[9] = (byte) 0x56;
                    value10[10] = (byte) 0xce;
                    target_chara.setValue(value10);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
//                    pinlvCount = 2;
//                    toubuCount = 2;
//                    zhubuCount = 2;
//                    foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 15) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
                    isCanTauch10 = true;
                    isCanTauch20 =false;
                    isCanTauch30=false;

                } else {
                    img_10mins.setBackgroundResource(R.drawable.ic_10min);
                    img_20mins.setBackgroundResource(R.drawable.ic_20min);
                    img_30mins.setBackgroundResource(R.drawable.ic_30min);
                    byte[] value90 = new byte[11];
                    value90[0] = (byte) 0xff;
                    value90[1] = (byte) 0xff;
                    value90[2] = (byte) 0xff;
                    value90[3] = (byte) 0xff;
                    value90[4] = (byte) 0x05;
                    value90[5] = (byte) 0x00;
                    value90[6] = (byte) 0x00;
                    value90[7] = (byte) 0x00;
                    value90[8] = (byte) 0x1c;
                    value90[9] = (byte) 0xd6;
                    value90[10] = (byte) 0xc9;
                    target_chara.setValue(value90);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
//                    pinlvCount = 0;
//                    toubuCount = 0;
//                    zhubuCount = 0;
//                    foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 15) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
                    isCanTauch10 = false;
                    isCanTauch20 =false;
                    isCanTauch30=false;
                }
                break;
            case R.id.img_20min:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }
                if (!isCanTauch20) {
                    img_10mins.setBackgroundResource(R.drawable.ic_10min);
                    img_20mins.setBackgroundResource(R.drawable.light30);
                    img_30mins.setBackgroundResource(R.drawable.ic_30min);
                    byte[] value20 = new byte[11];
                    value20[0] = (byte) 0xff;
                    value20[1] = (byte) 0xff;
                    value20[2] = (byte) 0xff;
                    value20[3] = (byte) 0xff;
                    value20[4] = (byte) 0x05;
                    value20[5] = (byte) 0x00;
                    value20[6] = (byte) 0x00;
                    value20[7] = (byte) 0x00;
                    value20[8] = (byte) 0x17;
                    value20[9] = (byte) 0x97;
                    value20[10] = (byte) 0x0e;
                    target_chara.setValue(value20);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
//                    pinlvCount = 2;
//                    toubuCount = 2;
//                    zhubuCount = 2;
//                    foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 15) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
                    isCanTauch20 = true;
                    isCanTauch10=false;
                    isCanTauch30=false;
                } else {
                    img_10mins.setBackgroundResource(R.drawable.ic_10min);
                    img_20mins.setBackgroundResource(R.drawable.ic_20min);
                    img_30mins.setBackgroundResource(R.drawable.ic_30min);
                    byte[] value900 = new byte[11];
                    value900[0] = (byte) 0xff;
                    value900[1] = (byte) 0xff;
                    value900[2] = (byte) 0xff;
                    value900[3] = (byte) 0xff;
                    value900[4] = (byte) 0x05;
                    value900[5] = (byte) 0x00;
                    value900[6] = (byte) 0x00;
                    value900[7] = (byte) 0x00;
                    value900[8] = (byte) 0x1c;
                    value900[9] = (byte) 0xd6;
                    value900[10] = (byte) 0xc9;
                    target_chara.setValue(value900);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
//                    pinlvCount = 0;
//                    toubuCount = 0;
//                    zhubuCount = 0;
//                    foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 15) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
                    isCanTauch20 = false;
                    isCanTauch10=false;
                    isCanTauch30=false;
                }
                break;
            case R.id.img_30min:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }

                if (!isCanTauch30) {
                    img_10mins.setBackgroundResource(R.drawable.ic_10min);
                    img_20mins.setBackgroundResource(R.drawable.ic_20min);
                    img_30mins.setBackgroundResource(R.drawable.light20);
                    byte[] value30 = new byte[11];
                    value30[0] = (byte) 0xff;
                    value30[1] = (byte) 0xff;
                    value30[2] = (byte) 0xff;
                    value30[3] = (byte) 0xff;
                    value30[4] = (byte) 0x05;
                    value30[5] = (byte) 0x00;
                    value30[6] = (byte) 0x00;
                    value30[7] = (byte) 0x00;
                    value30[8] = (byte) 0x18;
                    value30[9] = (byte) 0xd7;
                    value30[10] = (byte) 0x0a;
                    target_chara.setValue(value30);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    isCanTauch30 = true;
                    isCanTauch10 = false;
                    isCanTauch20 = false;
//                    pinlvCount = 2;
//                    toubuCount = 2;
//                    zhubuCount = 2;
//                    foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 15) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
                } else {
                    img_10mins.setBackgroundResource(R.drawable.ic_10min);
                    img_20mins.setBackgroundResource(R.drawable.ic_20min);
                    img_30mins.setBackgroundResource(R.drawable.ic_30min);
                    byte[] value9000 = new byte[11];
                    value9000[0] = (byte) 0xff;
                    value9000[1] = (byte) 0xff;
                    value9000[2] = (byte) 0xff;
                    value9000[3] = (byte) 0xff;
                    value9000[4] = (byte) 0x05;
                    value9000[5] = (byte) 0x00;
                    value9000[6] = (byte) 0x00;
                    value9000[7] = (byte) 0x00;
                    value9000[8] = (byte) 0x1c;
                    value9000[9] = (byte) 0xd6;
                    value9000[10] = (byte) 0xc9;
                    target_chara.setValue(value9000);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
//                    pinlvCount = 0;
//                    toubuCount = 0;
//                    zhubuCount = 0;
//                    foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 15) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));
//                    toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                            wid * (zhubuCount * 20) / (100),
//                            foot_fg.getLayoutParams().height
//                    ));

                    isCanTauch30 = false;
                    isCanTauch10 =false;
                    isCanTauch20 =false;
                }
                break;
            case R.id.img_pl_down:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }
                byte[] value1 = new byte[11];
                value1[0] = (byte) 0xff;
                value1[1] = (byte) 0xff;
                value1[2] = (byte) 0xff;
                value1[3] = (byte) 0xff;
                value1[4] = (byte) 0x05;
                value1[5] = (byte) 0x00;
                value1[6] = (byte) 0x00;
                value1[7] = (byte) 0x00;
                value1[8] = (byte) 0x15;
                value1[9] = (byte) 0x16;
                value1[10] = (byte) 0xcf;
                target_chara.setValue(value1);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                if (pinlvCount > 0) {
                    pinlvCount--;
                } else {
//                    byte[] value90 = new byte[8];
//                    value90[0] = (byte) 0xff;
//                    value90[1] = (byte) 0x05;
//                    value90[2] = (byte) 0x00;
//                    value90[3] = (byte) 0x00;
//                    value90[4] = (byte) 0x00;
//                    value90[5] = (byte) 0x1c;
//                    value90[6] = (byte) 0xd9;
//                    value90[7] = (byte) 0xdd;
//                    target_chara.setValue(value90);
//                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                }

                if (pinlvCount < 0) {
                    return;
                }
//                fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                        wid * (pinlvCount * 15) / (100),
//                        fg.getLayoutParams().height
//                ));
                break;
            case R.id.img_pl_up:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }

                byte[] value2 = new byte[11];
                value2[0] = (byte) 0xff;
                value2[1] = (byte) 0xff;
                value2[2] = (byte) 0xff;
                value2[3] = (byte) 0xff;
                value2[4] = (byte) 0x05;
                value2[5] = (byte) 0x00;
                value2[6] = (byte) 0x00;
                value2[7] = (byte) 0x00;
                value2[8] = (byte) 0x14;
                value2[9] = (byte) 0xd7;
                value2[10] = (byte) 0x0f;
                target_chara.setValue(value2);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                if (pinlvCount < 4)
                    pinlvCount++;

                if (pinlvCount == 10) {
                    return;
                }
//                fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                        wid * (pinlvCount * 15) / (100),
//                        fg.getLayoutParams().height
//                ));
                break;
            case R.id.img_head_down:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }


                byte[] value3 = new byte[11];
                value3[0] = (byte) 0xff;
                value3[1] = (byte) 0xff;
                value3[2] = (byte) 0xff;
                value3[3] = (byte) 0xff;
                value3[4] = (byte) 0x05;
                value3[5] = (byte) 0x00;
                value3[6] = (byte) 0x00;
                value3[7] = (byte) 0x00;
                value3[8] = (byte) 0x11;
                value3[9] = (byte) 0x17;
                value3[10] = (byte) 0x0c;
                target_chara.setValue(value3);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                if (toubuCount > 0) {
                    toubuCount--;
                } else {
//                    byte[] value90 = new byte[8];
//                    value90[0] = (byte) 0xff;
//                    value90[1] = (byte) 0x05;
//                    value90[2] = (byte) 0x00;
//                    value90[3] = (byte) 0x00;
//                    value90[4] = (byte) 0x00;
//                    value90[5] = (byte) 0x1c;
//                    value90[6] = (byte) 0xd9;
//                    value90[7] = (byte) 0xdd;
//                    target_chara.setValue(value90);
//                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                }

                if (toubuCount < 0) {
                    return;
                }
//                toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                        wid * (toubuCount * 20) / (100),
//                        toubu_fg.getLayoutParams().height
//                ));
                break;
            case R.id.img_head_up:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }

                byte[] value4 = new byte[11];
                value4[0] = (byte) 0xff;
                value4[1] = (byte) 0xff;
                value4[2] = (byte) 0xff;
                value4[3] = (byte) 0xff;
                value4[4] = (byte) 0x05;
                value4[5] = (byte) 0x00;
                value4[6] = (byte) 0x00;
                value4[7] = (byte) 0x00;
                value4[8] = (byte) 0x10;
                value4[9] = (byte) 0xd6;
                value4[10] = (byte) 0xcc;
                target_chara.setValue(value4);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                if (toubuCount < 3)
                    toubuCount++;


                if (toubuCount == 10) {
                    return;
                }
//                toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                        wid * (toubuCount * 20) / (100),
//                        toubu_fg.getLayoutParams().height
//                ));
                break;
            case R.id.img_foot_down:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }


                byte[] value5 = new byte[11];
                value5[0] = (byte) 0xff;
                value5[1] = (byte) 0xff;
                value5[2] = (byte) 0xff;
                value5[3] = (byte) 0xff;
                value5[4] = (byte) 0x05;
                value5[5] = (byte) 0x00;
                value5[6] = (byte) 0x00;
                value5[7] = (byte) 0x00;
                value5[8] = (byte) 0x13;
                value5[9] = (byte) 0x96;
                value5[10] = (byte) 0xcd;
                target_chara.setValue(value5);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                if (zhubuCount > 0) {
                    zhubuCount--;
                } else {
//                    byte[] value90 = new byte[8];
//                    value90[0] = (byte) 0xff;
//                    value90[1] = (byte) 0x05;
//                    value90[2] = (byte) 0x00;
//                    value90[3] = (byte) 0x00;
//                    value90[4] = (byte) 0x00;
//                    value90[5] = (byte) 0x1c;
//                    value90[6] = (byte) 0xd9;
//                    value90[7] = (byte) 0xdd;
//                    target_chara.setValue(value90);
//                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                }

                if (zhubuCount < 0) {
                    return;
                }
//                foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                        wid * (zhubuCount * 20) / (100),
//                        foot_fg.getLayoutParams().height
//                ));
                break;
            case R.id.img_foot_up:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }

                byte[] value6 = new byte[11];
                value6[0] = (byte) 0xff;
                value6[1] = (byte) 0xff;
                value6[2] = (byte) 0xff;
                value6[3] = (byte) 0xff;
                value6[4] = (byte) 0x05;
                value6[5] = (byte) 0x00;
                value6[6] = (byte) 0x00;
                value6[7] = (byte) 0x00;
                value6[8] = (byte) 0x12;
                value6[9] = (byte) 0x57;
                value6[10] = (byte) 0x0d;
                target_chara.setValue(value6);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                if (zhubuCount < 3)
                    zhubuCount++;
                Log.e("jjjj", "秒数：" + zhubuCount);
                if (zhubuCount == 10) {
                    return;
                }
//                foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
//                        wid * (zhubuCount * 20) / (100),
//                        foot_fg.getLayoutParams().height
//                ));
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
                //   获取设备的所有蓝牙服务
                //    displayGattServices(MyApplication.getInstance().mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //处理发送过来的数据  (//有效数据)

               // displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String data = bundle.getString(BluetoothLeService.EXTRA_DATA);
                    if (data != null) {
                        displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                        LogUtils.e("==按摩  接收设备返回的数据==", "");
                    }
                }
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
        LogUtils.e("==按摩  接收设备返回的数据==", "" + rev_string);

        if (rev_string.indexOf("01 00")!=-1) {
            toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (0 * 20) / (100),
                    toubu_fg.getLayoutParams().height
            ));
        }else if(rev_string. indexOf("01 1E")!=-1){
                toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (1 * 20) / (100),
                    toubu_fg.getLayoutParams().height
            ));
        }else if(rev_string. indexOf("01 1F")!=-1){
            toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (2 * 20) / (100),
                    toubu_fg.getLayoutParams().height
            ));
        } else if(rev_string. indexOf("01 20")!=-1){
            toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (3 * 20) / (100),
                    toubu_fg.getLayoutParams().height
            ));
        }else if(rev_string. indexOf("02 00")!=-1){
            foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (0 * 20) / (100),
                    foot_fg.getLayoutParams().height
            ));
        }else if(rev_string. indexOf("02 21")!=-1){
            foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (1 * 20) / (100),
                    foot_fg.getLayoutParams().height
            ));
        }else if(rev_string. indexOf("02 22")!=-1){
            foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (2 * 20) / (100),
                    foot_fg.getLayoutParams().height
            ));
        }else if(rev_string. indexOf("02 23")!=-1){
            foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (3 * 20) / (100),
                    foot_fg.getLayoutParams().height
            ));
        }
        else if(rev_string. indexOf("03 24")!=-1){
            fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (1 * 15) / (100),
                    fg.getLayoutParams().height
            ));
        }else if(rev_string. indexOf("03 25")!=-1){
            fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (2 * 15) / (100),
                    fg.getLayoutParams().height
            ));
        }else if(rev_string. indexOf("03 26")!=-1){
            fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (3 * 15) / (100),
                    fg.getLayoutParams().height
            ));
        }else if(rev_string. indexOf("03 27")!=-1){
            fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (4 * 15) / (100),
                    fg.getLayoutParams().height
            ));
        }else if(rev_string. indexOf("00 1C")!=-1){
            fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (0 * 15) / (100),
                    fg.getLayoutParams().height
            ));
            toubu_fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (0 * 15) / (100),
                    toubu_fg.getLayoutParams().height
            ));
            foot_fg.setLayoutParams(new RelativeLayout.LayoutParams(
                    wid * (0 * 15) / (100),
                    foot_fg.getLayoutParams().height
            ));
        }


    }


    @Override
    public void onTongbukzEvent(boolean show, boolean open) {

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
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
