package com.fenmenbielei.bedsense.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import static android.content.Context.MODE_PRIVATE;

public class HHWeiTiaoFragment extends BaseFragment implements View.OnTouchListener {
    @BindView(R.id.ll_zt)
    LinearLayout ll_zt;
    @BindView(R.id.img_ztups)
    LinearLayout img_ztups;
    @BindView(R.id.img_ztdowns)
    LinearLayout img_ztdowns;
    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.v_one)
    View v_one;
    @BindView(R.id.ll_one)
    LinearLayout ll_one;
    @BindView(R.id.ll_two)
    LinearLayout ll_two;
    @BindView(R.id.ll_three)
    LinearLayout ll_three;
    @BindView(R.id.iv_taiqi)
    LinearLayout iv_taiqi;
    @BindView(R.id.iv_anxia)
    LinearLayout iv_anxia;
    @BindView(R.id.img_light)
    ImageView img_light;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.custom_progress4)
    CircleProgressBar mCustomProgressBar4;
    @BindView(R.id.fl_all)
    FrameLayout fl_all;
    @BindView(R.id.img_back_down)
    LinearLayout img_back_down;
    @BindView(R.id.img_back_up)
    LinearLayout img_back_up;
    @BindView(R.id.img_hip_down)
    LinearLayout img_hip_down;
    @BindView(R.id.img_hip_up)
    LinearLayout img_hip_up;
    @BindView(R.id.img_head_down)
    LinearLayout img_head_down;
    @BindView(R.id.img_head_up)
    LinearLayout img_head_up;
    @BindView(R.id.img_foot_down)
    LinearLayout img_foot_down;
    @BindView(R.id.img_foot_up)
    LinearLayout img_foot_up;
    @BindView(R.id.img_bt_down)
    LinearLayout img_bt_down;
    @BindView(R.id.img_bt_up)
    LinearLayout img_bt_up;
    @BindView(R.id.name_beitui)
    TextView name_beitui;
    int allType = 0;
    //背部
    @BindView(R.id.ll_beibu)
    LinearLayout ll_beibu;
    //背部按钮
    @BindView(R.id.rl_beibus)
    RelativeLayout rl_beibus;
    //背腿
    @BindView(R.id.ll_beitui)
    LinearLayout ll_beitui;
    @BindView(R.id.img_foot_ups)
    LinearLayout img_foot_ups;
    @BindView(R.id.img_foot_downs)
    LinearLayout img_foot_downs;
    @BindView(R.id.ll_jiao)
    LinearLayout ll_jiao;
    @BindView(R.id.ll_weitiao)
    LinearLayout ll_weitiao;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.rl_beibu)
    RelativeLayout rlBeibu;
    @BindView(R.id.imageViews)
    ImageView imageViews;
    @BindView(R.id.rl_toutui)
    RelativeLayout rlToutui;
    @BindView(R.id.rl_toubu)
    RelativeLayout rlToubu;
    @BindView(R.id.rl_jiaobu)
    RelativeLayout rlJiaobu;
    @BindView(R.id.tv_beibu)
    TextView tvBeibu;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tv_beitui)
    TextView tvBeitui;
    @BindView(R.id.tv_toubu)
    TextView tvToubu;
    @BindView(R.id.tv_jiaobu)
    TextView tvJiaobu;
    private boolean isCli = false;
    private Handler mhandler = new Handler();
    private int second = 0;
    //蓝牙特征值
    private static String t = "";
    private static BluetoothGattCharacteristic target_chara = null;

    public static HHWeiTiaoFragment newInstance(String i) {
        HHWeiTiaoFragment homeFragment2 = new HHWeiTiaoFragment();
        t = i;
        return homeFragment2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hhwei_tiao, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView() {
        Log.e("=====hoah====", "" + t);
        img_back_down.setOnTouchListener(this);
        img_back_up.setOnTouchListener(this);
        img_foot_up.setOnTouchListener(this);
        img_bt_up.setOnTouchListener(this);
        img_bt_down.setOnTouchListener(this);
        img_foot_down.setOnTouchListener(this);
        img_foot_ups.setOnTouchListener(this);
        img_foot_downs.setOnTouchListener(this);
        img_head_down.setOnTouchListener(this);
        img_head_up.setOnTouchListener(this);
        img_hip_down.setOnTouchListener(this);
        img_hip_up.setOnTouchListener(this);
        fl_all.setOnTouchListener(this);
        iv_taiqi.setOnTouchListener(this);
        iv_anxia.setOnTouchListener(this);
        img_ztups.setOnTouchListener(this);
        img_ztdowns.setOnTouchListener(this);
        rlBeibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCli) {
                    byte[] value314 = new byte[11];
                    value314[0] = (byte) 0xff;
                    value314[1] = (byte) 0xff;
                    value314[2] = (byte) 0xff;
                    value314[3] = (byte) 0xff;
                    value314[4] = (byte) 0x05;
                    value314[5] = (byte) 0x00;
                    value314[6] = (byte) 0x00;
                    value314[7] = (byte) 0x00;
                    value314[8] = (byte) 0xe7;
                    value314[9] = (byte) 0x97;
                    value314[10] = (byte) 0x4a;
                    byte[] value3114 = new byte[11];
                    value3114[0] = (byte) 0xff;
                    value3114[1] = (byte) 0xff;
                    value3114[2] = (byte) 0xff;
                    value3114[3] = (byte) 0xff;
                    value3114[4] = (byte) 0x05;
                    value3114[5] = (byte) 0x00;
                    value3114[6] = (byte) 0x05;
                    value3114[7] = (byte) 0x00;
                    value3114[8] = (byte) 0xe4;
                    value3114[9] = (byte) 0xc7;
                    value3114[10] = (byte) 0x4a;
                    if(t.equals("1")||t.equals("2")||t.equals("5")||t.equals("4")) {
                        target_chara.setValue(value3114);
                    }else {
                        target_chara.setValue(value314);
                    }
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                }
            }
        });
        ll_beitui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCli) {
                    byte[] value335 = new byte[11];
                    value335[0] = (byte) 0xff;
                    value335[1] = (byte) 0xff;
                    value335[2] = (byte) 0xff;
                    value335[3] = (byte) 0xff;
                    value335[4] = (byte) 0x05;
                    value335[5] = (byte) 0x00;
                    value335[6] = (byte) 0x00;
                    value335[7] = (byte) 0x00;
                    value335[8] = (byte) 0xe4;
                    value335[9] = (byte) 0xd7;
                    value335[10] = (byte) 0x4b;
                    byte[] value3335 = new byte[11];
                    value3335[0] = (byte) 0xff;
                    value3335[1] = (byte) 0xff;
                    value3335[2] = (byte) 0xff;
                    value3335[3] = (byte) 0xff;
                    value3335[4] = (byte) 0x05;
                    value3335[5] = (byte) 0x00;
                    value3335[6] = (byte) 0x05;
                    value3335[7] = (byte) 0x00;
                    value3335[8] = (byte) 0xe8;
                    value3335[9] = (byte) 0xc7;
                    value3335[10] = (byte) 0x4f;
                    if(!t.equals("1")) {
                        target_chara.setValue(value335);
                    }else {
                        target_chara.setValue(value3335);
                    }
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                }
            }
        });
        ll_beibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCli) {
                    byte[] value315 = new byte[11];
                    value315[0] = (byte) 0xff;
                    value315[1] = (byte) 0xff;
                    value315[2] = (byte) 0xff;
                    value315[3] = (byte) 0xff;
                    value315[4] = (byte) 0x05;
                    value315[5] = (byte) 0x00;
                    value315[6] = (byte) 0x05;
                    value315[7] = (byte) 0x00;
                    value315[8] = (byte) 0xe6;
                    value315[9] = (byte) 0x46;
                    value315[10] = (byte) 0x8b;
                    target_chara.setValue(value315);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                }
            }
        });
        rlToubu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCli) {
                    byte[] value316 = new byte[11];
                    value316[0] = (byte) 0xff;
                    value316[1] = (byte) 0xff;
                    value316[2] = (byte) 0xff;
                    value316[3] = (byte) 0xff;
                    value316[4] = (byte) 0x05;
                    value316[5] = (byte) 0x00;
                    value316[6] = (byte) 0x05;
                    value316[7] = (byte) 0x00;
                    value316[8] = (byte) 0xe3;
                    value316[9] = (byte) 0x86;
                    value316[10] = (byte) 0x88;
                    byte[] value3116 = new byte[11];
                    value3116[0] = (byte) 0xff;
                    value3116[1] = (byte) 0xff;
                    value3116[2] = (byte) 0xff;
                    value3116[3] = (byte) 0xff;
                    value3116[4] = (byte) 0x05;
                    value3116[5] = (byte) 0x00;
                    value3116[6] = (byte) 0x05;
                    value3116[7] = (byte) 0x00;
                    value3116[8] = (byte) 0xe6;
                    value3116[9] = (byte) 0x46;
                    value3116[10] = (byte) 0x8b;
                    if(!t.equals("1")) {
                        target_chara.setValue(value316);
                    }else {
                        target_chara.setValue(value3116);
                    }
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                }
            }
        });
        rlJiaobu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCli) {
                    byte[] value317 = new byte[11];
                    value317[0] = (byte) 0xff;
                    value317[1] = (byte) 0xff;
                    value317[2] = (byte) 0xff;
                    value317[3] = (byte) 0xff;
                    value317[4] = (byte) 0x05;
                    value317[5] = (byte) 0x00;
                    value317[6] = (byte) 0x05;
                    value317[7] = (byte) 0x00;
                    value317[8] = (byte) 0xe5;
                    value317[9] = (byte) 0x06;
                    value317[10] = (byte) 0x8a;
                    target_chara.setValue(value317);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                }
            }
        });


        mCustomProgressBar4.setProgress(100);

        ll_weitiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target_chara = MyApplication.getInstance().gattCharacteristic;

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
            }
        });

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
            LogUtils.e("====开始=== 进来了", "秒数：" + second);
            Message msg = new Message();
            msg.what = second;
            handler2.sendEmptyMessageDelayed(msg.what, 1000);
            LogUtils.e("=====剩余时间====", "" + second);
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
                //displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String data = bundle.getString(BluetoothLeService.EXTRA_DATA);
                    if (data != null) {
                        displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                        Log.e("==处理发送过来的数据==", "" + intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                    }
                }
//                Log.e("==处理发送过来的数据==", "" + intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
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
        if (rev_string.contains("FF FF FF FF 05 00 00 00 E7 97 4A ")||rev_string.contains("FF FF FF FF 05 00 05 00 E4 C7 4A ")) {
            img_light.setImageDrawable(null);
            img_light.setBackgroundResource(R.drawable.ic_beibu);
            tv_title.setText(R.string.quanshen);
        }
        if (rev_string.contains("FF FF FF FF 05 00 05 00 E4 D7 4B ")) {
            if (t.equals("4") || t.equals("5")) {
                img_light.setImageDrawable(null);
                img_light.setBackgroundResource(R.drawable.ic_tunbu_light);
                tv_title.setText(R.string.tunbu);
            } else {
                img_light.setImageDrawable(null);
                img_light.setBackgroundResource(R.drawable.ic_yijiankandianshi);
                tv_title.setText(R.string.yaoxun);
            }
        }
//        if (rev_string.equals("FF FF FF FF 05 00 00 00 00 D7 00 ")) {
//            img_light.setBackgroundResource(R.drawable.ic_yijianfuyuan_light);
//            tv_title.setText(R.string.tou);
//        }
        if (rev_string.equals("FF FF FF FF 05 00 05 00 E3 86 88 ")) {
            img_light.setImageDrawable(null);
            img_light.setBackgroundResource(R.drawable.ic_toubu);
            tv_title.setText(R.string.tou);
        }
        if (rev_string.equals("FF FF FF FF 05 00 05 00 E5 06 8A ")) {
            img_light.setImageDrawable(null);
            img_light.setBackgroundResource(R.drawable.ic_jiaobu);
            tv_title.setText(R.string.jiaobu);

        }
        if (rev_string.equals("FF FF FF FF 05 00 05 00 E6 46 8B ")) {
            if (t.equals("4") || t.equals("5")) {
                img_light.setImageDrawable(null);
                img_light.setBackgroundResource(R.drawable.ic_tunbu);
                tv_title.setText(R.string.tunbu);
            }else {
                img_light.setImageDrawable(null);
                img_light.setBackgroundResource(R.drawable.ic_tunbu);
                tv_title.setText(R.string.yaoxun);
            }}
        if (rev_string.equals("FF FF FF FF 05 00 05 00 E8 C7 4F ")) {
            img_light.setImageDrawable(null);
            img_light.setBackgroundResource(R.drawable.ic_yijiankandianshi);
            tv_title.setText(R.string.beitui);
        }

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
        SharedPreferences read = getActivity().getSharedPreferences("type", MODE_PRIVATE);
        String value = read.getString("TYPE", "");
        t = value;

        if (value.equals("1")) {
            ll_zt.setVisibility(View.GONE);
            ll_jiao.setVisibility(View.GONE);
            ll_beibu.setVisibility(View.GONE);
            ll_beitui.setVisibility(View.VISIBLE);
            //改动
            tvToubu.setText(R.string.yao);
            imageHead.setImageResource(R.drawable.ic_tunbu);
            //
        } else if (value.equals("2")) {
            ll_zt.setVisibility(View.GONE);
            ll_jiao.setVisibility(View.GONE);
            ll_beibu.setVisibility(View.VISIBLE);
            ll_beitui.setVisibility(View.GONE);
            name_beitui.setText(R.string.yao);
        }else if(value.equals("7")){
            tvToubu.setText(R.string.head);
            ll_zt.setVisibility(View.GONE);
            ll_jiao.setVisibility(View.VISIBLE);
            ll_beibu.setVisibility(View.GONE);
            ll_beitui.setVisibility(View.GONE);
            name_beitui.setText(R.string.yao);
            ll_two.setVisibility(View.GONE);
            v_one.setVisibility(View.VISIBLE);
        }else if(value.equals("8")){
            tvToubu.setText(R.string.head);
            ll_jiao.setVisibility(View.GONE);
            ll_zt.setVisibility(View.VISIBLE);
            ll_beibu.setVisibility(View.GONE);
            ll_beitui.setVisibility(View.GONE);
            name_beitui.setText(R.string.yao);
            ll_two.setVisibility(View.GONE);
            v_one.setVisibility(View.VISIBLE);
        } else {
            tvToubu.setText(R.string.head);
            ll_zt.setVisibility(View.GONE);
            ll_jiao.setVisibility(View.GONE);
            ll_beibu.setVisibility(View.VISIBLE);
            ll_beitui.setVisibility(View.GONE);
            if (!isCli) {
                if (t.equals("4") || t.equals("5")) {
                    name_beitui.setText(R.string.hip);
                }else if(t.equals("9")){
                    imageHead.setImageResource(R.drawable.ic_yijianfuyuan);
                    tvToubu.setText(R.string.lift);
                } else {
                    name_beitui.setText(R.string.yao);
                }
            } else {
                if (t.equals("4") || t.equals("5")) {
                    name_beitui.setText(R.string.tunbu);
                }else if(t.equals("9")){
                    imageHead.setImageResource(R.drawable.ic_yijianfuyuan);
                    tvToubu.setText(R.string.lift);
                } else {
                    name_beitui.setText(R.string.yaoxun);
                }
            }
            if (value.equals("6")) {
                ll_one.setVisibility(View.GONE);
                ll_two.setVisibility(View.GONE);
                ll_three.setVisibility(View.VISIBLE);
            }
        }
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        target_chara = MyApplication.getInstance().gattCharacteristic;
        switch (view.getId()) {
            //头部
            case R.id.img_head_down:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    LogUtils.e("==按下==", "action_down");
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);

                    allType = 3;
                    byte[] value1 = new byte[11];
                    value1[0] = (byte) 0xff;
                    value1[1] = (byte) 0xff;
                    value1[2] = (byte) 0xff;
                    value1[3] = (byte) 0xff;
                    value1[4] = (byte) 0x05;
                    value1[5] = (byte) 0x00;
                    value1[6] = (byte) 0x00;
                    value1[7] = (byte) 0x00;
                    value1[8] = (byte) 0x02;
                    value1[9] = (byte) 0x56;
                    value1[10] = (byte) 0xc1;
                    byte[] value10 = new byte[11];
                    value10[0] = (byte) 0xff;
                    value10[1] = (byte) 0xff;
                    value10[2] = (byte) 0xff;
                    value10[3] = (byte) 0xff;
                    value10[4] = (byte) 0x05;
                    value10[5] = (byte) 0x00;
                    value10[6] = (byte) 0x00;
                    value10[7] = (byte) 0x00;
                    value10[8] = (byte) 0x0e;
                    value10[9] = (byte) 0x56;
                    value10[10] = (byte) 0xc4;
                    if(!"1".equals(t)) {
                        if(!"9".equals(t)) {
                            Glide.with(HHWeiTiaoFragment.this).load(R.drawable.toubutzf)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                            tv_title.setText(getResources().getString(R.string.head));
                        }else {
                            Glide.with(HHWeiTiaoFragment.this).load(R.drawable.ic_yijianfuyuan)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                            tv_title.setText(getResources().getString(R.string.lift));
                        }
                        target_chara.setValue(value1);
                    }else {
                        Glide.with(HHWeiTiaoFragment.this).load(R.drawable.tunbutz)
                                .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                        tv_title.setText(getResources().getString(R.string.yao));
                        target_chara.setValue(value10);
                    }

                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_toubu);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_toubu);
                }
                break;
            case R.id.img_head_up:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    LogUtils.e("==按下==", "action_down");
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);

                    allType = 3;
                    byte[] value2 = new byte[11];
                    value2[0] = (byte) 0xff;
                    value2[1] = (byte) 0xff;
                    value2[2] = (byte) 0xff;
                    value2[3] = (byte) 0xff;
                    value2[4] = (byte) 0x05;
                    value2[5] = (byte) 0x00;
                    value2[6] = (byte) 0x00;
                    value2[7] = (byte) 0x00;
                    value2[8] = (byte) 0x01;
                    value2[9] = (byte) 0x16;
                    value2[10] = (byte) 0xc0;
                    byte[] value20 = new byte[11];
                    value20[0] = (byte) 0xff;
                    value20[1] = (byte) 0xff;
                    value20[2] = (byte) 0xff;
                    value20[3] = (byte) 0xff;
                    value20[4] = (byte) 0x05;
                    value20[5] = (byte) 0x00;
                    value20[6] = (byte) 0x00;
                    value20[7] = (byte) 0x00;
                    value20[8] = (byte) 0x0d;
                    value20[9] = (byte) 0x16;
                    value20[10] = (byte) 0xc5;
                    if(!"1".equals(t)) {
                        if(!"9".equals(t)) {
                            Glide.with(HHWeiTiaoFragment.this).load(R.drawable.toubutz)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                            tv_title.setText(getResources().getString(R.string.head));
                        }else {
                            Glide.with(HHWeiTiaoFragment.this).load(R.drawable.ic_yijianfuyuan)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                            tv_title.setText(getResources().getString(R.string.lift));
                        }
                        target_chara.setValue(value2);
                    }else {
                        Glide.with(HHWeiTiaoFragment.this).load(R.drawable.tunbutz)
                                .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                        tv_title.setText(getResources().getString(R.string.yao));
                        target_chara.setValue(value20);
                    }
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);

                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_toubu);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_toubu);

                }
                break;
            //mq脚部调整
            case R.id.img_foot_downs:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);
                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.jiaobutzf)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.footer));
                    allType = 4;
                    byte[] value3 = new byte[11];
                    value3[0] = (byte) 0xff;
                    value3[1] = (byte) 0xff;
                    value3[2] = (byte) 0xff;
                    value3[3] = (byte) 0xff;
                    value3[4] = (byte) 0x05;
                    value3[5] = (byte) 0x00;
                    value3[6] = (byte) 0x00;
                    value3[7] = (byte) 0x00;
                    value3[8] = (byte) 0x07;
                    value3[9] = (byte) 0x96;
                    value3[10] = (byte) 0xc2;
                    target_chara.setValue(value3);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_jiaobu);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_jiaobu);
                }
                break;
            //脚部调整
            case R.id.img_foot_down:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);
                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.jiaobutzf)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.footer));
                    allType = 4;
                    byte[] value3 = new byte[11];
                    value3[0] = (byte) 0xff;
                    value3[1] = (byte) 0xff;
                    value3[2] = (byte) 0xff;
                    value3[3] = (byte) 0xff;
                    value3[4] = (byte) 0x05;
                    value3[5] = (byte) 0x00;
                    value3[6] = (byte) 0x00;
                    value3[7] = (byte) 0x00;
                    value3[8] = (byte) 0x07;
                    value3[9] = (byte) 0x96;
                    value3[10] = (byte) 0xc2;
                    target_chara.setValue(value3);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_jiaobu);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_jiaobu);
                }
                break;
            case R.id.img_foot_up:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);
                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.jiaobutz)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.footer));
                    allType = 4;
                    byte[] value4 = new byte[11];
                    value4[0] = (byte) 0xff;
                    value4[1] = (byte) 0xff;
                    value4[2] = (byte) 0xff;
                    value4[3] = (byte) 0xff;
                    value4[4] = (byte) 0x05;
                    value4[5] = (byte) 0x00;
                    value4[6] = (byte) 0x00;
                    value4[7] = (byte) 0x00;
                    value4[8] = (byte) 0x06;
                    value4[9] = (byte) 0x57;
                    value4[10] = (byte) 0x02;
                    target_chara.setValue(value4);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_jiaobu);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_jiaobu);
                }
                break;
            case R.id.img_foot_ups:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);
                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.jiaobutz)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.footer));
                    allType = 4;
                    byte[] value4 = new byte[11];
                    value4[0] = (byte) 0xff;
                    value4[1] = (byte) 0xff;
                    value4[2] = (byte) 0xff;
                    value4[3] = (byte) 0xff;
                    value4[4] = (byte) 0x05;
                    value4[5] = (byte) 0x00;
                    value4[6] = (byte) 0x00;
                    value4[7] = (byte) 0x00;
                    value4[8] = (byte) 0x06;
                    value4[9] = (byte) 0x57;
                    value4[10] = (byte) 0x02;
                    target_chara.setValue(value4);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_jiaobu);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_jiaobu);
                }
                break;
            //背腿上
            case R.id.img_bt_up:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);
                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.tuiup)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.bt));
                    allType = 2;
                    byte[] value10 = new byte[11];
                    value10[0] = (byte) 0xff;
                    value10[1] = (byte) 0xff;
                    value10[2] = (byte) 0xff;
                    value10[3] = (byte) 0xff;
                    value10[4] = (byte) 0x05;
                    value10[5] = (byte) 0x00;
                    value10[6] = (byte) 0x00;
                    value10[7] = (byte) 0x00;
                    value10[8] = (byte) 0x2b;
                    value10[9] = (byte) 0x97;
                    value10[10] = (byte) 0x1f;
                    target_chara.setValue(value10);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_yijiankandianshi);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_yijiankandianshi);
                }
                break;
            //背腿下
            case R.id.img_bt_down:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);
                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.tuidow)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.bt));
                    allType = 2;
                    byte[] value10 = new byte[11];
                    value10[0] = (byte) 0xff;
                    value10[1] = (byte) 0xff;
                    value10[2] = (byte) 0xff;
                    value10[3] = (byte) 0xff;
                    value10[4] = (byte) 0x05;
                    value10[5] = (byte) 0x00;
                    value10[6] = (byte) 0x00;
                    value10[7] = (byte) 0x00;
                    value10[8] = (byte) 0x2c;
                    value10[9] = (byte) 0xd6;
                    value10[10] = (byte) 0xdd;
                    target_chara.setValue(value10);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_yijiankandianshi);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_yijiankandianshi);
                }
                break;
            //臀部调整
            case R.id.img_hip_down:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);
                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.tunbutzf)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    if (t.equals("2")) {
                        tv_title.setText(getResources().getString(R.string.yao));
                    } else {
                        tv_title.setText(getResources().getString(R.string.hip));
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
                    value5[8] = (byte) 0x0e;
                    value5[9] = (byte) 0x56;
                    value5[10] = (byte) 0xc4;
                    target_chara.setValue(value5);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_tunbu_light);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_tunbu_light);
                }

                break;
            case R.id.img_hip_up:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);
                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.tunbutz)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    if (t.equals("2")) {
                        tv_title.setText(getResources().getString(R.string.yao));
                    } else {
                        tv_title.setText(getResources().getString(R.string.hip));
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
                    value6[8] = (byte) 0x0d;
                    value6[9] = (byte) 0x16;
                    value6[10] = (byte) 0xc5;
                    target_chara.setValue(value6);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_tunbu_light);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_tunbu_light);
                }
                break;
            //背部调整
            case R.id.img_back_down:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ) {
                    LogUtils.e("==按下==", "action_down");
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                        return false;
                    }
                    img_light.setBackgroundResource(R.drawable.timg);

                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.beibutz)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.back));
                    allType = 1;
                    byte[] value7 = new byte[11];
                    value7[0] = (byte) 0xff;
                    value7[1] = (byte) 0xff;
                    value7[2] = (byte) 0xff;
                    value7[3] = (byte) 0xff;
                    value7[4] = (byte) 0x05;
                    value7[5] = (byte) 0x00;
                    value7[6] = (byte) 0x00;
                    value7[7] = (byte) 0x00;
                    value7[8] = (byte) 0x04;
                    value7[9] = (byte) 0xD6;
                    value7[10] = (byte) 0xc3;
                    target_chara.setValue(value7);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    LogUtils.e("==按下==", "action_p");
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.timg);
                    img_light.setBackgroundResource(R.drawable.ic_beibu);

                    return true;
                } else {
                    pauseUp();
                    LogUtils.e("==按下==", "action_up");
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.timg);
                    img_light.setBackgroundResource(R.drawable.ic_beibu);
                }
                break;
            case R.id.img_back_up:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ) {
                    LogUtils.e("==抬起==", "action_up");
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);

                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.beibutzf)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.back));
                    allType = 1;
                    byte[] value8 = new byte[11];
                    value8[0] = (byte) 0xff;
                    value8[1] = (byte) 0xff;
                    value8[2] = (byte) 0xff;
                    value8[3] = (byte) 0xff;
                    value8[4] = (byte) 0x05;
                    value8[5] = (byte) 0x00;
                    value8[6] = (byte) 0x00;
                    value8[7] = (byte) 0x00;
                    value8[8] = (byte) 0x03;
                    value8[9] = (byte) 0x97;
                    value8[10] = (byte) 0x01;
                    target_chara.setValue(value8);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_beibu);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_beibu);
                }
                break;
                //kqh上
            case R.id.img_ztups:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.ic_yijianfuyuan);
//                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.jiaobutz)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.zt));
                    allType = 4;
                    byte[] value4 = new byte[11];
                    value4[0] = (byte) 0xff;
                    value4[1] = (byte) 0xff;
                    value4[2] = (byte) 0xff;
                    value4[3] = (byte) 0xff;
                    value4[4] = (byte) 0x05;
                    value4[5] = (byte) 0x00;
                    value4[6] = (byte) 0x00;
                    value4[7] = (byte) 0x00;
                    value4[8] = (byte) 0x06;
                    value4[9] = (byte) 0x57;
                    value4[10] = (byte) 0x02;
                    target_chara.setValue(value4);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();

                    return true;
                } else {
                    pauseUp();
                }
                break;
            case R.id.img_ztdowns:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                        return false;
                    }

//                    img_light.setBackgroundResource(R.drawable.timg);
//                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.jiaobutzf)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    img_light.setBackgroundResource(R.drawable.ic_yijianfuyuan);
                    tv_title.setText(getResources().getString(R.string.zt));
                    allType = 4;
                    byte[] value3 = new byte[11];
                    value3[0] = (byte) 0xff;
                    value3[1] = (byte) 0xff;
                    value3[2] = (byte) 0xff;
                    value3[3] = (byte) 0xff;
                    value3[4] = (byte) 0x05;
                    value3[5] = (byte) 0x00;
                    value3[6] = (byte) 0x00;
                    value3[7] = (byte) 0x00;
                    value3[8] = (byte) 0x07;
                    value3[9] = (byte) 0x96;
                    value3[10] = (byte) 0xc2;
                    target_chara.setValue(value3);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();

                    return true;
                } else {
                    pauseUp();

                }
                break;
            //kq上
            case R.id.iv_taiqi:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ) {
                    LogUtils.e("==抬起==", "action_up");
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);

                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.beibutzf)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.back));
                    allType = 1;
                    byte[] value8 = new byte[11];
                    value8[0] = (byte) 0xff;
                    value8[1] = (byte) 0xff;
                    value8[2] = (byte) 0xff;
                    value8[3] = (byte) 0xff;
                    value8[4] = (byte) 0x05;
                    value8[5] = (byte) 0x00;
                    value8[6] = (byte) 0x00;
                    value8[7] = (byte) 0x00;
                    value8[8] = (byte) 0x03;
                    value8[9] = (byte) 0x97;
                    value8[10] = (byte) 0x01;
                    target_chara.setValue(value8);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_beibu);
                    return true;
                } else {
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.ic_beibu);
                }
                break;
            case R.id.iv_anxia:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ) {
                    LogUtils.e("==按下==", "action_down");
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                        return false;
                    }

                    img_light.setBackgroundResource(R.drawable.timg);

                    Glide.with(HHWeiTiaoFragment.this).load(R.drawable.beibutz)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_light);
                    tv_title.setText(getResources().getString(R.string.back));
                    allType = 1;
                    byte[] value7 = new byte[11];
                    value7[0] = (byte) 0xff;
                    value7[1] = (byte) 0xff;
                    value7[2] = (byte) 0xff;
                    value7[3] = (byte) 0xff;
                    value7[4] = (byte) 0x05;
                    value7[5] = (byte) 0x00;
                    value7[6] = (byte) 0x00;
                    value7[7] = (byte) 0x00;
                    value7[8] = (byte) 0x04;
                    value7[9] = (byte) 0xD6;
                    value7[10] = (byte) 0xc3;
                    target_chara.setValue(value7);
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    return true;
                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    LogUtils.e("==按下==", "action_p");
                    pauseUp();
                    img_light.setImageDrawable(null);
                    img_light.setBackgroundResource(R.drawable.timg);
                    img_light.setBackgroundResource(R.drawable.ic_beibu);

                    return true;
                } else {
                    pauseUp();
                    LogUtils.e("==按下==", "action_up");
                    img_light.setBackgroundResource(R.drawable.timg);
                    img_light.setBackgroundResource(R.drawable.ic_beibu);
                }
                break;
            case R.id.fl_all:
                if(!"9".equals(t)) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LogUtils.e("==按下==", "按");
                        if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                            ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                            return false;
                        }
                        second = 2;
                        myHandler.sendEmptyMessageDelayed(1, 1000);
                        return true;
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (myHandler != null) {
                            myHandler.removeCallbacks(null);
                            myHandler.removeCallbacksAndMessages(null);
                        }
                        return true;
                    }
                }

                break;
        }

        return false;
    }

    //触摸后手抬起出发暂停发送串口码

    private void pauseUp() {
        // 0xff 0x05 0x00 0x00 0x00 0x00  0xD8 0x14
        LogUtils.e("==抬起==", "action_down");
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
        value9[9] = (byte) 0xD7;
        value9[10] = (byte) 0x00;
        target_chara.setValue(value9);
        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
    }

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    second--;
                    if (second < 0) {
                        second = 0;
                    } else if (second == 0) {
                        myHandler.removeCallbacks(null);
                        myHandler.removeCallbacksAndMessages(null);
                        target_chara = MyApplication.getInstance().gattCharacteristic;
                        if(!t.equals("6")&&!t.equals("7")&&!t.equals("8")) {
                            if (isCli) {
                                isCli = false;
                                tv_title.setText("");
                                rlBeibu.setBackgroundResource(R.drawable.ic_weitiao_bg);
                                rlJiaobu.setBackgroundResource(R.drawable.ic_weitiao_bg);
                                rlToubu.setBackgroundResource(R.drawable.ic_weitiao_bg);
                                rlToutui.setBackgroundResource(R.drawable.ic_weitiao_bg);
                                rl_beibus.setBackgroundResource(R.drawable.ic_weitiao_bg);
                                img_back_up.setVisibility(View.VISIBLE);
                                img_back_down.setVisibility(View.VISIBLE);
                                img_bt_up.setVisibility(View.VISIBLE);
                                img_bt_down.setVisibility(View.VISIBLE);
                                img_foot_up.setVisibility(View.VISIBLE);
                                img_foot_down.setVisibility(View.VISIBLE);
                                img_head_up.setVisibility(View.VISIBLE);
                                img_head_down.setVisibility(View.VISIBLE);
                                img_hip_down.setVisibility(View.VISIBLE);
                                img_hip_up.setVisibility(View.VISIBLE);
                                tv_title.setText(R.string.back);
                                if (t.equals("4") || t.equals("5")) {
                                    name_beitui.setText(R.string.hip);
                                } else {
                                    name_beitui.setText(R.string.yao);
                                }
                                tvBeibu.setText(R.string.back);
                                tvBeitui.setText(R.string.bt);
                                if(!t.equals("1")) {
                                    tvToubu.setText(R.string.head);
                                }else {
                                    tvToubu.setText(R.string.yao);
                                    imageHead.setImageResource(R.drawable.ic_tunbu);
                                }
                                tvJiaobu.setText(R.string.footer);
                            } else {
                                isCli = true;
                                tv_title.setText(R.string.quanshen);
                                rlBeibu.setBackgroundResource(R.drawable.ic_kuaijie_normal);
                                rlJiaobu.setBackgroundResource(R.drawable.ic_kuaijie_normal);
                                rlToubu.setBackgroundResource(R.drawable.ic_kuaijie_normal);
                                rlToutui.setBackgroundResource(R.drawable.ic_kuaijie_normal);
                                rl_beibus.setBackgroundResource(R.drawable.ic_kuaijie_normal);
                                img_back_up.setVisibility(View.INVISIBLE);
                                img_back_down.setVisibility(View.INVISIBLE);
                                img_bt_up.setVisibility(View.INVISIBLE);
                                img_bt_down.setVisibility(View.INVISIBLE);
                                img_foot_up.setVisibility(View.INVISIBLE);
                                img_foot_down.setVisibility(View.INVISIBLE);
                                img_head_up.setVisibility(View.INVISIBLE);
                                img_head_down.setVisibility(View.INVISIBLE);
                                img_hip_down.setVisibility(View.INVISIBLE);
                                img_hip_up.setVisibility(View.INVISIBLE);
                                if (t.equals("4") || t.equals("5")) {
                                    name_beitui.setText(R.string.tunbu);
                                } else {
                                    name_beitui.setText(R.string.yaoxun);
                                }
                                tvBeibu.setText(R.string.quanshen);
                                tvBeitui.setText(R.string.beitui);
                                if(!t.equals("1")) {
                                    tvToubu.setText(R.string.tou);
                                }else {
                                    tvToubu.setText(R.string.yaoxun);
                                    imageHead.setImageResource(R.drawable.ic_tunbu);
                                }
                                tvJiaobu.setText(R.string.jiaobu);
                            }
                        }
                    } else {
                        myHandler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
