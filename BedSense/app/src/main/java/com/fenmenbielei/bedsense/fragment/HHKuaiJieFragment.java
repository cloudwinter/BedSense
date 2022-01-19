package com.fenmenbielei.bedsense.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;

import com.fenmenbielei.bedsense.MyApplication;
import com.fenmenbielei.bedsense.base.BaseFragment;
import com.fenmenbielei.bedsense.service.BluetoothLeService;
import com.fenmenbielei.bedsense.uitls.LogUtils;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.fenmenbielei.bedsense.uitls.ToastUtils;
import com.wnhz.shidaodianqi.R;


import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class HHKuaiJieFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {
    CountDownTimer timecount;
    CountDownTimer timecounts;
    @BindView(R.id.img_light)
    ImageView img_light;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.custom_progress4)
    CircleProgressBar mCustomProgressBar4;
    @BindView(R.id.tv_M_first)
    RelativeLayout tv_M_first;
    @BindView(R.id.tv_M_two)
    RelativeLayout tv_M_two;
    @BindView(R.id.ll_dianshi)
    RelativeLayout ll_dianshi;
    @BindView(R.id.ll_lingyali)
    RelativeLayout ll_lingyali;
    @BindView(R.id.ll_shuimian)
    RelativeLayout ll_shuimian;
    @BindView(R.id.ll_kq_shuimian)
    RelativeLayout ll_kq_shuimian;
    @BindView(R.id.ll_fuyuan)
    RelativeLayout ll_fuyuan;
    @BindView(R.id.ll_kq_fuyuan)
    RelativeLayout ll_kq_fuyuan;
    @BindView(R.id.ll_qinxie1)
    RelativeLayout ll_qinxie1;
    @BindView(R.id.qinxie2)
    RelativeLayout qinxie2;
    @BindView(R.id.Rl_up)
    RelativeLayout Rl_up;
    @BindView(R.id.RL_down)
    RelativeLayout RL_down;
    @BindView(R.id.ll_kuaijie)
    LinearLayout ll_kuaijie;
    @BindView(R.id.haohua_anniu)
    LinearLayout haohua_anniu;
    @BindView(R.id.LL_S4)
    LinearLayout LL_S4;
    @BindView(R.id.v_one)
    View vone;
    @BindView(R.id.v_two)
    View vtwo;
    int k = 1;
    int n=0;
    private Handler mhandler = new Handler();
    private int second = 0;
    //蓝牙特征值
    private static BluetoothGattCharacteristic target_chara = null;
    private Timer timer;
    private static String t = "";
    private static Boolean fasong = false;

    public static HHKuaiJieFragment newInstance(String i) {
        HHKuaiJieFragment homeFragment1 = new HHKuaiJieFragment();
        t = i;
        return homeFragment1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hhdeng_guang, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
//        if (t.equals("4") || t.equals("5")) {
//            haohua_anniu.setVisibility(View.VISIBLE);
//            vone.setVisibility(View.GONE);
//            vtwo.setVisibility(View.GONE);
//        } else {
//            haohua_anniu.setVisibility(View.GONE);
//            vone.setVisibility(View.VISIBLE);
//            vtwo.setVisibility(View.VISIBLE);
//        }
        if (Prefer.getInstance().getM1().equals("red")) {
            tv_M_first.setBackgroundResource(R.drawable.ic_home_hhk);
        } else {
            tv_M_first.setBackgroundResource(R.drawable.ic_home_ssk);
        }

        if (Prefer.getInstance().getM2().equals("red")) {
            tv_M_two.setBackgroundResource(R.drawable.ic_home_hhk);
        } else {
            tv_M_two.setBackgroundResource(R.drawable.ic_home_ssk);
        }
        if (Prefer.getInstance().getM3().equals("red")) {
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_selected);
        } else {
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_normal);
        }

        if (Prefer.getInstance().getM4().equals("red")) {
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_selected);
        } else {
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_normal);
        }
        if (Prefer.getInstance().getM5().equals("red")) {
            ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_selected);
        } else {
            ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_normal);
        }

        tv_M_first.setOnTouchListener(this);
        tv_M_two.setOnTouchListener(this);
        ll_dianshi.setOnTouchListener(this);
        ll_lingyali.setOnTouchListener(this);
        ll_kq_fuyuan.setOnTouchListener(this);
        // ll_lingyali.setOnClickListener(this);
        ll_shuimian.setOnTouchListener(this);
        ll_fuyuan.setOnClickListener(this);
        ll_qinxie1.setOnClickListener(this);
        qinxie2.setOnClickListener(this);
        Rl_up.setOnClickListener(this);
        RL_down.setOnClickListener(this);
        ll_kuaijie.setOnClickListener(this);
        ll_kq_shuimian.setOnClickListener(this);

        mCustomProgressBar4.setProgress(100);
    }



    @Override
    public void onClick(View view) {
        target_chara = MyApplication.getInstance().gattCharacteristic;
        switch (view.getId()) {
            case R.id.ll_kuaijie:
                Log.e("点击sxsssssss", "sssssssss");
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    Log.e("点击sxsssssss", Prefer.getInstance().getBleStatus());
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
                value9[9] = (byte) 0xd7;
                value9[10] = (byte) 0x00;
                target_chara.setValue(value9);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
            case R.id.ll_kq_shuimian:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                    return;
                }
                img_light.setBackgroundResource(R.drawable.ic_tunbu);
                tv_title.setText(getResources().getString(R.string.dyao));
                byte[] value906 = new byte[11];
                value906[0] = (byte) 0xff;
                value906[1] = (byte) 0xff;
                value906[2] = (byte) 0xff;
                value906[3] = (byte) 0xff;
                value906[4] = (byte) 0x05;
                value906[5] = (byte) 0x00;
                value906[6] = (byte) 0x00;
                value906[7] = (byte) 0x00;
                value906[8] = (byte) 0x2e;
                value906[9] = (byte) 0x57;
                value906[10] = (byte) 0x1c;
                target_chara.setValue(value906);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
//            case R.id.ll_dianshi:
//                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
//                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
//
//                    return;
//                }
//
//                ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_selected);
//                ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//                ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//                ll_fuyuan.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//
//                img_light.setBackgroundResource(R.drawable.ic_yijiankandianshi_light);
//                tv_title.setText(getResources().getString(R.string.watch_tv));
//
//                byte[] value1 = new byte[8];
//                value1[0] = (byte) 0xff;
//                value1[1] = (byte) 0x05;
//                value1[2] = (byte) 0x00;
//                value1[3] = (byte) 0x00;
//                value1[4] = (byte) 0x00;
//                value1[5] = (byte) 0x05;
//                value1[6] = (byte) 0x18;
//                value1[7] = (byte) 0x17;
//                target_chara.setValue(value1);
//                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
//                break;
//            case R.id.ll_lingyali:
//                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
//                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
//
//                    return;
//                }
//
//                ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//                ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_selected);
//                ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//                ll_fuyuan.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//
//                img_light.setBackgroundResource(R.drawable.ic_lingyali_light);
//                tv_title.setText(getResources().getString(R.string.no_press));
//
//                byte[] value2 = new byte[8];
//                value2[0] = (byte) 0xFF;
//                value2[1] = (byte) 0x05;
//                value2[2] = (byte) 0x00;
//                value2[3] = (byte) 0x00;
//                value2[4] = (byte) 0x91;
//                value2[5] = (byte) 0x09;
//                value2[6] = (byte) 0x75;
//                value2[7] = (byte) 0x82;
//                target_chara.setValue(value2);
//                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
//                break;
//            case R.id.ll_shuimian:
//                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
//                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
//
//                    return;
//                }
//
////                ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_normal);
////                ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//             //   ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_selected);
//                ll_fuyuan.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//                ll_qinxie1.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//                qinxie2.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//
//                img_light.setBackgroundResource(R.drawable.ic_shuimianmoshi_light);
//                tv_title.setText(getResources().getString(R.string.sleep));
//
//                byte[] value3 = new byte[11];
//                value3[0] = (byte) 0xff;
//                value3[1] = (byte) 0xff;
//                value3[2] = (byte) 0xff;
//                value3[3] = (byte) 0xff;
//                value3[4] = (byte) 0x05;
//                value3[5] = (byte) 0x00;
//                value3[6] = (byte) 0x00;
//                value3[7] = (byte) 0x00;
//                value3[8] = (byte) 0x0f;
//                value3[9] = (byte) 0x98;
//                value3[10] = (byte) 0x10;
//                target_chara.setValue(value3);
//                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
//                break;
            case R.id.ll_fuyuan:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }
                //  ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_normal);
               // ll_fuyuan.setBackgroundResource(R.drawable.ic_light_an);
              //  ll_qinxie1.setBackgroundResource(R.drawable.ic_kuaijie_normal);
              //  qinxie2.setBackgroundResource(R.drawable.ic_kuaijie_normal);

                img_light.setBackgroundResource(R.drawable.ic_yijianfuyuan);
                tv_title.setText(getResources().getString(R.string.yijian_fuyuan));

                byte[] value5 = new byte[11];
                value5[0] = (byte) 0xff;
                value5[1] = (byte) 0xff;
                value5[2] = (byte) 0xff;
                value5[3] = (byte) 0xff;
                value5[4] = (byte) 0x05;
                value5[5] = (byte) 0x00;
                value5[6] = (byte) 0x00;
                value5[7] = (byte) 0x00;
                value5[8] = (byte) 0x08;
                value5[9] = (byte) 0xD6;
                value5[10] = (byte) 0xC6;
                target_chara.setValue(value5);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
            case R.id.ll_qinxie1:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }


                ll_fuyuan.setBackgroundResource(R.drawable.ic_kuaijie_normal);
                img_light.setBackgroundResource(R.drawable.tang);
                tv_title.setText(getResources().getString(R.string.music));

                byte[] value666 = new byte[11];
                value666[0] = (byte) 0xff;
                value666[1] = (byte) 0xff;
                value666[2] = (byte) 0xff;
                value666[3] = (byte) 0xff;
                value666[4] = (byte) 0x05;
                value666[5] = (byte) 0x00;
                value666[6] = (byte) 0x00;
                value666[7] = (byte) 0x00;
                value666[8] = (byte) 0x28;
                value666[9] = (byte) 0xd7;
                value666[10] = (byte) 0x1e;
                target_chara.setValue(value666);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;

            case R.id.qinxie2:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }

            //    ll_qinxie1.setBackgroundResource(R.drawable.ic_kuaijie_normal);
             //   qinxie2.setBackgroundResource(R.drawable.ic_light_an);
                ll_fuyuan.setBackgroundResource(R.drawable.ic_kuaijie_normal);
                // ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_normal);

                img_light.setBackgroundResource(R.drawable.tangf);
                tv_title.setText(getResources().getString(R.string.relax));

                byte[] value462 = new byte[11];
                value462[0] = (byte) 0xff;
                value462[1] = (byte) 0xff;
                value462[2] = (byte) 0xff;
                value462[3] = (byte) 0xff;
                value462[4] = (byte) 0x05;
                value462[5] = (byte) 0x00;
                value462[6] = (byte) 0x00;
                value462[7] = (byte) 0x00;
                value462[8] = (byte) 0x29;
                value462[9] = (byte) 0x16;
                value462[10] = (byte) 0xde;
                target_chara.setValue(value462);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
            case R.id.Rl_up:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }


                ll_fuyuan.setBackgroundResource(R.drawable.ic_kuaijie_normal);
                img_light.setBackgroundResource(R.drawable.ic_yijianfuyuan);
                tv_title.setText(getResources().getString(R.string.text_up));

                byte[] value667 = new byte[11];
                value667[0] = (byte) 0xff;
                value667[1] = (byte) 0xff;
                value667[2] = (byte) 0xff;
                value667[3] = (byte) 0xff;
                value667[4] = (byte) 0x05;
                value667[5] = (byte) 0x00;
                value667[6] = (byte) 0x00;
                value667[7] = (byte) 0x00;
                value667[8] = (byte) 0x01;
                value667[9] = (byte) 0x16;
                value667[10] = (byte) 0xc0;
                target_chara.setValue(value667);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
            case R.id.RL_down:
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));

                    return;
                }


                ll_fuyuan.setBackgroundResource(R.drawable.ic_kuaijie_normal);
                img_light.setBackgroundResource(R.drawable.ic_yijianfuyuan);
                tv_title.setText(getResources().getString(R.string.text_down));

                byte[] value668 = new byte[11];
                value668[0] = (byte) 0xff;
                value668[1] = (byte) 0xff;
                value668[2] = (byte) 0xff;
                value668[3] = (byte) 0xff;
                value668[4] = (byte) 0x05;
                value668[5] = (byte) 0x00;
                value668[6] = (byte) 0x00;
                value668[7] = (byte) 0x00;
                value668[8] = (byte) 0x02;
                value668[9] = (byte) 0x56;
                value668[10] = (byte) 0xc1;
                target_chara.setValue(value668);
                MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                break;
        }
    }


    //发送消息
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                timerTask1.cancel();
            }
        }
    };
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                timerTask.cancel();
            }
        }
    };
    Handler handler3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                timerTask3.cancel();
            }
        }
    };
    Handler handler4 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                timerTask4.cancel();
            }
        }
    };
    Handler handler5 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                timerTask5.cancel();
            }
        }
    };
    Handler handler6 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                timerTask6.cancel();
            }
        }
    };

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        target_chara = MyApplication.getInstance().gattCharacteristic;
        switch (view.getId()) {
            //M1
            case R.id.tv_M_first:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    LogUtils.e("==按下==", "action_down");
                    img_light.setBackgroundResource(R.drawable.ic_toubu);
                    tv_title.setText(getResources().getString(R.string.M1));
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
                    if (2 >= second && second != 0 && Prefer.getInstance().getM1().equals("red")) {
                        byte[] value42 = new byte[11];
                        value42[0] = (byte) 0xff;
                        value42[1] = (byte) 0xff;
                        value42[2] = (byte) 0xff;
                        value42[3] = (byte) 0xff;
                        value42[4] = (byte) 0x05;
                        value42[5] = (byte) 0x00;
                        value42[6] = (byte) 0x00;
                        value42[7] = (byte) 0xa1;
                        value42[8] = (byte) 0x0a;
                        value42[9] = (byte) 0x2E;
                        value42[10] = (byte) 0x97;
                        target_chara.setValue(value42);
                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    } else if (2 >= second && second != 0 && !Prefer.getInstance().getM1().equals("red")) {


                    }
                    return true;
                }else if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE){
                    if (myHandler != null) {
                        myHandler.removeCallbacks(null);
                        myHandler.removeCallbacksAndMessages(null);
                    }

                }

                break;
            //M2
            case R.id.tv_M_two:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    LogUtils.e("==按下==", "action_down");
                    img_light.setBackgroundResource(R.drawable.ic_toubu);
                    tv_title.setText(getResources().getString(R.string.M2));
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                        return false;
                    }
                    second = 2;
                    myHandler2.sendEmptyMessageDelayed(1, 1000);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (myHandler2 != null) {
                        myHandler2.removeCallbacks(null);
                        myHandler2.removeCallbacksAndMessages(null);
                    }

                    if (2 >= second && second != 0 && Prefer.getInstance().getM2().equals("red")) {
                        byte[] value52 = new byte[11];
                        value52[0] = (byte) 0xFF;
                        value52[1] = (byte) 0xFF;
                        value52[2] = (byte) 0xFF;
                        value52[3] = (byte) 0xFF;
                        value52[4] = (byte) 0x05;
                        value52[5] = (byte) 0x00;
                        value52[6] = (byte) 0x00;
                        value52[7] = (byte) 0xb1;
                        value52[8] = (byte) 0x0b;
                        value52[9] = (byte) 0xe2;
                        value52[10] = (byte) 0x97;
                        target_chara.setValue(value52);
                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    } else if (2 >= second && second != 0 && !Prefer.getInstance().getM2().equals("red")) {
//                        byte[] value520 = new byte[8];
//                        value520[0] = (byte) 0xFF;
//                        value520[1] = (byte) 0x05;
//                        value520[2] = (byte) 0x00;
//                        value520[3] = (byte) 0x00;
//                        value520[4] = (byte) 0xb1;
//                        value520[5] = (byte) 0x0b;
//                        value520[6] = (byte) 0x99;
//                        value520[7] = (byte) 0xD3;
//                        target_chara.setValue(value520);
//                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    }
                    return true;
                }else if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE){

                        if (myHandler2 != null) {
                            myHandler2.removeCallbacks(null);
                            myHandler2.removeCallbacksAndMessages(null);
                        }
                }
                break;
            //一键看电视
            case R.id.ll_dianshi:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    LogUtils.e("==按下==", "按");
                    img_light.setBackgroundResource(R.drawable.ic_yijiankandianshi);
                    tv_title.setText(getResources().getString(R.string.watch_tv));
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    //    ToastUtils.showToast(getContext(),"");
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                        return false;
                    }
                    second = 2;
                    myHandler3.sendEmptyMessageDelayed(1, 1000);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (myHandler3 != null) {
                        myHandler3.removeCallbacks(null);
                        myHandler3.removeCallbacksAndMessages(null);
                    }
                    if (2 >= second && second != 0 && Prefer.getInstance().getM3().equals("red")) {
                        LogUtils.e("==按下==", "记住短按");
                        byte[] value53 = new byte[11];
                        value53[0] = (byte) 0xFF;
                        value53[1] = (byte) 0xFF;
                        value53[2] = (byte) 0xFF;
                        value53[3] = (byte) 0xFF;
                        value53[4] = (byte) 0x05;
                        value53[5] = (byte) 0x00;
                        value53[6] = (byte) 0x00;
                        value53[7] = (byte) 0x51;
                        value53[8] = (byte) 0x05;
                        value53[9] = (byte) 0x2A;
                        value53[10] = (byte) 0x93;
                        target_chara.setValue(value53);
                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    } else if (2 >= second && second != 0 && !Prefer.getInstance().getM3().equals("red")) {
                        LogUtils.e("==按下==", "不记住短按");
                        byte[] value530 = new byte[11];
                        value530[0] = (byte) 0xFF;
                        value530[1] = (byte) 0xFF;
                        value530[2] = (byte) 0xFF;
                        value530[3] = (byte) 0xFF;
                        value530[4] = (byte) 0x05;
                        value530[5] = (byte) 0x00;
                        value530[6] = (byte) 0x00;
                        value530[7] = (byte) 0x00;
                        value530[8] = (byte) 0x05;
                        value530[9] = (byte) 0x17;
                        value530[10] = (byte) 0x03;
                        target_chara.setValue(value530);
                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    }
                    return true;
                }else if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE){
                    if (myHandler3 != null) {
                        myHandler3.removeCallbacks(null);
                        myHandler3.removeCallbacksAndMessages(null);
                    }
                }
//                else {
//                    if (myHandler3 != null) {
//                        myHandler3.removeCallbacks(null);
//                        myHandler3.removeCallbacksAndMessages(null);
//                    }
//                }
                break;
            //零压力状态
            case R.id.ll_lingyali:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    LogUtils.e("==按下==", "action");
                    img_light.setBackgroundResource(R.drawable.ic_lingyalizhuangtai);
                    tv_title.setText(getResources().getString(R.string.no_press));
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                        return false;
                    }
                    second = 2;
                    myHandler4.sendEmptyMessageDelayed(1, 1000);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    LogUtils.e("==抬起==", second + "");
                    if (myHandler4 != null) {
                        myHandler4.removeCallbacks(null);
                        myHandler4.removeCallbacksAndMessages(null);
                    }

                    if (2 >= second && second != 0 && Prefer.getInstance().getM4().equals("red")) {
                        byte[] value5300 = new byte[11];
                        value5300[0] = (byte) 0xFF;
                        value5300[1] = (byte) 0xFF;
                        value5300[2] = (byte) 0xFF;
                        value5300[3] = (byte) 0xFF;
                        value5300[4] = (byte) 0x05;
                        value5300[5] = (byte) 0x00;
                        value5300[6] = (byte) 0x00;
                        value5300[7] = (byte) 0x91;
                        value5300[8] = (byte) 0x09;
                        value5300[9] = (byte) 0x7A;
                        value5300[10] = (byte) 0x96;
                        target_chara.setValue(value5300);
                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    } else if (2 >= second && second != 0 && !Prefer.getInstance().getM4().equals("red")) {
                        byte[] value590 = new byte[11];
                        value590[0] = (byte) 0xFF;
                        value590[1] = (byte) 0xFF;
                        value590[2] = (byte) 0xFF;
                        value590[3] = (byte) 0xFF;
                        value590[4] = (byte) 0x05;
                        value590[5] = (byte) 0x00;
                        value590[6] = (byte) 0x00;
                        value590[7] = (byte) 0x00;
                        value590[8] = (byte) 0x09;
                        value590[9] = (byte) 0x17;
                        value590[10] = (byte) 0x06;
                        target_chara.setValue(value590);
                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    }
                    return true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE){
                    if (myHandler4 != null) {
                        myHandler4.removeCallbacks(null);
                        myHandler4.removeCallbacksAndMessages(null);
                    }
                }
                break;
            //止鼾
            case R.id.ll_shuimian:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    LogUtils.e("==按下==", "action");
                    img_light.setBackgroundResource(R.drawable.ic_shuimianmoshi);
                    tv_title.setText(getResources().getString(R.string.sleep));
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                        return false;
                    }
                    second = 2;
                    myHandler5.sendEmptyMessageDelayed(1, 1000);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    LogUtils.e("==抬起==", second + "");
                    if (myHandler5 != null) {
                        myHandler5.removeCallbacks(null);
                        myHandler5.removeCallbacksAndMessages(null);
                    }

                    if (2 >= second && second != 0 && Prefer.getInstance().getM5().equals("red")) {
                        byte[] value5120 = new byte[11];
                        value5120[0] = (byte) 0xFF;
                        value5120[1] = (byte) 0xFF;
                        value5120[2] = (byte) 0xFF;
                        value5120[3] = (byte) 0xFF;
                        value5120[4] = (byte) 0x05;
                        value5120[5] = (byte) 0x00;
                        value5120[6] = (byte) 0x00;
                        value5120[7] = (byte) 0xF1;
                        value5120[8] = (byte) 0x0F;
                        value5120[9] = (byte) 0xD2;
                        value5120[10] = (byte) 0x94;
                        target_chara.setValue(value5120);
                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    } else if (2 >= second && second != 0 && !Prefer.getInstance().getM5().equals("red")) {
                        byte[] value790 = new byte[11];
                        value790[0] = (byte) 0xFF;
                        value790[1] = (byte) 0xFF;
                        value790[2] = (byte) 0xFF;
                        value790[3] = (byte) 0xFF;
                        value790[4] = (byte) 0x05;
                        value790[5] = (byte) 0x00;
                        value790[6] = (byte) 0x00;
                        value790[7] = (byte) 0x00;
                        value790[8] = (byte) 0x0F;
                        value790[9] = (byte) 0x97;
                        value790[10] = (byte) 0x04;
                        target_chara.setValue(value790);
                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    }
                    return true;
                }else if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE){
                    if (myHandler5 != null) {
                        myHandler5.removeCallbacks(null);
                        myHandler5.removeCallbacksAndMessages(null);
                    }
                }
                break;
            //M1
            case R.id.ll_kq_fuyuan:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    LogUtils.e("==按下==", "action_down");
                    img_light.setBackgroundResource(R.drawable.ic_yijianfuyuan);
                    tv_title.setText(getResources().getString(R.string.yijian_fuyuan));
                    if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                        ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                        return false;
                    }
                    second = 2;
                    myHandler6.sendEmptyMessageDelayed(1, 1000);
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (myHandler6 != null) {
                        myHandler6.removeCallbacks(null);
                        myHandler6.removeCallbacksAndMessages(null);
                    }
                    ll_kq_fuyuan.setBackgroundResource(R.drawable.ic_kuaijie_normal);
                    if (2 >= second && second != 0) {
                        byte[] value5010 = new byte[11];
                        value5010[0] = (byte) 0xFF;
                        value5010[1] = (byte) 0xFF;
                        value5010[2] = (byte) 0xFF;
                        value5010[3] = (byte) 0xFF;
                        value5010[4] = (byte) 0x05;
                        value5010[5] = (byte) 0x00;
                        value5010[6] = (byte) 0x00;
                        value5010[7] = (byte) 0xF1;
                        value5010[8] = (byte) 0x0F;
                        value5010[9] = (byte) 0xD2;
                        value5010[10] = (byte) 0x94;
                        target_chara.setValue(value5010);
                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    }
                    return true;
                }else if(motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE){
                    if (myHandler6 != null) {
                        myHandler6.removeCallbacks(null);
                        myHandler6.removeCallbacksAndMessages(null);
                    }
                }

                break;
        }
        return false;
    }

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    second--;
                    LogUtils.e("==zou==", "----ww-------");
                    if (second < 0) {
                        second = 0;
                    } else if (second == 0) {
                        myHandler.removeCallbacks(null);
                        myHandler.removeCallbacksAndMessages(null);
                        LogUtils.e("==zou==", "----lv-------");
                        if (Prefer.getInstance().getM1().equals("lv")) {
                            //发送记忆串口码
                            LogUtils.e("==M1 发送记忆串口码==", "----lv-------");
                            byte[] value4 = new byte[11];
                            value4[0] = (byte) 0xff;
                            value4[1] = (byte) 0xff;
                            value4[2] = (byte) 0xff;
                            value4[3] = (byte) 0xff;
                            value4[4] = (byte) 0x05;
                            value4[5] = (byte) 0x00;
                            value4[6] = (byte) 0x00;
                            value4[7] = (byte) 0xa0;
                            value4[8] = (byte) 0x0a;
                            value4[9] = (byte) 0x2F;
                            value4[10] = (byte) 0x07;
                            target_chara.setValue(value4);
                            MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                        } else if (Prefer.getInstance().getM1().equals("red")) {
                            //发送清楚串口码
                            LogUtils.e("==M1 发送清除串口码==", "----red-------");
                            byte[] value41 = new byte[11];
                            value41[0] = (byte) 0xff;
                            value41[1] = (byte) 0xff;
                            value41[2] = (byte) 0xff;
                            value41[3] = (byte) 0xff;
                            value41[4] = (byte) 0x05;
                            value41[5] = (byte) 0x00;
                            value41[6] = (byte) 0x00;
                            value41[7] = (byte) 0xaf;
                            value41[8] = (byte) 0x0a;
                            value41[9] = (byte) 0x2a;
                            value41[10] = (byte) 0xf7;
                            target_chara.setValue(value41);
                            MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                        }
                    } else {
                        myHandler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Handler myHandler2 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    second--;
                    if (second < 0) {
                        second = 0;
                    } else if (second == 0) {
                        myHandler2.removeCallbacks(null);
                        myHandler2.removeCallbacksAndMessages(null);

                        if (Prefer.getInstance().getM2().equals("lv")) {
                            //发送记忆串口码
                            LogUtils.e("==M2 发送记忆串口码==", "----lv-------");
                            byte[] value5 = new byte[11];
                            value5[0] = (byte) 0xff;
                            value5[1] = (byte) 0xff;
                            value5[2] = (byte) 0xff;
                            value5[3] = (byte) 0xff;
                            value5[4] = (byte) 0x05;
                            value5[5] = (byte) 0x00;
                            value5[6] = (byte) 0x00;
                            value5[7] = (byte) 0xb0;
                            value5[8] = (byte) 0x0b;
                            value5[9] = (byte) 0xe3;
                            value5[10] = (byte) 0x07;
                            target_chara.setValue(value5);
                            MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                        } else if (Prefer.getInstance().getM2().equals("red")) {
                            //发送清楚串口码
                            LogUtils.e("==M2 发送清除串口码==", "----red-------");
                            byte[] value51 = new byte[11];
                            value51[0] = (byte) 0xff;
                            value51[1] = (byte) 0xff;
                            value51[2] = (byte) 0xff;
                            value51[3] = (byte) 0xff;
                            value51[4] = (byte) 0x05;
                            value51[5] = (byte) 0x00;
                            value51[6] = (byte) 0x00;
                            value51[7] = (byte) 0xbf;
                            value51[8] = (byte) 0x0b;
                            value51[9] = (byte) 0xe6;
                            value51[10] = (byte) 0xf7;
                            target_chara.setValue(value51);
                            MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                        }
                    } else {
                        myHandler2.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Handler myHandler3 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    second--;
                    if (second < 0) {
                        second = 0;
                    } else if (second == 0) {
                        myHandler3.removeCallbacks(null);
                        myHandler3.removeCallbacksAndMessages(null);

                        if (Prefer.getInstance().getM3().equals("lv")) {
                            //发送记忆串口码
                            LogUtils.e("==M3 发送记忆串口码==", "----lv-------");
                            byte[] value5200 = new byte[11];
                            value5200[0] = (byte) 0xff;
                            value5200[1] = (byte) 0xff;
                            value5200[2] = (byte) 0xff;
                            value5200[3] = (byte) 0xff;
                            value5200[4] = (byte) 0x05;
                            value5200[5] = (byte) 0x00;
                            value5200[6] = (byte) 0x00;
                            value5200[7] = (byte) 0x50;
                            value5200[8] = (byte) 0x05;
                            value5200[9] = (byte) 0x2B;
                            value5200[10] = (byte) 0x03;
                            target_chara.setValue(value5200);
                            MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                        } else if (Prefer.getInstance().getM3().equals("red")) {
                            //发送清楚串口码
                            LogUtils.e("==M2 发送清除串口码==", "----red-------");
                            byte[] value5100 = new byte[11];
                            value5100[0] = (byte) 0xff;
                            value5100[1] = (byte) 0xff;
                            value5100[2] = (byte) 0xff;
                            value5100[3] = (byte) 0xff;
                            value5100[4] = (byte) 0x05;
                            value5100[5] = (byte) 0x00;
                            value5100[6] = (byte) 0x00;
                            value5100[7] = (byte) 0x5f;
                            value5100[8] = (byte) 0x05;
                            value5100[9] = (byte) 0x2E;
                            value5100[10] = (byte) 0xF3;
                            target_chara.setValue(value5100);
                            MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                        }
                    } else {
                        myHandler3.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Handler myHandler4 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    second--;
                    if (second < 0) {
                        second = 0;
                    } else if (second == 0) {
                        myHandler4.removeCallbacks(null);
                        myHandler4.removeCallbacksAndMessages(null);

                        if (Prefer.getInstance().getM4().equals("lv")) {
                            //发送记忆串口码
                            LogUtils.e("==M4 发送记忆串口码==", "----lv-------");
                            byte[] value5300 = new byte[11];
                            value5300[0] = (byte) 0xff;
                            value5300[1] = (byte) 0xff;
                            value5300[2] = (byte) 0xff;
                            value5300[3] = (byte) 0xff;
                            value5300[4] = (byte) 0x05;
                            value5300[5] = (byte) 0x00;
                            value5300[6] = (byte) 0x00;
                            value5300[7] = (byte) 0x90;
                            value5300[8] = (byte) 0x09;
                            value5300[9] = (byte) 0x7B;
                            value5300[10] = (byte) 0x06;
                            target_chara.setValue(value5300);
                            MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                        } else if (Prefer.getInstance().getM4().equals("red")) {
                            //发送清楚串口码
                            LogUtils.e("==M2 发送清除串口码==", "----red-------");
                            byte[] value53000 = new byte[11];
                            value53000[0] = (byte) 0xff;
                            value53000[1] = (byte) 0xff;
                            value53000[2] = (byte) 0xff;
                            value53000[3] = (byte) 0xff;
                            value53000[4] = (byte) 0x05;
                            value53000[5] = (byte) 0x00;
                            value53000[6] = (byte) 0x00;
                            value53000[7] = (byte) 0x9f;
                            value53000[8] = (byte) 0x09;
                            value53000[9] = (byte) 0x7e;
                            value53000[10] = (byte) 0xf6;
                            target_chara.setValue(value53000);
                            MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                        }
                    } else {
                        myHandler4.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    //止鼾记忆
    private Handler myHandler5 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    second--;
                    if (second < 0) {
                        second = 0;
                    } else if (second == 0) {
                        myHandler5.removeCallbacks(null);
                        myHandler5.removeCallbacksAndMessages(null);

                        if (Prefer.getInstance().getM5().equals("lv")) {
                            //发送记忆串口码
                            LogUtils.e("==M5 发送记忆串口码==", "----lv-------");
                            byte[] value5565 = new byte[11];
                            value5565[0] = (byte) 0xff;
                            value5565[1] = (byte) 0xff;
                            value5565[2] = (byte) 0xff;
                            value5565[3] = (byte) 0xff;
                            value5565[4] = (byte) 0x05;
                            value5565[5] = (byte) 0x00;
                            value5565[6] = (byte) 0x00;
                            value5565[7] = (byte) 0xf0;
                            value5565[8] = (byte) 0x0f;
                            value5565[9] = (byte) 0xd3;
                            value5565[10] = (byte) 0x04;
                            target_chara.setValue(value5565);
                            MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                        } else if (Prefer.getInstance().getM5().equals("red")) {
                            //发送清楚串口码
                            LogUtils.e("==M5 发送清除串口码==", "----red-------");
                            byte[] value5986 = new byte[11];
                            value5986[0] = (byte) 0xff;
                            value5986[1] = (byte) 0xff;
                            value5986[2] = (byte) 0xff;
                            value5986[3] = (byte) 0xff;
                            value5986[4] = (byte) 0x05;
                            value5986[5] = (byte) 0x00;
                            value5986[6] = (byte) 0x00;
                            value5986[7] = (byte) 0xff;
                            value5986[8] = (byte) 0x0f;
                            value5986[9] = (byte) 0xd6;
                            value5986[10] = (byte) 0xf4;
                            target_chara.setValue(value5986);
                            MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                        }
                    } else {
                        myHandler5.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //kq记忆
    private Handler myHandler6 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    second--;
                    if (second < 0) {
                        second = 0;
                    } else if (second == 0) {
                        myHandler6.removeCallbacks(null);
                        myHandler6.removeCallbacksAndMessages(null);
                        //发送串口码
                        LogUtils.e("==kq 发送复原串口码==", "----red-------");
                        byte[] value5006 = new byte[11];
                        value5006[0] = (byte) 0xff;
                        value5006[1] = (byte) 0xff;
                        value5006[2] = (byte) 0xff;
                        value5006[3] = (byte) 0xff;
                        value5006[4] = (byte) 0x05;
                        value5006[5] = (byte) 0x00;
                        value5006[6] = (byte) 0x00;
                        value5006[7] = (byte) 0xf0;
                        value5006[8] = (byte) 0x0f;
                        value5006[9] = (byte) 0xd3;
                        value5006[10] = (byte) 0x04;
                        target_chara.setValue(value5006);
                        MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    } else {
                        myHandler6.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //读秒线程
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = second;
            handler2.sendEmptyMessageDelayed(msg.what, 1000);
            LogUtils.e("=====剩余时间====", "" + second);
            second--;
        }
    };
    //读秒线程
    TimerTask timerTask1 = new TimerTask() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = second;
            handler1.sendEmptyMessageDelayed(msg.what, 1000);
            LogUtils.e("=====剩余时间====", "" + second);
            second--;
        }
    };
    //读秒线程
    TimerTask timerTask3 = new TimerTask() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = second;
            handler3.sendEmptyMessageDelayed(msg.what, 1000);
            LogUtils.e("=====剩余时间====", "" + second);
            second--;
        }
    };
    //读秒线程
    TimerTask timerTask4 = new TimerTask() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = second;
            handler4.sendEmptyMessageDelayed(msg.what, 1000);
            LogUtils.e("=====剩余时间====", "" + second);
            second--;
        }
    };
    //读秒线程
    TimerTask timerTask5 = new TimerTask() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = second;
            handler5.sendEmptyMessageDelayed(msg.what, 1000);
            LogUtils.e("=====剩余时间====", "" + second);
            second--;
        }
    };
    //读秒线程
    TimerTask timerTask6 = new TimerTask() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = second;
            handler6.sendEmptyMessageDelayed(msg.what, 1000);
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
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String data = bundle.getString(BluetoothLeService.EXTRA_DATA);
                    if (data != null) {
                        displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                    }
                }
//    displayData(intent.getExtras().BluetoothLeService.EXTRA_DATA);

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
        LogUtils.e("==快捷  接收设备返回的数据==", "" + rev_string);
        // ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_normal);
        if (rev_string.contains("00 A5")) {
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM3("red");
          //  LogUtils.e("==ssssssssssss==", "进来了q1");
        } else if (rev_string.contains("00 A9")) {
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM4("red");
           // LogUtils.e("==ssssssssssss==", "进来了q2");
        } else if (rev_string.contains("00 AA")) {
            tv_M_first.setBackgroundResource(R.drawable.ic_home_hhk);
            Prefer.getInstance().setM1("red");
           // LogUtils.e("==ssssssssssss==", "进来了q3");
        } else if (rev_string.contains("00 AB")) {
            tv_M_two.setBackgroundResource(R.drawable.ic_home_hhk);
            Prefer.getInstance().setM2("red");
           // LogUtils.e("==ssssssssssss==", "进来了q4");
        } else if (rev_string.contains("00 AF") && !rev_string.equals("FF FF FF FF 05 00 00 AF 0A 2A F7 ")) {
            ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM5("red");
            //LogUtils.e("==ssssssssssss==", "进来了q5");
        } else if (rev_string.equals("FF FF FF FF 05 00 00 A0 0A 2F 07 ")) {
            LogUtils.e("==M1 记忆 进来了==", "进来了");
            tv_M_first.setBackgroundResource(R.drawable.ic_home_hhk);
            Prefer.getInstance().setM1("red");
        } else if (rev_string.equals("FF FF FF FF 05 00 00 AF 0A 2A F7 ")) {
            LogUtils.e("== M1 清除 进来了==", "进来了");
            tv_M_first.setBackgroundResource(R.drawable.ic_home_ssk);
            Prefer.getInstance().setM1("lv");
        } else if (rev_string.equals("FF FF FF FF 05 00 00 B0 0B E3 07 ")) {
            LogUtils.e("==M2 记忆 进来了==", "进来了");
            tv_M_two.setBackgroundResource(R.drawable.ic_home_hhk);
            Prefer.getInstance().setM2("red");
        } else if (rev_string.equals("FF FF FF FF 05 00 00 BF 0B E6 F7 ")) {
            LogUtils.e("== M2 清除 进来了==", "进来了");
            tv_M_two.setBackgroundResource(R.drawable.ic_home_ssk);
            Prefer.getInstance().setM2("lv");
        } else if (rev_string.equals("FF FF FF FF 05 00 00 50 05 2B 03 ")) {//一键看电视
            LogUtils.e("== 一键看电视 进来了==", "进来了");
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM3("red");
        } else if (rev_string.equals("FF FF FF FF 05 00 00 5F 05 2E F3 ")) {//一键看电视
            LogUtils.e("== 一键看电视 进来了==", "进来了");
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM3("lv");
        } else if (rev_string.equals("FF FF FF FF 05 00 00 90 09 7B 06 ")) {
            LogUtils.e("== 一键零压力 进来了==", "进来了");
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM4("red");
        } else if (rev_string.equals("FF FF FF FF 05 00 00 9F 09 7E F6 ")) {
            LogUtils.e("== 一键零压力 进来了==", "进来了");
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM4("lv");
        } else if (rev_string.contains("FF FF FF FF 05 00 00 F0 0F D3 04 ")) {
            LogUtils.e("== 一键止住鼾 进来了==", "进来了");
            ll_kq_fuyuan.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM5("red");
        } else if (rev_string.equals("FF FF FF FF 05 00 00 FF 0F D6 F4 ")) {
            LogUtils.e("== 一键止住鼾 进来了==", "进来了");
            ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM5("lv");
          //M1
        }else if(rev_string.contains("FF FF FF FF 03 00 24 00 03 5F 0A ")){
            tv_M_first.setBackgroundResource(R.drawable.ic_home_ssk);
            n=1;
            Prefer.getInstance().setM1("lv");
        }else if(n==1&&rev_string.contains("00 0A ")){
            tv_M_first.setBackgroundResource(R.drawable.ic_home_hhk);
            Prefer.getInstance().setM1("red");
        }else if(rev_string.contains("FF FF FF FF 03 00 28 00 03 9F 09 ")){
            tv_M_first.setBackgroundResource(R.drawable.ic_home_ssk);
            n=2;
            Prefer.getInstance().setM1("lv");
        }else if(n==2&&rev_string.contains("00 0A ")){
            tv_M_first.setBackgroundResource(R.drawable.ic_home_hhk);
            Prefer.getInstance().setM1("red");
            //M2
        }else if(rev_string.contains("FF FF FF FF 03 00 2C 00 03 DE C8 ")){
           n=3;
            tv_M_two.setBackgroundResource(R.drawable.ic_home_ssk);
            Prefer.getInstance().setM2("lv");
        }else if(n==3&&rev_string.contains("00 0B ")){
            tv_M_two.setBackgroundResource(R.drawable.ic_home_hhk);
            Prefer.getInstance().setM2("red");
        }else if(rev_string.contains("FF FF FF FF 03 00 31 00 09 CE C9 ")){
            n=11;
            tv_M_two.setBackgroundResource(R.drawable.ic_home_ssk);
            Prefer.getInstance().setM2("lv");
        }else if(n==11&&rev_string.contains("00 0B ")){
            tv_M_two.setBackgroundResource(R.drawable.ic_home_hhk);
            Prefer.getInstance().setM2("red");
        }else if(rev_string.contains("FF FF FF FF 03 00 30 00 03 1F 0E ")){
            tv_M_two.setBackgroundResource(R.drawable.ic_home_ssk);
            Prefer.getInstance().setM2("lv");
            n=4;
        }else if(n==4&&rev_string.contains("00 0B ")){
            //看电视
            tv_M_two.setBackgroundResource(R.drawable.ic_home_hhk);
            Prefer.getInstance().setM2("red");
        }else if(rev_string.contains("FF FF FF FF 03 00 14 00 03 5F 05 ")){
            n=5;
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM3("lv");
        }else if(n==5&&rev_string.contains("00 05 ")){
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM3("red");
        }else if(rev_string.contains("FF FF FF FF 03 00 16 00 09 7E C2 ")){
            n=15;
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM3("lv");
        }else if(n==15&&rev_string.contains("00 05 ")){
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM3("red");
        }else if(rev_string.contains("FF FF FF FF 03 00 18 00 03 9F 06 ")){
            n=6;
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM3("lv");
        }else if(n==6&&rev_string.contains("00 05 ")){
            ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM3("red");
            //零压力

        }else if(rev_string.contains("FF FF FF FF 03 00 1C 00 03 DE C7 ")){
            n=7;
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM4("lv");
        }else if(n==7&&rev_string.contains("00 09 ")){
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM4("red");
        }else if(rev_string.contains("FF FF FF FF 03 00 1F 00 09 AE C0 ")){
            n=14;
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM4("lv");
        }else if(n==14&&rev_string.contains("00 09 ")&&!rev_string.contains("BF 0B ")&&!rev_string.contains("AE C0 ")&&!rev_string.contains("7E C2 ")){
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM4("red");
        }else if(rev_string.contains("FF FF FF FF 03 00 20 00 03 1E CB ")){
            n=8;
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM4("lv");
        }else if(n==8&&rev_string.contains("00 09 ")){
            ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM4("red");
            //止鼾

        }else if(rev_string.contains("FF FF FF FF 03 00 3A 00 09 BF 0B ")){
            n=13;
            ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM5("lv");
        }else if(n==13&&rev_string.contains("00 0F ")){
            ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM5("red");
        }else if(rev_string.contains("FF FF FF FF 03 00 38 00 03 9E CC ")){
            n=9;
            ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_normal);
            Prefer.getInstance().setM5("lv");
        }else if(n==9&&rev_string.contains("00 0F ")){

            ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_selected);
            Prefer.getInstance().setM5("red");
        }else {
            n=10;
        }



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除广播接收器
        if (timecount != null) {
            timecount.cancel();
        }
        if (timecounts != null) {
            timecounts.cancel();
        }
        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    // Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
    @Override
    public void onResume() {
        super.onResume();
        //绑定广播接收器
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        SharedPreferences read = getActivity().getSharedPreferences("type", MODE_PRIVATE);
        askState();
        String value = read.getString("TYPE", "");

        LL_S4.setVisibility(View.GONE);
        vone.setVisibility(View.GONE);
        vtwo.setVisibility(View.GONE);
        haohua_anniu.setVisibility(View.GONE);
        if (value.equals("4") || value.equals("5")) {
            haohua_anniu.setVisibility(View.VISIBLE);

        }else if (value.equals("9") ) {
            LL_S4.setVisibility(View.VISIBLE);

        } else {
            vone.setVisibility(View.VISIBLE);
            vtwo.setVisibility(View.VISIBLE);
            if (value.equals("6")||value.equals("8")) {
                ll_fuyuan.setVisibility(View.GONE);
                ll_kq_fuyuan.setVisibility(View.VISIBLE);
                ll_kq_shuimian.setVisibility(View.VISIBLE);
                ll_shuimian.setVisibility(View.GONE);
            }
        }
        Log.e("什么类型", value);
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


    public void askState() {
        SharedPreferences read = getActivity().getSharedPreferences("type", MODE_PRIVATE);
        final String val = read.getString("TYPE", "");
        k = 1;
//        ll_dianshi.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//        Prefer.getInstance().setM3("lv");
//        ll_lingyali.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//        Prefer.getInstance().setM4("lv");
//        tv_M_first.setBackgroundResource(R.drawable.ic_home_ssk);
//        Prefer.getInstance().setM1("lv");
//        tv_M_two.setBackgroundResource(R.drawable.ic_home_ssk);
//        Prefer.getInstance().setM2("lv");
//        ll_shuimian.setBackgroundResource(R.drawable.ic_kuaijie_normal);
//        Prefer.getInstance().setM5("lv");
        target_chara = MyApplication.getInstance().gattCharacteristic;

        LogUtils.e("==蓝牙特征值2==", "" + MyApplication.getInstance().gattCharacteristic);
        if (timecounts != null) {
            timecounts.cancel();
        }
        timecounts = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                    //ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                    LogUtils.e("==ssssssssssss==", Prefer.getInstance().getBleStatus());
                } else {
                    //看电视
                    byte[] value9098 = new byte[11];
                    value9098[0] = (byte) 0xff;
                    value9098[1] = (byte) 0xff;
                    value9098[2] = (byte) 0xff;
                    value9098[3] = (byte) 0xff;
                    value9098[4] = (byte) 0x03;
                    value9098[5] = (byte) 0x00;
                    value9098[6] = (byte) 0x16;
                    value9098[7] = (byte) 0x00;
                    value9098[8] = (byte) 0x09;
                    value9098[9] = (byte) 0x7e;
                    value9098[10] = (byte) 0xc2;
                    //kq/kqh
                    byte[] value90981 = new byte[11];
                    value90981[0] = (byte) 0xff;
                    value90981[1] = (byte) 0xff;
                    value90981[2] = (byte) 0xff;
                    value90981[3] = (byte) 0xff;
                    value90981[4] = (byte) 0x03;
                    value90981[5] = (byte) 0x00;
                    value90981[6] = (byte) 0x14;
                    value90981[7] = (byte) 0x00;
                    value90981[8] = (byte) 0x03;
                    value90981[9] = (byte) 0x5f;
                    value90981[10] = (byte) 0x05;
                    //mq
                    byte[] value90982 = new byte[11];
                    value90982[0] = (byte) 0xff;
                    value90982[1] = (byte) 0xff;
                    value90982[2] = (byte) 0xff;
                    value90982[3] = (byte) 0xff;
                    value90982[4] = (byte) 0x03;
                    value90982[5] = (byte) 0x00;
                    value90982[6] = (byte) 0x18;
                    value90982[7] = (byte) 0x00;
                    value90982[8] = (byte) 0x03;
                    value90982[9] = (byte) 0x9f;
                    value90982[10] = (byte) 0x06;
                    if(val.equals("6")||val.equals("8")){
                        target_chara.setValue(value90981);
                    }else if(val.equals("7")){
                        target_chara.setValue(value90982);
                    }else {
                        target_chara.setValue(value9098);
                    }
                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                    k = 2;
                    if (timecount != null) {
                        timecount.cancel();
                    }
                    timecount = new CountDownTimer(200, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            LogUtils.e("==ssssssssssss==", "倒计时");
                        }

                        @Override
                        public void onFinish() {
                           // target_chara = MyApplication.getInstance().gattCharacteristic;
                            if (!Prefer.getInstance().getBleStatus().equals("已连接") || target_chara == null) {
                                //ToastUtils.showToast(getContext(), getResources().getString(R.string.no_contect));
                                LogUtils.e("==ssssssssssss==", "没链接");
                            } else {
                                LogUtils.e("==ssssssssssss==", "发送了q1");
                                if (k == 2) {
                                    //零压力
                                    byte[] value123 = new byte[11];
                                    value123[0] = (byte) 0xff;
                                    value123[1] = (byte) 0xff;
                                    value123[2] = (byte) 0xff;
                                    value123[3] = (byte) 0xff;
                                    value123[4] = (byte) 0x03;
                                    value123[5] = (byte) 0x00;
                                    value123[6] = (byte) 0x1f;
                                    value123[7] = (byte) 0x00;
                                    value123[8] = (byte) 0x09;
                                    value123[9] = (byte) 0xae;
                                    value123[10] = (byte) 0xc0;
                                    byte[] value223 = new byte[11];
                                    value223[0] = (byte) 0xff;
                                    value223[1] = (byte) 0xff;
                                    value223[2] = (byte) 0xff;
                                    value223[3] = (byte) 0xff;
                                    value223[4] = (byte) 0x03;
                                    value223[5] = (byte) 0x00;
                                    value223[6] = (byte) 0x1c;
                                    value223[7] = (byte) 0x00;
                                    value223[8] = (byte) 0x03;
                                    value223[9] = (byte) 0xde;
                                    value223[10] = (byte) 0xc7;
                                    byte[] value323 = new byte[11];
                                    value323[0] = (byte) 0xff;
                                    value323[1] = (byte) 0xff;
                                    value323[2] = (byte) 0xff;
                                    value323[3] = (byte) 0xff;
                                    value323[4] = (byte) 0x03;
                                    value323[5] = (byte) 0x00;
                                    value323[6] = (byte) 0x20;
                                    value323[7] = (byte) 0x00;
                                    value323[8] = (byte) 0x03;
                                    value323[9] = (byte) 0x1e;
                                    value323[10] = (byte) 0xcb;
                                    if(val.equals("6")||val.equals("8")){
                                        target_chara.setValue(value223);
                                    }else if(val.equals("7")){
                                        target_chara.setValue(value323);
                                    }else {
                                        target_chara.setValue(value123);
                                    }
                                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                                    k = 3;
                                    timecount.start();
                                    LogUtils.e("==ssssssssssss==", "发送了q2");
                                } else if (k == 3) {
                                    //M1
                                    byte[] value1234 = new byte[11];
                                    value1234[0] = (byte) 0xff;
                                    value1234[1] = (byte) 0xff;
                                    value1234[2] = (byte) 0xff;
                                    value1234[3] = (byte) 0xff;
                                    value1234[4] = (byte) 0x03;
                                    value1234[5] = (byte) 0x00;
                                    value1234[6] = (byte) 0x28;
                                    value1234[7] = (byte) 0x00;
                                    value1234[8] = (byte) 0x09;
                                    value1234[9] = (byte) 0x1f;
                                    value1234[10] = (byte) 0x0e;
                                    //kqkqh
                                    byte[] value1334 = new byte[11];
                                    value1334[0] = (byte) 0xff;
                                    value1334[1] = (byte) 0xff;
                                    value1334[2] = (byte) 0xff;
                                    value1334[3] = (byte) 0xff;
                                    value1334[4] = (byte) 0x03;
                                    value1334[5] = (byte) 0x00;
                                    value1334[6] = (byte) 0x24;
                                    value1334[7] = (byte) 0x00;
                                    value1334[8] = (byte) 0x03;
                                    value1334[9] = (byte) 0x5f;
                                    value1334[10] = (byte) 0x0a;
                                    //mq
                                    byte[] value1434 = new byte[11];
                                    value1434[0] = (byte) 0xff;
                                    value1434[1] = (byte) 0xff;
                                    value1434[2] = (byte) 0xff;
                                    value1434[3] = (byte) 0xff;
                                    value1434[4] = (byte) 0x03;
                                    value1434[5] = (byte) 0x00;
                                    value1434[6] = (byte) 0x28;
                                    value1434[7] = (byte) 0x00;
                                    value1434[8] = (byte) 0x03;
                                    value1434[9] = (byte) 0x9f;
                                    value1434[10] = (byte) 0x09;
                                    if(val.equals("6")||val.equals("8")){
                                        target_chara.setValue(value1334);
                                    }else if(val.equals("7")){
                                        target_chara.setValue(value1434);
                                    }else {
                                        target_chara.setValue(value1234);
                                    }

                                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                                    LogUtils.e("==ssssssssssss==", "发送了q3");
                                    k = 4;
                                    timecount.start();
                                } else if (k == 4) {
                                    //M2
                                    byte[] value12345 = new byte[11];
                                    value12345[0] = (byte) 0xff;
                                    value12345[1] = (byte) 0xff;
                                    value12345[2] = (byte) 0xff;
                                    value12345[3] = (byte) 0xff;
                                    value12345[4] = (byte) 0x03;
                                    value12345[5] = (byte) 0x00;
                                    value12345[6] = (byte) 0x31;
                                    value12345[7] = (byte) 0x00;
                                    value12345[8] = (byte) 0x09;
                                    value12345[9] = (byte) 0xce;
                                    value12345[10] = (byte) 0xc9;
                                    byte[] value13345 = new byte[11];
                                    value13345[0] = (byte) 0xff;
                                    value13345[1] = (byte) 0xff;
                                    value13345[2] = (byte) 0xff;
                                    value13345[3] = (byte) 0xff;
                                    value13345[4] = (byte) 0x03;
                                    value13345[5] = (byte) 0x00;
                                    value13345[6] = (byte) 0x2c;
                                    value13345[7] = (byte) 0x00;
                                    value13345[8] = (byte) 0x03;
                                    value13345[9] = (byte) 0xde;
                                    value13345[10] = (byte) 0xc8;
                                    byte[] value14345 = new byte[11];
                                    value14345[0] = (byte) 0xff;
                                    value14345[1] = (byte) 0xff;
                                    value14345[2] = (byte) 0xff;
                                    value14345[3] = (byte) 0xff;
                                    value14345[4] = (byte) 0x03;
                                    value14345[5] = (byte) 0x00;
                                    value14345[6] = (byte) 0x30;
                                    value14345[7] = (byte) 0x00;
                                    value14345[8] = (byte) 0x03;
                                    value14345[9] = (byte) 0x1F;
                                    value14345[10] = (byte) 0x0e;
                                    if(val.equals("6")||val.equals("8")){
                                        target_chara.setValue(value13345);
                                    }else if(val.equals("7")){
                                        target_chara.setValue(value14345);
                                    }else {
                                        target_chara.setValue(value12345);
                                    }

                                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                                    LogUtils.e("==ssssssssssss==", "发送了q4");
                                    k = 5;
                                    timecount.start();
                                } else if (k == 5) {
                                    //止鼾
                                    byte[] value12 = new byte[11];
                                    value12[0] = (byte) 0xff;
                                    value12[1] = (byte) 0xff;
                                    value12[2] = (byte) 0xff;
                                    value12[3] = (byte) 0xff;
                                    value12[4] = (byte) 0x03;
                                    value12[5] = (byte) 0x00;
                                    value12[6] = (byte) 0x3a;
                                    value12[7] = (byte) 0x00;
                                    value12[8] = (byte) 0x09;
                                    value12[9] = (byte) 0xbf;
                                    value12[10] = (byte) 0x0b;
                                    byte[] value21 = new byte[11];
                                    value21[0] = (byte) 0xff;
                                    value21[1] = (byte) 0xff;
                                    value21[2] = (byte) 0xff;
                                    value21[3] = (byte) 0xff;
                                    value21[4] = (byte) 0x03;
                                    value21[5] = (byte) 0x00;
                                    value21[6] = (byte) 0x38;
                                    value21[7] = (byte) 0x00;
                                    value21[8] = (byte) 0x03;
                                    value21[9] = (byte) 0x9e;
                                    value21[10] = (byte) 0xcc;
                                    if(val.equals("7")){
                                        target_chara.setValue(value21);
                                    }else {
                                        target_chara.setValue(value12);
                                    }
                                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                                    LogUtils.e("==ssssssssssss==", "发送了q5");
                                    k = 6;
                                }else if (k == 6) {
                                    byte[] value12 = new byte[11];
                                    value12[0] = (byte) 0xff;
                                    value12[1] = (byte) 0xff;
                                    value12[2] = (byte) 0xff;
                                    value12[3] = (byte) 0xff;
                                    value12[4] = (byte) 0x03;
                                    value12[5] = (byte) 0x00;
                                    value12[6] = (byte) 0x3a;
                                    value12[7] = (byte) 0x00;
                                    value12[8] = (byte) 0x09;
                                    value12[9] = (byte) 0xbf;
                                    value12[10] = (byte) 0x0b;
                                    target_chara.setValue(value12);
                                    MyApplication.getInstance().mBluetoothLeService.writeCharacteristic(target_chara);
                                    LogUtils.e("==ssssssssssss==", "发送了q5");
                                    k = 7;
                                }
                            }
                        }
                    }.start();
                }
            }
        }.start();
    }


}
