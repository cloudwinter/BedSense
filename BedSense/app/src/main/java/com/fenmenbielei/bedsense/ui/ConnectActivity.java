package com.fenmenbielei.bedsense.ui;

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
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.fenmenbielei.bedsense.MyApplication;
import com.fenmenbielei.bedsense.base.BaseActivity;
import com.fenmenbielei.bedsense.dialog.WaitDialog;
import com.fenmenbielei.bedsense.impl.ActionBarClickListener;
import com.fenmenbielei.bedsense.javabean.TestBean;
import com.fenmenbielei.bedsense.service.BluetoothLeService;
import com.fenmenbielei.bedsense.uitls.BroadCastReceiverUtil;
import com.fenmenbielei.bedsense.uitls.CountDownTimerUtils;
import com.fenmenbielei.bedsense.uitls.LogUtils;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.fenmenbielei.bedsense.uitls.ToastUtils;
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

public class ConnectActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
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
    private Handler mHandler;
    // ??????????????????
    private static final long SCAN_PERIOD = 5000;

    private String currentDecice;

    private JumpingBeans mJumpingBeans;
    private List<TestBean> testBeanList = new ArrayList<>();
    private CountDownTimerUtils mCountDownTimerUtils;
    private Intent gattServiceIntent;
    private SharedPreferences mySharedPreferences;

    private SharedPreferences.Editor edit;

    //??????????????????
    private String mConnected = "0";
    private String status = "disconnected";
    private TestBean testBean;

    private Handler mhandler = new Handler();
    private Handler myHandler2 = new Handler() {
        // 2.????????????????????????
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // ?????????????????????
                case 1: {
                    // ??????View
                    String state = msg.getData().getString("connect_state");
                    if (state.equals("connected")) {
                        testBean.setConnected(true);
                    } else {
                        testBean.setConnected(false);
                    }
                    mleDeviceListAdapter.notifyDataSetChanged();
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };
    private WaitDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ButterKnife.bind(this);
        mySharedPreferences = getSharedPreferences("scenelist", Context.MODE_PRIVATE);
        edit = mySharedPreferences.edit();
        mConnected = getIntent().getStringExtra("isConnected");
        LogUtils.e("==????????????==", "mConnected ==" + mConnected);
        actionbar.setData("??????????????????", R.mipmap.ic_default_return, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        waitDialog = new WaitDialog(ConnectActivity.this, "?????????...");
//        initView();
    }

    private void initView() {
        if (mConnected.equals("1")) {
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

            //?????????????????????
            LogUtils.e("==???????????????????????????==", "" + testBeanList.size());
        }

        // Initializes Bluetooth adapter.
        // ????????????????????????????????????
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        mHandler = new Handler();
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
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                // TODO Auto-generated method stub
                // final BluetoothDevice device = mleDeviceListAdapter.getDevice(position);
                //                if (device == null)
                // return;

                testBean = (TestBean) mleDeviceListAdapter.getItem(position);
                if (testBeanList.get(position).isConnected()) {
                    showDialogv7("??????????????????", "??????", "??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            testBeanList.clear();
                            edit.clear();
                            MyApplication.getInstance().mBluetoothLeService.disconnect();
                            ToastUtils.showToast(ConnectActivity.this, "???????????????");
                            Prefer.getInstance().setCurrentDecice("");
                            BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
                            Intent intent = new Intent();
                            intent.putExtra("status", "?????????");
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                } else {
                    //?????????  --  ??????????????????????????????????????????????????????
                    if (!isCheckAllChild()) {
                        ToastUtils.showToast(ConnectActivity.this, "????????????????????????,??????????????????");
                    } else {
                        /* ????????????service */
                        gattServiceIntent = new Intent(ConnectActivity.this, BluetoothLeService.class);
                        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                        currentDecice = testBeanList.get(position).getAddress();

                        LogUtils.e("==??????item==", "?????????" + currentDecice);
                        if (mScanning) {
                            /* ?????????????????? */
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            mScanning = false;
                        }
                    }
                }

            }
        });
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
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scan_flag = true;
                    tv_try.setText("??????????????????");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    LogUtils.e("==5s?????????????????????==", "stoping................");
                }
            }, SCAN_PERIOD);
            /* ??????????????????????????????mLeScanCallback ???????????? */
            LogUtils.e("==????????????????????????==", "begin.....................");
            mScanning = true;
            scan_flag = false;
            tv_try.setText("?????????");
            mJumpingBeans = JumpingBeans.with(tv_try).appendJumpingDots().build();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            LogUtils.e("==????????????????????????==", "stoping................");
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scan_flag = true;
            tv_try.setText("??????????????????");
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
                    /* ????????????????????????????????????listview???????????? */
                    mleDeviceListAdapter.addDevice(device, rssi);
                    mleDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_try:
                testBeanList.clear();
                Prefer.getInstance().setCurrentDecice("");
                if (scan_flag) {
                    mCountDownTimerUtils = new CountDownTimerUtils(tv_connect_time, 5000, 1000);
                    mCountDownTimerUtils.start();

                    mleDeviceListAdapter = new LeDeviceListAdapter();
                    lv.setAdapter(mleDeviceListAdapter);
                    scanLeDevice(true);
                } else {
                    scanLeDevice(false);
                    tv_try.setText("??????????????????");
                }
                break;
        }
    }

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

            String device = testBeanList.get(i).getTitle();
            deviceName.setText(device);
            if (testBeanList.get(i).isConnected()) {
                tvStatus.setText("????????????");
            } else {
                tvStatus.setText("????????????");
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
            MyApplication.getInstance().mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                LogUtils.e("???????????????", "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
            // ?????????????????????????????????
            LogUtils.e("==?????????????????????????????????==", "????????????????????????");
            //??????????????????
            waitDialog.setCanceledOnTouchOutside(false);
            waitDialog.show();
            mBluetoothLeService.disconnect();
            mBluetoothLeService.connect(currentDecice);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    /**
     * ??????????????????????????????BluetoothLeService??????????????????
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))//Gatt????????????
            {
                Prefer.getInstance().setCurrentDecice(currentDecice);
                waitDialog.dismiss();
                // mConnected = true;
                status = "connected";
                //??????????????????
                updateConnectionState(status);
                LogUtils.e("==?????????????????? ?????????==", status);
                BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //Gatt????????????
                // mConnected = false;
                Prefer.getInstance().setCurrentDecice("");
                waitDialog.dismiss();
                status = "disconnected";
                //??????????????????
                updateConnectionState(status);
                LogUtils.e("==?????????????????? ????????????==", status);
                if (!Prefer.getInstance().getCurrentDecice().equals("")) {
                    showDialogv7("????????????,?????????????????????", "", "????????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                }
                BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //??????GATT?????????
                // Show all the supported services and characteristics on the
                // user interface.
                //?????????????????????????????????
//                MyApplication.getInstance().supportedGattServices = mBluetoothLeService.getSupportedGattServices();
                // Log.e("==?????????????????????????????????==", "" + MyApplication.getInstance().supportedGattServices.size());
//                displayGattServices(mBluetoothLeService.getSupportedGattServices());

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


    /**
     * @param @param rev_string(???????????????)
     * @return void
     * @throws
     * @Title: displayData
     * @Description: TODO(?????????????????????scrollview?????????)
     */
    private void displayData(String rev_string) {
//        rev_str += rev_string;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                rev_tv.setText(rev_str);
//                rev_sv.scrollTo(0, rev_tv.getMeasuredHeight());
//                System.out.println("rev:" + rev_str);
//            }
//        });
//        LogUtils.e("==???????????????????????????==", rev_string);
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
                    // ??????Characteristic???????????????,???????????????????????????????????????mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                    target_chara = gattCharacteristic;
                    MyApplication.getInstance().gattCharacteristic = gattCharacteristic;
                    LogUtils.e("==???????????????==", "" + MyApplication.getInstance().gattCharacteristic);
                    // ??????????????????
                    // ???????????????????????????
                    // mBluetoothLeService.writeCharacteristic(gattCharacteristic);
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
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            LogUtils.e("== ??????????????????==", "" + isCheckAllChild());
            if (!isCheckAllChild()) {
                LogUtils.e("== testBeanList ?????????==", "" + testBeanList.size());
                try {
                    String liststr = MyApplication.getInstance().SceneList2String(testBeanList);
                    edit.putString("LIST", liststr);
                    edit.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                intent.putExtra("status", "?????????");
                Prefer.getInstance().setBleStatus("?????????");
                Prefer.getInstance().setCurrentDecice(currentDecice);
                BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
            } else {
                testBeanList.clear();
                intent.putExtra("status", "?????????");
                Prefer.getInstance().setBleStatus("?????????");
                Prefer.getInstance().setCurrentDecice("");
                BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
            }
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLeftClick() {
        Intent intent = new Intent();
        LogUtils.e("== ??????????????????==", "" + isCheckAllChild());
        if (!isCheckAllChild()) {
            LogUtils.e("== testBeanList ?????????==", "" + testBeanList.size());
            try {
                String liststr = MyApplication.getInstance().SceneList2String(testBeanList);
                edit.putString("LIST", liststr);
                edit.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.putExtra("status", "?????????");
            Prefer.getInstance().setBleStatus("?????????");
            Prefer.getInstance().setCurrentDecice(currentDecice);
            BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
        } else {
            testBeanList.clear();
            intent.putExtra("status", "?????????");
            Prefer.getInstance().setBleStatus("?????????");
            Prefer.getInstance().setCurrentDecice("");
            BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //?????????????????????
        unregisterReceiver(mGattUpdateReceiver);
        mBluetoothLeService = null;
    }

    // Activity?????????????????????????????????????????????????????????????????????????????????
    @Override
    protected void onResume() {
        super.onResume();
        //?????????????????????
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            //?????????????????????????????????
            final boolean result = mBluetoothLeService.connect(currentDecice);
            LogUtils.e("?????????????????????", "Connect request result=" + result);
        }
    }
}
