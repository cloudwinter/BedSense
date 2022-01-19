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

public class HaoHuaBanActivity extends BaseActivity implements View.OnClickListener, ActionBarClickListener {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;

    //中间的
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

    //右边的
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

    //左边显示主界面
    @BindView(R.id.ll_left)
    RelativeLayout ll_left;
    //中间设置界面
    @BindView(R.id.ll_center)
    LinearLayout ll_center;
    //右边链接界面
    @BindView(R.id.ll_right)
    RelativeLayout ll_right;

    //公用的
    private String curActivity = "1";
    private boolean isClick = false;

    private TextView text1, text2, text3, text4;
    private List<TextView> textViews = new ArrayList<>();
    private NoScrollViewPager vpMain;
    private int textColor;

    private Handler mhandler = new Handler();
    private WaitDialog waitDialog;

    //设置界面copy过来的  中间的
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int BLE_STATUS = 222;

    //右边的
    //蓝牙4.0的UUID,其中0000ffe1-0000-1000-8000-00805f9b34fb是广州汇承信息科技有限公司08蓝牙模块的UUID
    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    //蓝牙特征值
    private static BluetoothGattCharacteristic target_chara = null;
    //蓝牙service,负责后台的蓝牙服务
    private static BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    // 蓝牙适配器
    BluetoothAdapter mBluetoothAdapter;
    // 蓝牙信号强度
    private ArrayList<Integer> rssis;
    // 自定义Adapter
    LeDeviceListAdapter mleDeviceListAdapter;
    // 描述扫描蓝牙的状态
    private boolean mScanning;
    private boolean scan_flag;
    // 蓝牙扫描时间
    private static final long SCAN_PERIOD = 5000;

    private String currentDecice = Prefer.getInstance().getCurrentDecice();

    private JumpingBeans mJumpingBeans;
    private List<TestBean> testBeanList = new ArrayList<>();
    private CountDownTimerUtils mCountDownTimerUtils;
    private Intent gattServiceIntent;
    private SharedPreferences mySharedPreferences;

    private SharedPreferences.Editor edit;

    //蓝牙连接状态
    private String mConnected = "0";
    private String status = "disconnected";
    private TestBean testBean;
    private String rev_str;
    private BluetoothAdapter blueadapter;
    private Animation slide_left_to_left;
    private Animation slide_right_to_left;
    private Animation slide_left_to_right;
    private Animation slide_left_to_left_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shu_shi_ban);
        ButterKnife.bind(this);
        actionbar.setData("豪华版", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        // 检查设备上是否支持蓝牙
        blueadapter = BluetoothAdapter.getDefaultAdapter();
        if (blueadapter == null) {
            showDialogv7("该手机暂不支持蓝牙模式", "", "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            return;
        }

        //初始化本地保存工具
        mySharedPreferences = getSharedPreferences("scenelist", Context.MODE_PRIVATE);
        edit = mySharedPreferences.edit();

        // Initializes Bluetooth adapter.
        // 获取手机本地的蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        /* 启动蓝牙service */
        gattServiceIntent = new Intent(HaoHuaBanActivity.this, BluetoothLeService.class);


        initView();
        initData();
    }

    private void initView() {
        waitDialog = new WaitDialog(HaoHuaBanActivity.this, "连接中...");

        slide_left_to_left = AnimationUtils.loadAnimation(HaoHuaBanActivity.this, R.anim.slide_left_to_left);
        slide_right_to_left = AnimationUtils.loadAnimation(HaoHuaBanActivity.this, R.anim.slide_right_to_left);
        slide_left_to_right = AnimationUtils.loadAnimation(HaoHuaBanActivity.this, R.anim.slide_left_to_right);
        slide_left_to_left_in = AnimationUtils.loadAnimation(HaoHuaBanActivity.this, R.anim.slide_left_to_left_in);

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

        tv_version.setText(SystemUtils.getVersionName(HaoHuaBanActivity.this));
        ll_connect.setOnClickListener(this);
        ll_qr_code.setOnClickListener(this);
        ll_language.setOnClickListener(this);
        ll_explain.setOnClickListener(this);
        sw_on_off.setOnClickListener(this);

        String liststr = mySharedPreferences.getString("LIST", "");
        try {
            testBeanList = MyApplication.String2SceneList(liststr);
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //进行保存获取。
        LogUtils.e("==本地保存的已经存在的集合长度==", "" + testBeanList.size());


        //右边链接界面copy来的
        // Initializes Bluetooth adapter.
        // 获取手机本地的蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        scan_flag = true;
        tv_try.setOnClickListener(this);
        ll_search.setOnClickListener(this);

        // 自定义适配器
        mleDeviceListAdapter = new LeDeviceListAdapter();
        // 为listview指定适配器
        lv.setAdapter(mleDeviceListAdapter);

		/* listview点击函数 */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, final int position, long id) {
                testBean = (TestBean) mleDeviceListAdapter.getItem(position);
                if (testBeanList.get(position).isConnected()) {
                    showDialogv7("是否断开连接", "取消", "断开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (int j = 0; j < testBeanList.size(); j++) {
                                testBeanList.get(j).setConnected(false);
                            }

                            mBluetoothLeService.disconnect();

                            Prefer.getInstance().setCurrentDecice("");
                            mleDeviceListAdapter.notifyDataSetChanged();
                            Prefer.getInstance().setBleStatus("未连接");
                            tv_connect.setText("未连接");


                            ll_center.startAnimation(slide_left_to_left_in);
                            ll_center.setVisibility(View.VISIBLE);
                            ll_right.startAnimation(slide_left_to_right);
                            ll_right.setVisibility(View.GONE);
                        }
                    });
                } else {
                    //未连接  --  检查下是否存在已连接的（存在则提示）
                    if (!isCheckAllChild()) {
                        ToastUtils.showToast(HaoHuaBanActivity.this, getResources().getString(R.string.bluetoothretry));
                    } else {
                        if (mScanning) {
                            /* 停止扫描设备 */
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            mScanning = false;
                        }

                        waitDialog.setCanceledOnTouchOutside(false);
                        waitDialog.show();
                        currentDecice = testBeanList.get(position).getAddress();
                        LogUtils.e("==点击item==", "进来了" + currentDecice);

                        mBluetoothLeService.disconnect();

                        mBluetoothLeService.connect(currentDecice);
                        Prefer.getInstance().setCurrentDecice(currentDecice);
                    }
                }
            }
        });
    }

    //切换颜色背景
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
            //连接设备
            case R.id.ll_connect:
                if (!blueadapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    return;
                }

                actionbar.setData("扫描连接设备", R.mipmap.ic_default_return, null, 0, null, this);

                if (tv_connect.getText().equals("未连接")) {
                    testBeanList.clear();
                    mleDeviceListAdapter.notifyDataSetChanged();
                }

                ll_center.startAnimation(slide_left_to_left);
                ll_center.setVisibility(View.GONE);
                ll_right.startAnimation(slide_right_to_left);
                ll_right.setVisibility(View.VISIBLE);

                curActivity = "3";
                break;
            //二维码
            case R.id.ll_qr_code:
                startActivity(new Intent(HaoHuaBanActivity.this, QrCodeActivity.class));
                break;
            //是否震动
            case R.id.sw_on_off:
                sw_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            ToastUtils.showToast(HaoHuaBanActivity.this, getResources().getString(R.string.opens));
                        } else {
                            ToastUtils.showToast(HaoHuaBanActivity.this, getResources().getString(R.string.close));
                        }
                    }
                });
                break;
            //切换语言
            case R.id.ll_language:
                startActivity(new Intent(HaoHuaBanActivity.this, LanguageActivity.class));
                break;
            //使用说明
            case R.id.ll_explain:
                startActivity(new Intent(HaoHuaBanActivity.this, ExplainActivity.class));
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
                    tv_try.setText("扫描附近蓝牙设备");
                }
            default:
                break;
        }
    }

    public void initData() {
        List<Fragment> listFragent = new ArrayList<>();

        HHKuaiJieFragment homeFragment1 = HHKuaiJieFragment.newInstance("1");
        listFragent.add(homeFragment1);
        HHWeiTiaoFragment homeFragment2 = HHWeiTiaoFragment.newInstance("2");
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
            finish();
        } else if (curActivity.equals("2")) {
            curActivity = "1";
            actionbar.setData("豪华版", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);

            ll_left.startAnimation(slide_left_to_left_in);
            ll_left.setVisibility(View.VISIBLE);
            ll_center.startAnimation(slide_left_to_right);
            ll_center.setVisibility(View.GONE);
        } else if (curActivity.equals("3")) {
            curActivity = "2";
            actionbar.setData("设置", R.mipmap.ic_default_return, null, 0, null, this);

            LogUtils.e("== 是否有选择的==", "" + isCheckAllChild());
            if (!isCheckAllChild()) {
                try {
                    //把搜索的蓝牙集合转为String， 保存本地
                    String liststr = MyApplication.getInstance().SceneList2String(testBeanList);
                    edit.putString("LIST", liststr);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Prefer.getInstance().setCurrentDecice(currentDecice);
                Prefer.getInstance().setBleStatus("已连接");
                tv_connect.setText("已连接");
            } else {
                testBeanList.clear();

                Prefer.getInstance().setBleStatus("未连接");
                tv_connect.setText("未连接");
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
            actionbar.setData("设置", R.mipmap.ic_default_return, null, 0, null, this);

            ll_left.startAnimation(slide_left_to_left);
            ll_left.setVisibility(View.GONE);
            ll_center.startAnimation(slide_right_to_left);
            ll_center.setVisibility(View.VISIBLE);
            curActivity = "2";
        }
    }

    //内部类 适配器
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
        //解除广播接收器
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
        mBluetoothLeService.disconnect();
        mBluetoothLeService = null;
    }

    // Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
    @Override
    protected void onResume() {
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        if (Prefer.getInstance().getCurrentDecice().equals("")) {
            LogUtils.e("==onResume 2 连接==", " = 进来了 ");
            Prefer.getInstance().setBleStatus("未连接");
            tv_connect.setText("未连接");
            showDialogv7("当前未连接相关蓝牙设备", "", "设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //去到设置界面动画
                    ll_left.startAnimation(slide_left_to_left);
                    ll_left.setVisibility(View.GONE);
                    ll_center.startAnimation(slide_right_to_left);
                    ll_center.setVisibility(View.VISIBLE);
                }
            });
        }

        isClick = false;
        //绑定广播接收器
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            startActivity(new Intent(HaoHuaBanActivity.this, OpenBleActivity.class));
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
     * @param enable (扫描使能，true:扫描开始,false:扫描停止)
     * @return void
     * @throws
     * @Title: scanLeDevice
     * @Description: TODO(扫描蓝牙设备)
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scan_flag = true;
                    tv_try.setText("扫描附近设备");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    LogUtils.e("==5s后自动停止扫描==", "stoping................");
                }
            }, SCAN_PERIOD);
            /* 开始扫描蓝牙设备，带mLeScanCallback 回调函数 */
            LogUtils.e("==开始扫描蓝牙设备==", "begin.....................");
            mScanning = true;
            scan_flag = false;
            tv_try.setText("扫描中");
            mJumpingBeans = JumpingBeans.with(tv_try).appendJumpingDots().build();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            LogUtils.e("==停止扫描蓝牙设备==", "stoping................");
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scan_flag = true;
            tv_try.setText("扫描附近设备");
        }
    }

    /**
     * 蓝牙扫描回调函数 实现扫描蓝牙设备，回调蓝牙BluetoothDevice，可以获取name MAC等信息
     **/
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            // TODO Auto-generated method stub

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.e("==扫描的蓝牙集合==", "" + device.getAddress() + "===" + device.getName());
                    /* 將扫描到设备的信息输出到listview的适配器 */
                    mleDeviceListAdapter.addDevice(device, rssi);
                    mleDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    /**
     * @author 广州汇承信息科技有限公司
     * @Description: TODO<自定义适配器Adapter,作为listview的适配器>
     * @data: 2014-10-12 上午10:46:30
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
         * 重写getview
         **/
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            // General ListView optimization code.
            // 加载listview每一项的视图
            view = mInflator.inflate(R.layout.item_connect_layout, null);
            // 初始化三个textview显示蓝牙信息
            // TextView deviceAddress = (TextView) view.findViewById(R.id.tv_deviceAddr);
            // TextView rssi = (TextView) view.findViewById(R.id.tv_rssi);
            TextView deviceName = (TextView) view.findViewById(R.id.tv_title);
            TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);

            String device = testBeanList.get(i).getTitle().replace(":","A").replace(";","B").replace("<","C")
                    .replace("=","D").replace(">","E").replace("?","F");
            deviceName.setText(device);
            if (testBeanList.get(i).isConnected()) {
                tvStatus.setText("断开连接");
            } else {
                tvStatus.setText("点击连接");
            }
            //  deviceAddress.setText(device.getAddress());
            // rssi.setText("" + rssis.get(i));
            return view;
        }
    }

    /* BluetoothLeService绑定的回调函数 */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                LogUtils.e("找不到蓝牙", "Unable to initialize Bluetooth");
                finish();
            }

            if (!TextUtils.isEmpty(Prefer.getInstance().getCurrentDecice())) {
                // 根据蓝牙地址，连接设备
                LogUtils.e("==根据蓝牙地址，连接设备==", "开始连接目标设备");
                if (isHasBle()) {

                    //连接之前的先断开一下，防止连接其他蓝牙时，长时间或不能连接
                    mBluetoothLeService.disconnect();

                    mBluetoothLeService.connect(Prefer.getInstance().getCurrentDecice());
                    //启动连接动画
                    waitDialog.setCanceledOnTouchOutside(false);
                    waitDialog.show();

                    isClick = false;
                } else {
                    Prefer.getInstance().setBleStatus("未连接");
                    showDialogv7("当前未连接相关蓝牙设备", "", "设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //去到设置界面动画
                            ll_left.startAnimation(slide_left_to_left);
                            ll_left.setVisibility(View.GONE);
                            ll_center.startAnimation(slide_right_to_left);
                            ll_center.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    //存在本地的蓝牙设备地址是否存在扫描的蓝牙中
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
     * 广播接收器，负责接收BluetoothLeService类发送的数据
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            waitDialog.dismiss();
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))//Gatt连接成功
            {
                Prefer.getInstance().setCurrentDecice(currentDecice);
                // mConnected = true;
                status = "connected";
                //更新连接状态
                updateConnectionState(status);
                LogUtils.e("==更新连接状态 已连接==", status);

                tv_connect.setText("已连接");
                ToastUtils.showToast(HaoHuaBanActivity.this, getResources().getString(R.string.linked));
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //Gatt连接失败
                status = "disconnected";
                //更新连接状态
                updateConnectionState(status);
                LogUtils.e("==更新连接状态 连接失败==", status);

                Prefer.getInstance().setBleStatus("未连接");
                tv_connect.setText("未连接");
                if (!curActivity.equals("3") && curActivity.equals("1")) {
                    showDialogv7("蓝牙连接失败", "", "设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mBluetoothLeService.disconnect();
                            //去到设置界面动画
                            ll_left.startAnimation(slide_left_to_left);
                            ll_left.setVisibility(View.GONE);
                            ll_center.startAnimation(slide_right_to_left);
                            ll_center.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    ToastUtils.showToast(HaoHuaBanActivity.this, getResources().getString(R.string.unlink));
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //发现GATT服务器
                // Show all the supported services and characteristics on the
                // user interface.
                // Log.e("==获取设备的所有蓝牙服务==", "" + MyApplication.getInstance().supportedGattServices.size());
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action))//有效数据
            {
                //处理发送过来的数据
                // displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
                // Log.e("==处理发送过来的数据==", "" + intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    /* 更新连接状态 */
    private void updateConnectionState(String status) {
        Message msg = new Message();
        msg.what = 1;
        Bundle b = new Bundle();
        b.putString("connect_state", status);
        msg.setData(b);
        //将连接状态更新的UI的textview上
        myHandler2.sendMessage(msg);
    }

    /* 意图过滤器 */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private Handler myHandler2 = new Handler() {
        // 2.重写消息处理函数
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 判断发送的消息
                case 1: {
                    if (!isClick) {
                        if (testBean != null) {
                            // 更新View
                            String state = msg.getData().getString("connect_state");
                            if (state.equals("connected")) {
                                testBean.setConnected(true);
                                Prefer.getInstance().setBleStatus("已连接");
                                tv_connect.setText("已连接");
                            } else {
                                testBean.setConnected(false);
                                Prefer.getInstance().setBleStatus("未连接");
                                tv_connect.setText("未连接");
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
     * @param @param rev_string(接受的数据)
     * @return void
     * @throws
     * @Title: displayData
     * @Description: TODO(接收到的数据在scrollview上显示)
     */
    private void displayData(String rev_string) {
        rev_str += rev_string;
        LogUtils.e("==接收设备返回的数据==", rev_string);
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: displayGattServices
     * @Description: TODO(处理蓝牙服务)
     */
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = "unknown_service";
        String unknownCharaString = "unknown_characteristic";
        // 服务数据,可扩展下拉列表的第一级数据
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        // 特征数据（隶属于某一级服务下面的特征值集合）
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
        // 部分层次，所有特征值集合
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            // 获取服务列表
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            // 查表，根据该uuid获取对应的服务名称。SampleGattAttributes这个表需要自定义。
            gattServiceData.add(currentServiceData);
            // LogUtils.e("=获取服务列表中 Service Uuid==", "" + uuid);
            System.out.println("Service uuid:" + uuid);
            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
            // 从当前循环所指向的服务中读取特征值列表
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();
            // Loops through available Characteristics.
            // 对于当前循环所指向的服务中的每一个特征值
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);

                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                if (gattCharacteristic.getUuid().toString().equals(HEART_RATE_MEASUREMENT)) {
                    // 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
                    mhandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mBluetoothLeService.readCharacteristic(gattCharacteristic);
                        }
                    }, 200);

                    LogUtils.e("========================获取特征值=================", "==========");
                    MyApplication.getInstance().mBluetoothLeService = mBluetoothLeService;
                    MyApplication.getInstance().gattCharacteristic = gattCharacteristic;

                    LogUtils.e("==蓝牙特征值==", "" + MyApplication.getInstance().gattCharacteristic);

                    // 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                    target_chara = gattCharacteristic;

                    // 设置数据内容
                    // 往蓝牙模块写入数据
                     mBluetoothLeService.writeCharacteristic(gattCharacteristic);
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    System.out.println("---descriptor UUID:" + descriptor.getUuid());
                    // 获取特征值的描述
                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
                    // true);
                }
                gattCharacteristicGroupData.add(currentCharaData);
            }
            // 按先后顺序，分层次放入特征值集合中，只有特征值
            mGattCharacteristics.add(charas);
            // 构件第二级扩展列表（服务下面的特征值）
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (curActivity.equals("1")) {
                moveTaskToBack(true);
            } else if (curActivity.equals("2")) {
                actionbar.setData("豪华版", R.mipmap.ic_default_return, null, R.mipmap.ic_default_set, null, this);

                ll_left.startAnimation(slide_left_to_left_in);
                ll_left.setVisibility(View.VISIBLE);
                ll_center.startAnimation(slide_left_to_right);
                ll_center.setVisibility(View.GONE);
                curActivity = "1";
            } else if (curActivity.equals("3")) {

                actionbar.setData("设置", R.mipmap.ic_default_return, null, 0, null, this);

                LogUtils.e("== 是否有选择的==", "" + isCheckAllChild());
                if (!isCheckAllChild()) {
                    try {
                        //把搜索的蓝牙集合转为String， 保存本地
                        String liststr = MyApplication.getInstance().SceneList2String(testBeanList);
                        edit.putString("LIST", liststr);
                        edit.commit();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Prefer.getInstance().setCurrentDecice(currentDecice);
                    Prefer.getInstance().setBleStatus("已连接");
                    tv_connect.setText("已连接");
                } else {
                    testBeanList.clear();

                    Prefer.getInstance().setBleStatus("未连接");
                    tv_connect.setText("未连接");
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
}
