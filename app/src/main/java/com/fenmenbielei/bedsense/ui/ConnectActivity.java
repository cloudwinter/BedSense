//package com.fenmenbielei.bedsense.ui;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattDescriptor;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.BluetoothManager;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.ServiceConnection;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.support.v7.widget.RecyclerView;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//
//import com.fenmenbielei.bedsense.MyApplication;
//import com.fenmenbielei.bedsense.base.BaseActivity;
//import com.fenmenbielei.bedsense.dialog.WaitDialog;
//import com.fenmenbielei.bedsense.impl.ActionBarClickListener;
//import com.fenmenbielei.bedsense.javabean.TestBean;
//import com.fenmenbielei.bedsense.service.BluetoothLeService;
//import com.fenmenbielei.bedsense.uitls.BroadCastReceiverUtil;
//import com.fenmenbielei.bedsense.uitls.CountDownTimerUtils;
//import com.fenmenbielei.bedsense.uitls.LogUtils;
//import com.fenmenbielei.bedsense.uitls.Prefer;
//import com.fenmenbielei.bedsense.uitls.ToastUtils;
//import com.fenmenbielei.bedsense.view.TranslucentActionBar;
//import com.wnhz.shidaodianqi.R;
//
//
//import net.frakbot.jumpingbeans.JumpingBeans;
//
//import java.io.IOException;
//import java.io.StreamCorruptedException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//
///**
// * 废弃 待删除
// */
//@Deprecated
//public class ConnectActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {
//    @BindView(R.id.actionbar)
//    TranslucentActionBar actionbar;
//    @BindView(R.id.recycler)
//    RecyclerView recycler;
//    @BindView(R.id.ll_search)
//    LinearLayout ll_search;
//    @BindView(R.id.lv)
//    ListView lv;
//    @BindView(R.id.tv_try)
//    TextView tv_try;
//    @BindView(R.id.tv_connect_time)
//    TextView tv_connect_time;
//
//    //蓝牙4.0的UUID,其中0000ffe1-0000-1000-8000-00805f9b34fb是广州汇承信息科技有限公司08蓝牙模块的UUID
//    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
//    //蓝牙特征值
//    private static BluetoothGattCharacteristic target_chara = null;
//    //蓝牙service,负责后台的蓝牙服务
//    private static BluetoothLeService mBluetoothLeService;
//    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
//    // 蓝牙适配器
//    BluetoothAdapter mBluetoothAdapter;
//    // 蓝牙信号强度
//    private ArrayList<Integer> rssis;
//    // 自定义Adapter
//    LeDeviceListAdapter mleDeviceListAdapter;
//    // 描述扫描蓝牙的状态
//    private boolean mScanning;
//    private boolean scan_flag;
//    private Handler mHandler;
//    // 蓝牙扫描时间
//    private static final long SCAN_PERIOD = 5000;
//
//    private String currentDecice;
//
//    private JumpingBeans mJumpingBeans;
//    private List<TestBean> testBeanList = new ArrayList<>();
//    private CountDownTimerUtils mCountDownTimerUtils;
//    private Intent gattServiceIntent;
//    private SharedPreferences mySharedPreferences;
//
//    private SharedPreferences.Editor edit;
//
//    //蓝牙连接状态
//    private String mConnected = "0";
//    private String status = "disconnected";
//    private TestBean testBean;
//
//    private Handler mhandler = new Handler();
//    private Handler myHandler2 = new Handler() {
//        // 2.重写消息处理函数
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                // 判断发送的消息
//                case 1: {
//                    // 更新View
//                    String state = msg.getData().getString("connect_state");
//                    if (state.equals("connected")) {
//                        testBean.setConnected(true);
//                    } else {
//                        testBean.setConnected(false);
//                    }
//                    mleDeviceListAdapter.notifyDataSetChanged();
//                    break;
//                }
//            }
//            super.handleMessage(msg);
//        }
//    };
//    private WaitDialog waitDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_connect);
//        ButterKnife.bind(this);
//        mySharedPreferences = getSharedPreferences("scenelist", Context.MODE_PRIVATE);
//        edit = mySharedPreferences.edit();
//        mConnected = getIntent().getStringExtra("isConnected");
//        LogUtils.e("==连接状态==", "mConnected ==" + mConnected);
//        actionbar.setData("附近蓝牙设备", R.mipmap.ic_default_return, null, 0, null, this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            actionbar.setStatusBarHeight(getStatusBarHeight());
//        }
//
//        waitDialog = new WaitDialog(ConnectActivity.this, "连接中...");
////        initView();
//    }
//
//    private void initView() {
//        if (mConnected.equals("1")) {
//            String liststr = mySharedPreferences.getString("LIST", "");
//            try {
//                testBeanList = MyApplication.String2SceneList(liststr);
//            } catch (StreamCorruptedException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//
//            //进行保存获取。
//            LogUtils.e("==已经存在的集合长度==", "" + testBeanList.size());
//        }
//
//        // Initializes Bluetooth adapter.
//        // 获取手机本地的蓝牙适配器
//        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        mBluetoothAdapter = bluetoothManager.getAdapter();
//
//        mHandler = new Handler();
//        scan_flag = true;
//        tv_try.setOnClickListener(this);
//        ll_search.setOnClickListener(this);
//
//        // 自定义适配器
//        mleDeviceListAdapter = new LeDeviceListAdapter();
//        // 为listview指定适配器
//        lv.setAdapter(mleDeviceListAdapter);
//
//		/* listview点击函数 */
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
//                // TODO Auto-generated method stub
//                // final BluetoothDevice device = mleDeviceListAdapter.getDevice(position);
//                //                if (device == null)
//                // return;
//
//                testBean = (TestBean) mleDeviceListAdapter.getItem(position);
//                if (testBeanList.get(position).isConnected()) {
//                    showDialogv7("是否断开连接", "取消", "断开", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            testBeanList.clear();
//                            edit.clear();
//                            MyApplication.getInstance().mBluetoothLeService.disconnect();
//                            ToastUtils.showToast(ConnectActivity.this, "已断开连接");
//                            Prefer.getInstance().setCurrentDecice("");
//                            BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
//                            Intent intent = new Intent();
//                            intent.putExtra("status", "未连接");
//                            setResult(RESULT_OK, intent);
//                            finish();
//                        }
//                    });
//                } else {
//                    //未连接  --  检查下是否存在已连接的（存在则提示）
//                    if (!isCheckAllChild()) {
//                        ToastUtils.showToast(ConnectActivity.this, "当前已有设备连接,请断开后重试");
//                    } else {
//                        /* 启动蓝牙service */
//                        gattServiceIntent = new Intent(ConnectActivity.this, BluetoothLeService.class);
//                        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
//                        currentDecice = testBeanList.get(position).getAddress();
//
//                        LogUtils.e("==点击item==", "进来了" + currentDecice);
//                        if (mScanning) {
//                            /* 停止扫描设备 */
//                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                            mScanning = false;
//                        }
//                    }
//                }
//
//            }
//        });
//    }
//
//    public boolean isCheckAllChild() {
//        for (int j = 0; j < testBeanList.size(); j++) {
//            TestBean testBean = (TestBean) testBeanList.get(j);
//            if (testBean.isConnected()) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * @param enable (扫描使能，true:扫描开始,false:扫描停止)
//     * @return void
//     * @throws
//     * @Title: scanLeDevice
//     * @Description: TODO(扫描蓝牙设备)
//     */
//    private void scanLeDevice(final boolean enable) {
//        if (enable) {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScanning = false;
//                    scan_flag = true;
//                    tv_try.setText("扫描附近设备");
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    LogUtils.e("==5s后自动停止扫描==", "stoping................");
//                }
//            }, SCAN_PERIOD);
//            /* 开始扫描蓝牙设备，带mLeScanCallback 回调函数 */
//            LogUtils.e("==开始扫描蓝牙设备==", "begin.....................");
//            mScanning = true;
//            scan_flag = false;
//            tv_try.setText("扫描中");
//            mJumpingBeans = JumpingBeans.with(tv_try).appendJumpingDots().build();
//            mBluetoothAdapter.startLeScan(mLeScanCallback);
//        } else {
//            LogUtils.e("==停止扫描蓝牙设备==", "stoping................");
//            mScanning = false;
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            scan_flag = true;
//            tv_try.setText("扫描附近设备");
//        }
//    }
//
//    /**
//     * 蓝牙扫描回调函数 实现扫描蓝牙设备，回调蓝牙BluetoothDevice，可以获取name MAC等信息
//     **/
//    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
//
//        @Override
//        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
//            // TODO Auto-generated method stub
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    /* 將扫描到设备的信息输出到listview的适配器 */
//                    mleDeviceListAdapter.addDevice(device, rssi);
//                    mleDeviceListAdapter.notifyDataSetChanged();
//                }
//            });
//        }
//    };
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_try:
//                testBeanList.clear();
//                Prefer.getInstance().setCurrentDecice("");
//                if (scan_flag) {
//                    mCountDownTimerUtils = new CountDownTimerUtils(tv_connect_time, 5000, 1000);
//                    mCountDownTimerUtils.start();
//
//                    mleDeviceListAdapter = new LeDeviceListAdapter();
//                    lv.setAdapter(mleDeviceListAdapter);
//                    scanLeDevice(true);
//                } else {
//                    scanLeDevice(false);
//                    tv_try.setText("扫描附近设备");
//                }
//                break;
//        }
//    }
//
//    /**
//     * @author 广州汇承信息科技有限公司
//     * @Description: TODO<自定义适配器Adapter,作为listview的适配器>
//     * @data: 2014-10-12 上午10:46:30
//     * @version: V1.0
//     */
//    private class LeDeviceListAdapter extends BaseAdapter {
//        private ArrayList<BluetoothDevice> mLeDevices;
//
//        private LayoutInflater mInflator;
//
//        public LeDeviceListAdapter() {
//            super();
//            rssis = new ArrayList<Integer>();
//            mLeDevices = new ArrayList<BluetoothDevice>();
//            mInflator = getLayoutInflater();
//        }
//
//        public void addDevice(BluetoothDevice device, int rssi) {
//            if (!mLeDevices.contains(device) && device.getName() != null) {
//                mLeDevices.add(device);
//                TestBean testBean = new TestBean();
//                testBean.setConnected(false);
//                testBean.setTitle(device.getName());
//                testBean.setAddress(device.getAddress());
//                testBeanList.add(testBean);
//                rssis.add(rssi);
//            }
//        }
//
//        public BluetoothDevice getDevice(int position) {
//            return mLeDevices.get(position);
//        }
//
//        public void clear() {
//            mLeDevices.clear();
//            testBeanList.clear();
//            rssis.clear();
//        }
//
//        @Override
//        public int getCount() {
//            // return mLeDevices.size();
//            return testBeanList.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            // return mLeDevices.get(i);
//            return testBeanList.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        /**
//         * 重写getview
//         **/
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            // General ListView optimization code.
//            // 加载listview每一项的视图
//            view = mInflator.inflate(R.layout.item_connect_layout, null);
//            // 初始化三个textview显示蓝牙信息
//            // TextView deviceAddress = (TextView) view.findViewById(R.id.tv_deviceAddr);
//            // TextView rssi = (TextView) view.findViewById(R.id.tv_rssi);
//            TextView deviceName = (TextView) view.findViewById(R.id.tv_title);
//            TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
//
//            String device = testBeanList.get(i).getTitle();
//            deviceName.setText(device);
//            if (testBeanList.get(i).isConnected()) {
//                tvStatus.setText("断开连接");
//            } else {
//                tvStatus.setText("点击连接");
//            }
//            //  deviceAddress.setText(device.getAddress());
//            // rssi.setText("" + rssis.get(i));
//            return view;
//        }
//    }
//
//    /* BluetoothLeService绑定的回调函数 */
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
//            MyApplication.getInstance().mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
//            if (!mBluetoothLeService.initialize()) {
//                LogUtils.e("找不到蓝牙", "Unable to initialize Bluetooth");
//                finish();
//            }
//            // Automatically connects to the device upon successful start-up
//            // initialization.
//            // 根据蓝牙地址，连接设备
//            LogUtils.e("==根据蓝牙地址，连接设备==", "开始连接目标设备");
//            //启动连接动画
//            waitDialog.setCanceledOnTouchOutside(false);
//            waitDialog.show();
//            mBluetoothLeService.disconnect();
//            mBluetoothLeService.connect(currentDecice);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//        }
//    };
//
//    /**
//     * 广播接收器，负责接收BluetoothLeService类发送的数据
//     */
//    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))//Gatt连接成功
//            {
//                Prefer.getInstance().setCurrentDecice(currentDecice);
//                waitDialog.dismiss();
//                // mConnected = true;
//                status = "connected";
//                //更新连接状态
//                updateConnectionState(status);
//                LogUtils.e("==更新连接状态 已连接==", status);
//                BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
//
//            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //Gatt连接失败
//                // mConnected = false;
//                Prefer.getInstance().setCurrentDecice("");
//                waitDialog.dismiss();
//                status = "disconnected";
//                //更新连接状态
//                updateConnectionState(status);
//                LogUtils.e("==更新连接状态 连接失败==", status);
//                if (!Prefer.getInstance().getCurrentDecice().equals("")) {
//                    showDialogv7("连接失败,请尝试重新连接", "", "朕知道了", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    });
//                }
//                BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
//
//            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //发现GATT服务器
//                // Show all the supported services and characteristics on the
//                // user interface.
//                //获取设备的所有蓝牙服务
////                MyApplication.getInstance().supportedGattServices = mBluetoothLeService.getSupportedGattServices();
//                // Log.e("==获取设备的所有蓝牙服务==", "" + MyApplication.getInstance().supportedGattServices.size());
////                displayGattServices(mBluetoothLeService.getSupportedGattServices());
//
//            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action))//有效数据
//            {
//                //处理发送过来的数据
//                // displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
//                // Log.e("==处理发送过来的数据==", "" + intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
//            }
//        }
//    };
//
//    /* 更新连接状态 */
//    private void updateConnectionState(String status) {
//        Message msg = new Message();
//        msg.what = 1;
//        Bundle b = new Bundle();
//        b.putString("connect_state", status);
//        msg.setData(b);
//        //将连接状态更新的UI的textview上
//        myHandler2.sendMessage(msg);
//    }
//
//    /* 意图过滤器 */
//    private static IntentFilter makeGattUpdateIntentFilter() {
//        final IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
//        return intentFilter;
//    }
//
//
//    /**
//     * @param @param rev_string(接受的数据)
//     * @return void
//     * @throws
//     * @Title: displayData
//     * @Description: TODO(接收到的数据在scrollview上显示)
//     */
//    private void displayData(String rev_string) {
////        rev_str += rev_string;
////        runOnUiThread(new Runnable() {
////            @Override
////            public void run() {
////                rev_tv.setText(rev_str);
////                rev_sv.scrollTo(0, rev_tv.getMeasuredHeight());
////                System.out.println("rev:" + rev_str);
////            }
////        });
////        LogUtils.e("==接收设备返回的数据==", rev_string);
//    }
//
//    /**
//     * @param
//     * @return void
//     * @throws
//     * @Title: displayGattServices
//     * @Description: TODO(处理蓝牙服务)
//     */
//    private void displayGattServices(List<BluetoothGattService> gattServices) {
//        if (gattServices == null)
//            return;
//        String uuid = null;
//        String unknownServiceString = "unknown_service";
//        String unknownCharaString = "unknown_characteristic";
//        // 服务数据,可扩展下拉列表的第一级数据
//        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
//        // 特征数据（隶属于某一级服务下面的特征值集合）
//        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
//        // 部分层次，所有特征值集合
//        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
//        // Loops through available GATT Services.
//        for (BluetoothGattService gattService : gattServices) {
//            // 获取服务列表
//            HashMap<String, String> currentServiceData = new HashMap<String, String>();
//            uuid = gattService.getUuid().toString();
//            // 查表，根据该uuid获取对应的服务名称。SampleGattAttributes这个表需要自定义。
//            gattServiceData.add(currentServiceData);
//            // LogUtils.e("=获取服务列表中 Service Uuid==", "" + uuid);
//            System.out.println("Service uuid:" + uuid);
//            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
//            // 从当前循环所指向的服务中读取特征值列表
//            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
//            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();
//            // Loops through available Characteristics.
//            // 对于当前循环所指向的服务中的每一个特征值
//            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
//                charas.add(gattCharacteristic);
//
//                HashMap<String, String> currentCharaData = new HashMap<String, String>();
//                uuid = gattCharacteristic.getUuid().toString();
//                if (gattCharacteristic.getUuid().toString().equals(HEART_RATE_MEASUREMENT)) {
//                    // 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
//                    mhandler.postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            mBluetoothLeService.readCharacteristic(gattCharacteristic);
//                        }
//                    }, 200);
//                    // 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
//                    mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
//                    target_chara = gattCharacteristic;
//                    MyApplication.getInstance().gattCharacteristic = gattCharacteristic;
//                    LogUtils.e("==蓝牙特征值==", "" + MyApplication.getInstance().gattCharacteristic);
//                    // 设置数据内容
//                    // 往蓝牙模块写入数据
//                    // mBluetoothLeService.writeCharacteristic(gattCharacteristic);
//                }
//                List<BluetoothGattDescriptor> descriptors = gattCharacteristic.getDescriptors();
//                for (BluetoothGattDescriptor descriptor : descriptors) {
//                    System.out.println("---descriptor UUID:" + descriptor.getUuid());
//                    // 获取特征值的描述
//                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
//                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
//                    // true);
//                }
//                gattCharacteristicGroupData.add(currentCharaData);
//            }
//            // 按先后顺序，分层次放入特征值集合中，只有特征值
//            mGattCharacteristics.add(charas);
//            // 构件第二级扩展列表（服务下面的特征值）
//            gattCharacteristicData.add(gattCharacteristicGroupData);
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            Intent intent = new Intent();
//            LogUtils.e("== 是否有选择的==", "" + isCheckAllChild());
//            if (!isCheckAllChild()) {
//                LogUtils.e("== testBeanList 的长度==", "" + testBeanList.size());
//                try {
//                    String liststr = MyApplication.getInstance().SceneList2String(testBeanList);
//                    edit.putString("LIST", liststr);
//                    edit.commit();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                intent.putExtra("status", "已连接");
//                Prefer.getInstance().setBleStatus("已连接");
//                Prefer.getInstance().setCurrentDecice(currentDecice);
//                BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
//            } else {
//                testBeanList.clear();
//                intent.putExtra("status", "未连接");
//                Prefer.getInstance().setBleStatus("未连接");
//                Prefer.getInstance().setCurrentDecice("");
//                BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
//            }
//            setResult(RESULT_OK, intent);
//            finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public void onLeftClick() {
//        Intent intent = new Intent();
//        LogUtils.e("== 是否有选择的==", "" + isCheckAllChild());
//        if (!isCheckAllChild()) {
//            LogUtils.e("== testBeanList 的长度==", "" + testBeanList.size());
//            try {
//                String liststr = MyApplication.getInstance().SceneList2String(testBeanList);
//                edit.putString("LIST", liststr);
//                edit.commit();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            intent.putExtra("status", "已连接");
//            Prefer.getInstance().setBleStatus("已连接");
//            Prefer.getInstance().setCurrentDecice(currentDecice);
//            BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
//        } else {
//            testBeanList.clear();
//            intent.putExtra("status", "未连接");
//            Prefer.getInstance().setBleStatus("未连接");
//            Prefer.getInstance().setCurrentDecice("");
//            BroadCastReceiverUtil.sendBroadcast(ConnectActivity.this, "CONNECTSTATUS");
//        }
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//
//    @Override
//    public void onRightClick() {
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //解除广播接收器
//        unregisterReceiver(mGattUpdateReceiver);
//        mBluetoothLeService = null;
//    }
//
//    // Activity出来时候，绑定广播接收器，监听蓝牙连接服务传过来的事件
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //绑定广播接收器
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//        if (mBluetoothLeService != null) {
//            //根据蓝牙地址，建立连接
//            final boolean result = mBluetoothLeService.connect(currentDecice);
//            LogUtils.e("绑定广播接收器", "Connect request result=" + result);
//        }
//    }
//}
