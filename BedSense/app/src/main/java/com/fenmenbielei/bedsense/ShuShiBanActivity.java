package com.fenmenbielei.bedsense;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


import com.fenmenbielei.bedsense.base.BaseActivity;
import com.fenmenbielei.bedsense.dialog.WaitDialog;
import com.fenmenbielei.bedsense.fragment.HHAnMoFragment;
import com.fenmenbielei.bedsense.fragment.HHDengGuangFragment;
import com.fenmenbielei.bedsense.fragment.HHKuaiJieFragment;
import com.fenmenbielei.bedsense.fragment.HHWeiTiaoFragment;
import com.fenmenbielei.bedsense.impl.ActionBarClickListener;
import com.fenmenbielei.bedsense.javabean.TestBean;
import com.fenmenbielei.bedsense.service.BluetoothLeService;
import com.fenmenbielei.bedsense.ui.ExplainActivity;
import com.fenmenbielei.bedsense.ui.LanguageActivity;
import com.fenmenbielei.bedsense.ui.OpenBleActivity;
import com.fenmenbielei.bedsense.ui.QrCodeActivity;
import com.fenmenbielei.bedsense.uitls.CountDownTimerUtils;
import com.fenmenbielei.bedsense.uitls.LogUtils;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.fenmenbielei.bedsense.uitls.SystemUtils;
import com.fenmenbielei.bedsense.uitls.ToastUtils;
import com.fenmenbielei.bedsense.view.NoScrollViewPager;
import com.fenmenbielei.bedsense.view.TranslucentActionBar;
import com.wnhz.shidaodianqi.R;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShuShiBanActivity extends BaseActivity implements View.OnClickListener, ActionBarClickListener {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;

    //?????????
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

    //?????????
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_try)
    TextView tv_try;
    @BindView(R.id.tv_connect_time)
    TextView tv_connect_time;

    //?????????????????????
    @BindView(R.id.ll_left)
    RelativeLayout ll_left;
    //??????????????????
    @BindView(R.id.ll_center)
    LinearLayout ll_center;
    //??????????????????
    @BindView(R.id.ll_right)
    RelativeLayout ll_right;

    //?????????
    private String curActivity = "1";
    private boolean isClick = false;
    private String first = "1";

    private TextView text1, text2, text3, text4;
    private List<TextView> textViews = new ArrayList<>();
    private NoScrollViewPager vpMain;
    private int textColor;

    private Handler mhandler = new Handler();
    private WaitDialog waitDialog;

    //????????????copy?????????  ?????????
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int BLE_STATUS = 222;

    //?????????
    //??????4.0???UUID,??????0000ffe1-0000-1000-8000-00805f9b34fb???????????????????????????????????????08???????????????UUID
    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    //???????????????
    private static BluetoothGattCharacteristic target_chara = null;
    //??????service,???????????????????????????
    private static BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    // ???????????????
    BluetoothAdapter mBluetoothAdapter;
    // ??????????????????
    private ArrayList<Integer> rssis;
    // ?????????Adapter
    LeDeviceListAdapter mleDeviceListAdapter;
    // ???????????????????????????
    private boolean mScanning;
    private boolean scan_flag;
    // ??????????????????
    private static final long SCAN_PERIOD = 5000;

    private String currentDecice = Prefer.getInstance().getCurrentDecice();

    private JumpingBeans mJumpingBeans;
    private List<TestBean> testBeanList = new ArrayList<>();
    private CountDownTimerUtils mCountDownTimerUtils;
    private Intent gattServiceIntent;
    private SharedPreferences mySharedPreferences;

    private SharedPreferences.Editor edit;

    //??????????????????
    // private SharedPreferences.Editor type;

    //??????????????????
    private String mConnected = "0";
    private String status = "disconnected";
    private TestBean testBean;
    private String rev_str;
    private BluetoothAdapter blueadapter;
    private Animation slide_left_to_left;
    private Animation slide_right_to_left;
    private Animation slide_left_to_right;
    private Animation slide_left_to_left_in;

    HHKuaiJieFragment homeFragment1 = HHKuaiJieFragment.newInstance(rev_str);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shu_shi_ban);
        ButterKnife.bind(this);
        SharedPreferences read = getSharedPreferences("type", MODE_PRIVATE);

        String value = read.getString("TYPE", "");
        rev_str = value;
        if (value.equals("1")) {
            actionbar.setData("BEDSENSE-850", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
        } else if (value.equals("2")) {
            actionbar.setData("BEDSENSE-890", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
        } else if (value.equals("4")) {
            actionbar.setData("BEDSENSE-1150", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
        } else if (value.equals("5")) {
            actionbar.setData("BEDSENSE-1050", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
        } else if (value.equals("6")) {
            actionbar.setData("BEDSENSE-250", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
        } else if (value.equals("7")) {
            actionbar.setData("BEDSENSE-150", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
        } else if (value.equals("8")) {
            actionbar.setData("BEDSENSE-350", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
        } else if (value.equals("9")) {
            actionbar.setData("Permobil Resten Hi-Low", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
        } else {
            actionbar.setData(getResources().getString(R.string.text_scan_near_d), R.mipmap.ic_default_return, null, 0, null, this);
            first = "1";
        }
        Log.e("shushi", value);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        // ?????????????????????????????????
        blueadapter = BluetoothAdapter.getDefaultAdapter();
        if (blueadapter == null) {
            showDialogv7("?????????????????????????????????", "", "?????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            return;
        }

        //???????????????????????????
        mySharedPreferences = getSharedPreferences("scenelist", Context.MODE_PRIVATE);
        edit = mySharedPreferences.edit();

//        SharedPreferences read = getSharedPreferences("type", MODE_PRIVATE);
//
//        String value = read.getString("TYPE", "");
        //Toast.makeText(getApplicationContext(), "????????????"+value, Toast.LENGTH_LONG).show();

        // Initializes Bluetooth adapter.
        // ????????????????????????????????????
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        /* ????????????service */
        gattServiceIntent = new Intent(ShuShiBanActivity.this, BluetoothLeService.class);


//        //??????SP
//        sp = getSharedPreferences("config", 0);
//        //????????????????????????????????????<2.0>
//        String name=null;
//        String pass=null;
//        //????????????????????? key  ,?????????
//        name = sp.getString("name", "");

        initView();
        initData();
//
//        ll_left.startAnimation(slide_left_to_left);
//        ll_left.setVisibility(View.GONE);
//        ll_center.startAnimation(slide_left_to_left);
//        ll_center.setVisibility(View.GONE);
//        ll_right.startAnimation(slide_right_to_left);
//        ll_right.setVisibility(View.VISIBLE);
    }

    private void initView() {
        waitDialog = new WaitDialog(ShuShiBanActivity.this, getResources().getString(R.string.contect));

        slide_left_to_left = AnimationUtils.loadAnimation(ShuShiBanActivity.this, R.anim.slide_left_to_left);
        slide_right_to_left = AnimationUtils.loadAnimation(ShuShiBanActivity.this, R.anim.slide_right_to_left);
        slide_left_to_right = AnimationUtils.loadAnimation(ShuShiBanActivity.this, R.anim.slide_left_to_right);
        slide_left_to_left_in = AnimationUtils.loadAnimation(ShuShiBanActivity.this, R.anim.slide_left_to_left_in);

        vpMain = (NoScrollViewPager) findViewById(R.id.vp_main);
        findViewById(R.id.tab1).setOnClickListener(this);
        findViewById(R.id.tab2).setOnClickListener(this);
        findViewById(R.id.tab3).setOnClickListener(this);
        findViewById(R.id.tab4).setOnClickListener(this);
        text1 = (TextView) findViewById(R.id.main_text1);
        text2 = (TextView) findViewById(R.id.main_text2);
        text3 = (TextView) findViewById(R.id.main_text3);
        text4 = (TextView) findViewById(R.id.main_text4);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        textViews.add(text1);
        textViews.add(text2);
        textViews.add(text3);
        textViews.add(text4);

        textColor = getResources().getColor(R.color.white);
        text1.setBackgroundColor(getResources().getColor(R.color.blue));

        tv_version.setText(SystemUtils.getVersionName(ShuShiBanActivity.this));
        ll_connect.setOnClickListener(this);
        ll_qr_code.setOnClickListener(this);
        ll_language.setOnClickListener(this);
        ll_explain.setOnClickListener(this);
        sw_on_off.setOnClickListener(this);

        final String liststr = mySharedPreferences.getString("LIST", "");
        try {
            testBeanList = MyApplication.String2SceneList(liststr);
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //?????????????????????
        LogUtils.e("==??????????????????????????????????????????==", "" + testBeanList.size());


        //??????????????????copy??????
        // Initializes Bluetooth adapter.
        // ????????????????????????????????????
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        scan_flag = true;
        tv_try.setOnClickListener(this);
        ll_search.setOnClickListener(this);

        // ??????????????????
        mleDeviceListAdapter = new LeDeviceListAdapter();
        // ???listview???????????????
        lv.setAdapter(mleDeviceListAdapter);

		/* listview???????????? */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, final int position, long id) {
                testBean = (TestBean) mleDeviceListAdapter.getItem(position);
                if (testBeanList.get(position).isConnected()) {
                    showDialogv7(getResources().getString(R.string.Disconnect_not), getResources().getString(R.string.cancel), getResources().getString(R.string.unclickscan), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (int j = 0; j < testBeanList.size(); j++) {
                                testBeanList.get(j).setConnected(false);
                            }

                            mBluetoothLeService.disconnect();

                            Prefer.getInstance().setCurrentDecice("");
                            mleDeviceListAdapter.notifyDataSetChanged();
                            Prefer.getInstance().setBleStatus("?????????");
                            SharedPreferences.Editor editor = getSharedPreferences("type", Context.MODE_PRIVATE).edit();
                            editor.putString("TYPE", "3");
                            tv_connect.setText(getResources().getString(R.string.no_contect));

//                            ll_center.startAnimation(slide_left_to_left_in);
//                            ll_center.setVisibility(View.VISIBLE);
//                            ll_right.startAnimation(slide_left_to_right);
//                            ll_right.setVisibility(View.GONE);
                            //curActivity="2";
                        }
                    });
                } else {
                    //?????????  --  ??????????????????????????????????????????????????????
                    if (!isCheckAllChild()) {
                        ToastUtils.showToast(ShuShiBanActivity.this, getResources().getString(R.string.bluetoothretry));
                    } else {
                        if (mScanning) {
                            /* ?????????????????? */
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            mScanning = false;
                        }

                        waitDialog.setCanceledOnTouchOutside(false);
                        waitDialog.show();
                        currentDecice = testBeanList.get(position).getAddress();
                        LogUtils.e("==??????item==", "?????????" + currentDecice);

                        // LogUtils.e("==??????item==", "baocun" + currentDecice);

                        SharedPreferences.Editor editor = getSharedPreferences("type", Context.MODE_PRIVATE).edit();

                        //??????2-2????????????????????????????????????
                         if (testBeanList.get(position).isShuShi() ) {
                            editor.putString("TYPE", "1");
                        } else if (testBeanList.get(position).isTriMix4() ) {
                            editor.putString("TYPE", "2");
                        } else if (testBeanList.get(position).isIQ() ) {
                            editor.putString("TYPE", "4");
                        } else if ( testBeanList.get(position).isLQ() ) {
                            editor.putString("TYPE", "5");
                        } else if (testBeanList.get(position).isKQ() ) {
                            editor.putString("TYPE", "6");
                        } else if (testBeanList.get(position).isMQ() ) {
                            editor.putString("TYPE", "7");
                        } else if (testBeanList.get(position).isKQH()) {
                            editor.putString("TYPE", "8");
                        } else if (testBeanList.get(position).isS4()) {
                             editor.putString("TYPE", "9");
                         }else {
                            editor.putString("TYPE", "3");
                        }
                        //??????3?????????
                        editor.commit();
                        initData();
                        // restartAct();
                        mBluetoothLeService.disconnect();

                        mBluetoothLeService.connect(currentDecice);
                        Prefer.getInstance().setCurrentDecice(currentDecice);
                    }
                }
            }
        });
    }

    //??????????????????
    private void hideFragment(int index) {
        for (int i = 0; i < textViews.size(); i++) {
            if (i == index) {
                textViews.get(i).setTextColor(textColor);
                textViews.get(i).setBackgroundColor(getResources().getColor(R.color.blue));
            } else {
                textViews.get(i).setTextColor(textColor);
                textViews.get(i).setBackgroundColor(getResources().getColor(R.color.bg_hui));
            }
        }
        vpMain.setCurrentItem(index, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_text1:
                hideFragment(0);
                break;
            case R.id.main_text2:
                hideFragment(1);
                break;
            case R.id.main_text3:
                hideFragment(2);
                break;
            case R.id.main_text4:
                hideFragment(3);
                break;
            //????????????
            case R.id.ll_connect:
                if (!blueadapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    return;
                }

                actionbar.setData(getResources().getString(R.string.text_scan_near_d), R.mipmap.ic_default_return, null, 0, null, this);

                if (tv_connect.getText().equals(getResources().getString(R.string.no_contect))) {
                    testBeanList.clear();
                    mleDeviceListAdapter.notifyDataSetChanged();
                }

                ll_center.startAnimation(slide_left_to_left);
                ll_center.setVisibility(View.GONE);
                ll_right.startAnimation(slide_right_to_left);
                ll_right.setVisibility(View.VISIBLE);

                curActivity = "3";
                break;
            //?????????
            case R.id.ll_qr_code:
                startActivity(new Intent(ShuShiBanActivity.this, QrCodeActivity.class));
                break;
            //????????????
            case R.id.sw_on_off:
                sw_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            ToastUtils.showToast(ShuShiBanActivity.this, getResources().getString(R.string.opens));
                        } else {
                            ToastUtils.showToast(ShuShiBanActivity.this, getResources().getString(R.string.close));
                        }
                    }
                });
                break;
            //????????????
            case R.id.ll_language:
                startActivity(new Intent(ShuShiBanActivity.this, LanguageActivity.class));
                break;
            //????????????
            case R.id.ll_explain:
                startActivity(new Intent(ShuShiBanActivity.this, ExplainActivity.class));
                break;
            case R.id.tv_try:
                testBeanList.clear();
                if (scan_flag) {
                    mCountDownTimerUtils = new CountDownTimerUtils(tv_connect_time, 5000, 1000);
                    mCountDownTimerUtils.start();

                    mleDeviceListAdapter = new LeDeviceListAdapter();
                    lv.setAdapter(mleDeviceListAdapter);
                    scanLeDevice(true);
                } else {
                    scanLeDevice(false);
                    tv_try.setText(getResources().getString(R.string.saomiao));
                }
            default:
                break;
        }
    }

    public void initData() {
        List<Fragment> listFragent = new ArrayList<>();
        SharedPreferences read = getSharedPreferences("type", MODE_PRIVATE);

        String value = read.getString("TYPE", "");
        listFragent.add(homeFragment1);
        HHWeiTiaoFragment homeFragment2 = HHWeiTiaoFragment.newInstance(value);
        listFragent.add(homeFragment2);
        HHAnMoFragment homeFragment3 = HHAnMoFragment.newInstance();
        listFragent.add(homeFragment3);
        HHDengGuangFragment homeFragment4 = HHDengGuangFragment.newInstance();
        listFragent.add(homeFragment4);
        MainPagerAdapter pageAdapter = new MainPagerAdapter(listFragent);
        vpMain.setAdapter(pageAdapter);
        vpMain.setScroll(true);
        vpMain.setOffscreenPageLimit(4);
    }

    @Override
    public void onLeftClick() {
        if (curActivity.equals("1")) {
            Prefer.getInstance().setCurrentDecice(Prefer.getInstance().getCurrentDecice());
            //mBluetoothLeService.disconnect();
            finish();
        } else if (curActivity.equals("2")) {
            curActivity = "1";
            SharedPreferences read = getSharedPreferences("type", MODE_PRIVATE);

            String value = read.getString("TYPE", "");
            if (value.equals("1"))
                actionbar.setData("BEDSENSE-850", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
            else if (value.equals("2")) {
                actionbar.setData("BEDSENSE-890", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
            } else if (value.equals("5")) {
                actionbar.setData("BEDSENSE-1050", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
            } else if (value.equals("4")) {
                actionbar.setData("BEDSENSE-1150", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
            } else if (value.equals("6")) {
                actionbar.setData("BEDSENSE-250", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
            } else if (value.equals("7")) {
                actionbar.setData("BEDSENSE-150", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
            } else if (value.equals("8")) {
                actionbar.setData("BEDSENSE-350", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
            }else if (value.equals("9")) {
                actionbar.setData("BEDSENSE-1050", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
            }

            ll_left.startAnimation(slide_left_to_left_in);
            ll_left.setVisibility(View.VISIBLE);
            ll_center.startAnimation(slide_left_to_right);
            ll_center.setVisibility(View.GONE);
        } else if (curActivity.equals("3")) {
            curActivity = "2";
            actionbar.setData(getResources().getString(R.string.setting), R.mipmap.ic_default_return, null, 0, null, this);

            LogUtils.e("== ??????????????????==", "" + isCheckAllChild());
            if (!isCheckAllChild()) {
                try {
                    //??????????????????????????????String??? ????????????
                    String liststr = MyApplication.getInstance().SceneList2String(testBeanList);
                    edit.putString("LIST", liststr);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Prefer.getInstance().setCurrentDecice(currentDecice);
                Prefer.getInstance().setBleStatus("?????????");
                LogUtils.e("==????????????==", "lianjie");
                homeFragment1.askState();
                tv_connect.setText(getResources().getString(R.string.contected));

            } else {
                testBeanList.clear();

                Prefer.getInstance().setBleStatus("?????????");
                tv_connect.setText(getResources().getString(R.string.no_contect));
            }

            ll_center.startAnimation(slide_left_to_left_in);
            ll_center.setVisibility(View.VISIBLE);
            ll_right.startAnimation(slide_left_to_right);
            ll_right.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRightClick() {
        if (curActivity.equals("1")) {
            actionbar.setData(getResources().getString(R.string.setting), R.mipmap.ic_default_return, null, 0, null, this);

            ll_left.startAnimation(slide_left_to_left);
            ll_left.setVisibility(View.GONE);
            ll_center.startAnimation(slide_right_to_left);
            ll_center.setVisibility(View.VISIBLE);
            curActivity = "2";
        }
    }

    //????????? ?????????
    class MainPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList;

        public MainPagerAdapter(List<Fragment> listist) {
            super(getSupportFragmentManager());
            fragmentList = listist;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            if (fragmentList == null) {
                return 0;
            }
            return fragmentList.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //?????????????????????
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
        mBluetoothLeService.disconnect();
        mBluetoothLeService = null;
    }

    // Activity?????????????????????????????????????????????????????????????????????????????????
    @Override
    protected void onResume() {
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        if (Prefer.getInstance().getCurrentDecice().equals("")||Prefer.getInstance().getBleStatus().equals("")) {
            LogUtils.e("==onResume ??????==", Prefer.getInstance().getCurrentDecice()+Prefer.getInstance().getBleStatus());
            Prefer.getInstance().setBleStatus("?????????");
            tv_connect.setText(getResources().getString(R.string.no_contect));
            ll_left.startAnimation(slide_left_to_left);
            ll_left.setVisibility(View.GONE);
            ll_center.startAnimation(slide_right_to_left);
            ll_center.setVisibility(View.GONE);
            ll_right.startAnimation(slide_left_to_left);
            ll_right.setVisibility(View.VISIBLE);
            testBeanList.clear();
            if (scan_flag) {
                mCountDownTimerUtils = new CountDownTimerUtils(tv_connect_time, 5000, 1000);
                mCountDownTimerUtils.start();

                mleDeviceListAdapter = new LeDeviceListAdapter();
                lv.setAdapter(mleDeviceListAdapter);
                scanLeDevice(true);
            } else {
                scanLeDevice(false);
                tv_try.setText(getResources().getString(R.string.saomiao));
            }
//            initData();
//            showDialogv7("?????????????????????????????????", "", "??????", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    //????????????????????????
//                    ll_left.startAnimation(slide_left_to_left);
//                    ll_left.setVisibility(View.GONE);
//                    ll_center.startAnimation(slide_right_to_left);
//                    ll_center.setVisibility(View.VISIBLE);
//                }
//            });
        }

        isClick = false;
        //?????????????????????
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            startActivity(new Intent(ShuShiBanActivity.this, OpenBleActivity.class));
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isCheckAllChild() {
        for (int j = 0; j < testBeanList.size(); j++) {
            TestBean testBean = (TestBean) testBeanList.get(j);
            if (testBean.isConnected()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param enable (???????????????true:????????????,false:????????????)
     * @return void
     * @throws
     * @Title: scanLeDevice
     * @Description: TODO(??????????????????)
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scan_flag = true;
                    tv_try.setText(getResources().getString(R.string.saomiao));
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    LogUtils.e("==5s?????????????????????==", "stoping................");
                }
            }, SCAN_PERIOD);
            /* ??????????????????????????????mLeScanCallback ???????????? */
            LogUtils.e("==????????????????????????==", "begin.....................");
            mScanning = true;
            scan_flag = false;
            tv_try.setText(getResources().getString(R.string.saomiaozhong));
            mJumpingBeans = JumpingBeans.with(tv_try).appendJumpingDots().build();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            LogUtils.e("==????????????????????????==", "stoping................");
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scan_flag = true;
            tv_try.setText(getResources().getString(R.string.text_scan_near_d));
            //?????????????????????
            curActivity = "1";

            actionbar.setData(getResources().getString(R.string.no_contect), R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
            //   actionbar.setData("?????????", R.mipmap.ic_default_return, null, 0, null, this);
            ll_left.startAnimation(slide_left_to_left);
            ll_left.setVisibility(View.VISIBLE);
            ll_right.startAnimation(slide_right_to_left);
            ll_right.setVisibility(View.GONE);
        }
    }

    /**
     * ???????????????????????? ???????????????????????????????????????BluetoothDevice???????????????name MAC?????????
     **/
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            // TODO Auto-generated method stub

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.e("==?????????????????????==", "" + device.getAddress() + "===" + device.getName());
                    /* ????????????????????????????????????listview???????????? */
                    mleDeviceListAdapter.addDevice(device, rssi);
                    mleDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    /**
     * @author ????????????????????????????????????
     * @Description: TODO<??????????????????Adapter,??????listview????????????>
     * @data: 2014-10-12 ??????10:46:30
     * @version: V1.0
     */
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;

        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            rssis = new ArrayList<Integer>();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, int rssi) {
            if (!mLeDevices.contains(device) && device.getName() != null) {
                mLeDevices.add(device);
                TestBean testBean = new TestBean();
                testBean.setConnected(false);
                testBean.setTitle(device.getName());
                testBean.setAddress(device.getAddress());
                testBean.setIsS4(false);
                testBean.setIsKQH(false);
                testBean.setIsShuShi(false);
                testBean.setIsTriMix4(false);
                testBean.setIsLQ(false);
                testBean.setIsKQ(false);
                testBean.setIsMQ(false);
                testBean.setIsIQ(false);
                if (device.getName().indexOf("JQ-D") != -1) {

                    testBean.setIsTriMix4(true);

                } else if (device.getName().indexOf("JQ") != -1||device.getName().indexOf("NQ") != -1) {

                    testBean.setIsShuShi(true);

                } else if (device.getName().indexOf("IQ") != -1) {
                    testBean.setIsIQ(true);

                } else if (device.getName().indexOf("LQ") != -1) {

                    testBean.setIsLQ(true);

                } else if (device.getName().indexOf("KQ") != -1 && device.getName().indexOf("H") == -1) {

                    testBean.setIsKQ(true);

                } else if (device.getName().indexOf("MQ") != -1 || device.getName().indexOf("UPS") != -1) {
                    testBean.setIsMQ(true);
                }else if(device.getName().indexOf("S4-2") != -1){
                    testBean.setIsS4(true);
                } else if (device.getName().indexOf("H") != -1&& device.getName().indexOf("S4") == -1) {
                    testBean.setIsKQH(true);

                }

                testBeanList.add(testBean);
                rssis.add(rssi);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
            testBeanList.clear();
            rssis.clear();
        }

        @Override
        public int getCount() {
            // return mLeDevices.size();
            return testBeanList.size();
        }

        @Override
        public Object getItem(int i) {
            // return mLeDevices.get(i);
            return testBeanList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        /**
         * ??????getview
         **/
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            // General ListView optimization code.
            // ??????listview??????????????????
            view = mInflator.inflate(R.layout.item_connect_layout, null);
            // ???????????????textview??????????????????
            // TextView deviceAddress = (TextView) view.findViewById(R.id.tv_deviceAddr);
            // TextView rssi = (TextView) view.findViewById(R.id.tv_rssi);
            TextView deviceName = (TextView) view.findViewById(R.id.tv_title);
            TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
            String device = testBeanList.get(i).getTitle().replace(":", "A").replace(";", "B").replace("<", "C")
                    .replace("=", "D").replace(">", "E").replace("?", "F");

//            String device = testBeanList.get(i).getTitle();
            deviceName.setText(device);
            if (testBeanList.get(i).isConnected()) {
                tvStatus.setText(getResources().getString(R.string.unclickscan));
            } else {
                tvStatus.setText(getResources().getString(R.string.clickscan));
            }
            //  deviceAddress.setText(device.getAddress());
            // rssi.setText("" + rssis.get(i));
            return view;
        }
    }

    /* BluetoothLeService????????????????????? */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                LogUtils.e("???????????????", "Unable to initialize Bluetooth");
                finish();
            }

            if (!TextUtils.isEmpty(Prefer.getInstance().getCurrentDecice())) {
                // ?????????????????????????????????
                LogUtils.e("==?????????????????????????????????==", "????????????????????????");
                if (isHasBle()) {

                    //???????????????????????????????????????????????????????????????????????????????????????
                    mBluetoothLeService.disconnect();

                    mBluetoothLeService.connect(Prefer.getInstance().getCurrentDecice());
                    //??????????????????
                    waitDialog.setCanceledOnTouchOutside(false);
                    waitDialog.show();

                    isClick = false;
                } else {
                    Prefer.getInstance().setBleStatus("?????????");
//                    showDialogv7("?????????????????????????????????", "", "??????", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
                    //????????????????????????
//                    actionbar.setTitle("??????????????????");
                    //               actionbar.setDatas("??????????????????", R.mipmap.ic_default_return, null, 0, null);
                    ll_left.startAnimation(slide_left_to_left);
                    ll_left.setVisibility(View.GONE);
                    ll_center.startAnimation(slide_right_to_left);
                    ll_center.setVisibility(View.GONE);
                    ll_right.startAnimation(slide_left_to_left);
                    ll_right.setVisibility(View.VISIBLE);
                    testBeanList.clear();
                    if (scan_flag) {
                        mCountDownTimerUtils = new CountDownTimerUtils(tv_connect_time, 5000, 1000);
                        mCountDownTimerUtils.start();

                        mleDeviceListAdapter = new LeDeviceListAdapter();
                        lv.setAdapter(mleDeviceListAdapter);
                        scanLeDevice(true);
                    } else {
                        scanLeDevice(false);
                        tv_try.setText(getResources().getString(R.string.saomiao));
                    }
//                        }
//                    });
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    //???????????????????????????????????????????????????????????????
    private boolean isHasBle() {
        if (testBeanList != null) {
            for (TestBean bean : testBeanList) {
                if (bean.getAddress().equals(Prefer.getInstance().getCurrentDecice())) {
                    return true;
                } else {

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ??????????????????????????????BluetoothLeService??????????????????
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            waitDialog.dismiss();
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))//Gatt????????????
            {
                Prefer.getInstance().setCurrentDecice(currentDecice);
                // mConnected = true;
                status = "connected";
                //??????????????????
                updateConnectionState(status);
                LogUtils.e("==?????????????????? ?????????==", status);
                tv_connect.setText(getResources().getString(R.string.contected));
                ToastUtils.showToast(ShuShiBanActivity.this, getResources().getString(R.string.contected));

                LogUtils.e("==first???==", first);
                if (first.equals("1")) {
                    //?????????????????????
                    ll_left.startAnimation(slide_left_to_left);
                    ll_left.setVisibility(View.VISIBLE);
                    ll_right.startAnimation(slide_right_to_left);
                    ll_right.setVisibility(View.GONE);

                    first = "2";
                } else {

                }
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //Gatt????????????
                first = "2";
                status = "disconnected";
                //??????????????????
                updateConnectionState(status);
                LogUtils.e("==?????????????????? ????????????==", status);

                Prefer.getInstance().setBleStatus("?????????");
                tv_connect.setText(getResources().getString(R.string.no_contect));
                if (!curActivity.equals("3") && curActivity.equals("1")) {
                    showDialogv7(getResources().getString(R.string.Bluetooth_connection_failed), "", getResources().getString(R.string.setting), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            curActivity = "2";
                            //actionbar.setData("??????", R.mipmap.ic_default_return, null, 0, null,);

                            mBluetoothLeService.disconnect();
                            //????????????????????????
                            ll_left.startAnimation(slide_left_to_left);
                            ll_left.setVisibility(View.GONE);
                            ll_center.startAnimation(slide_right_to_left);
                            ll_center.setVisibility(View.VISIBLE);
                            tv_connect.setText(getResources().getString(R.string.no_contect));
                        }
                    });
                } else {
                    first = "2";
                    ToastUtils.showToast(ShuShiBanActivity.this, getResources().getString(R.string.no_contect));
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //??????GATT?????????
                // Show all the supported services and characteristics on the
                // user interface.
                // Log.e("==?????????????????????????????????==", "" + MyApplication.getInstance().supportedGattServices.size());
                displayGattServices(mBluetoothLeService.getSupportedGattServices());

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action))//????????????
            {
                //???????????????????????????
                // displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                // Log.e("==???????????????????????????==", "" + intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    /* ?????????????????? */
    private void updateConnectionState(String status) {
        Message msg = new Message();
        msg.what = 1;
        Bundle b = new Bundle();
        b.putString("connect_state", status);
        msg.setData(b);
        //????????????????????????UI???textview???
        myHandler2.sendMessage(msg);
    }

    /* ??????????????? */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private Handler myHandler2 = new Handler() {
        // 2.????????????????????????
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // ?????????????????????
                case 1: {
                    if (!isClick) {
                        if (testBean != null) {
                            // ??????View
                            String state = msg.getData().getString("connect_state");
                            if (state.equals("connected")) {
                                testBean.setConnected(true);
                                Prefer.getInstance().setBleStatus("?????????");
                                homeFragment1.askState();
                                tv_connect.setText(getResources().getString(R.string.contected));
                            } else {
                                testBean.setConnected(false);
                                Prefer.getInstance().setBleStatus("?????????");
                                tv_connect.setText(getResources().getString(R.string.no_contect));
                            }
                        }
                    } else {
                        if (testBeanList != null) {
                            for (TestBean bean : testBeanList) {
                                if (bean.getAddress().equals(Prefer.getInstance().getCurrentDecice())) {
                                    bean.setConnected(true);
                                } else {
                                    bean.setConnected(true);
                                }
                            }
                        }
                    }
                    mleDeviceListAdapter.notifyDataSetChanged();
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };

    /**
     * @param @param rev_string(???????????????)
     * @return void
     * @throws
     * @Title: displayData
     * @Description: TODO(?????????????????????scrollview?????????)
     */
    private void displayData(String rev_string) {
        rev_str += rev_string;
        LogUtils.e("==???????????????????????????==", rev_string);
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: displayGattServices
     * @Description: TODO(??????????????????)
     */
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = "unknown_service";
        String unknownCharaString = "unknown_characteristic";
        // ????????????,???????????????????????????????????????
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        // ??????????????????????????????????????????????????????????????????
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
        // ????????????????????????????????????
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            // ??????????????????
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            // ??????????????????uuid??????????????????????????????SampleGattAttributes???????????????????????????
            gattServiceData.add(currentServiceData);
            // LogUtils.e("=????????????????????? Service Uuid==", "" + uuid);
            System.out.println("Service uuid:" + uuid);
            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
            // ?????????????????????????????????????????????????????????
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();
            // Loops through available Characteristics.
            // ????????????????????????????????????????????????????????????
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);

                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                if (gattCharacteristic.getUuid().toString().equals(HEART_RATE_MEASUREMENT)) {
                    // ??????????????????Characteristic??????????????????mOnDataAvailable.onCharacteristicRead()
                    mhandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mBluetoothLeService.readCharacteristic(gattCharacteristic);
                        }
                    }, 200);

                    LogUtils.e("========================???????????????=================", "==========");
                    MyApplication.getInstance().mBluetoothLeService = mBluetoothLeService;
                    MyApplication.getInstance().gattCharacteristic = gattCharacteristic;

                    LogUtils.e("==???????????????1==", "" + MyApplication.getInstance().gattCharacteristic);
                    homeFragment1.askState();
                    // ??????Characteristic???????????????,???????????????????????????????????????mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                    target_chara = gattCharacteristic;

                    // ??????????????????
                    // ???????????????????????????
                    mBluetoothLeService.writeCharacteristic(gattCharacteristic);
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    System.out.println("---descriptor UUID:" + descriptor.getUuid());
                    // ????????????????????????
                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
                    // true);
                }
                gattCharacteristicGroupData.add(currentCharaData);
            }
            // ?????????????????????????????????????????????????????????????????????
            mGattCharacteristics.add(charas);
            // ?????????????????????????????????????????????????????????
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (curActivity.equals("1")) {
                moveTaskToBack(true);
            } else if (curActivity.equals("2")) {
                SharedPreferences read = getSharedPreferences("type", MODE_PRIVATE);

                String value = read.getString("TYPE", "");

                if (value.equals("1")) {
                    actionbar.setData("BEDSENSE-850", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
                } else if (value.equals("2")) {
                    actionbar.setData("BEDSENSE-890", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
                } else if (value.equals("5")) {
                    actionbar.setData("BEDSENSE-1050", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
                } else if (value.equals("4")) {
                    actionbar.setData("BEDSENSE-1150", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
                } else if (value.equals("6")) {
                    actionbar.setData("BEDSENSE-250", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
                } else if (value.equals("7")) {
                    actionbar.setData("BEDSENSE-150", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
                } else if (value.equals("8")) {
                    actionbar.setData("BEDSENSE-350", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
                }else if (value.equals("9")) {
                    actionbar.setData("BEDSENSE-1050", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);
                }
                ll_left.startAnimation(slide_left_to_left_in);
                ll_left.setVisibility(View.VISIBLE);
                ll_center.startAnimation(slide_left_to_right);
                ll_center.setVisibility(View.GONE);
                curActivity = "1";
            } else if (curActivity.equals("3")) {

                actionbar.setData(getResources().getString(R.string.setting), R.mipmap.ic_default_return, null, 0, null, this);

                LogUtils.e("== ??????????????????==", "" + isCheckAllChild());
                if (!isCheckAllChild()) {
                    try {
                        //??????????????????????????????String??? ????????????
                        String liststr = MyApplication.getInstance().SceneList2String(testBeanList);
                        edit.putString("LIST", liststr);
                        edit.commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Prefer.getInstance().setCurrentDecice(currentDecice);
                    Prefer.getInstance().setBleStatus("?????????");
                    homeFragment1.askState();
                    tv_connect.setText(getResources().getString(R.string.contected));
                } else {
                    testBeanList.clear();

                    Prefer.getInstance().setBleStatus("?????????");
                    tv_connect.setText(getResources().getString(R.string.no_contect));
                }

                ll_center.startAnimation(slide_left_to_left_in);
                ll_center.setVisibility(View.VISIBLE);
                ll_right.startAnimation(slide_left_to_right);
                ll_right.setVisibility(View.GONE);
                curActivity = "2";
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * ????????????Activity
     */
    private void restartAct() {
//        finish();
        Intent _Intent = new Intent(this, MainActivity.class);
        _Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        _Intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(_Intent);
        //??????Activity????????????????????????
//        overridePendingTransition(0, 0);
    }
}
