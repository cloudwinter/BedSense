package com.fenmenbielei.bedsense.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.fenmenbielei.bedsense.base.BaseActivity;
import com.fenmenbielei.bedsense.impl.ActionBarClickListener;
import com.fenmenbielei.bedsense.ui.LanguageActivity;
import com.fenmenbielei.bedsense.ui.QrCodeActivity;
import com.fenmenbielei.bedsense.uitls.BlueUtils;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.fenmenbielei.bedsense.uitls.SystemUtils;
import com.fenmenbielei.bedsense.uitls.ToastUtils;
import com.fenmenbielei.bedsense.view.TranslucentActionBar;
import com.wnhz.shidaodianqi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity implements TranslucentActionBar.ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
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
    @BindView(R.id.ll_privacy)
    LinearLayout ll_privacy;
    @BindView(R.id.ll_version)
    LinearLayout ll_version;
    @BindView(R.id.tv_version)
    TextView tv_version;



    @Override
    protected void onResume() {
        super.onResume();
        if (BlueUtils.isConnected()) {
            tv_connect.setText(R.string.connected);
        } else {
            tv_connect.setText(R.string.no_contect_right);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        actionbar.setData(getString(R.string.setting_title), R.mipmap.ic_default_return, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        initView();
    }

    private void initView() {
        //设置连接状态
        tv_connect.setText(Prefer.getInstance().getBleStatus());

        tv_version.setText(SystemUtils.getVersionName(SettingActivity.this));
        ll_connect.setOnClickListener(this);
        ll_qr_code.setOnClickListener(this);
        ll_language.setOnClickListener(this);
        ll_privacy.setOnClickListener(this);
        sw_on_off.setOnClickListener(this);
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //连接设备
            case R.id.ll_connect:
                Intent intent = new Intent(SettingActivity.this, ConnectActivity.class);
                intent.putExtra("from","set");
                startActivity(intent);
                break;
            //二维码
            case R.id.ll_qr_code:
                startActivity(new Intent(SettingActivity.this, QrCodeActivity.class));
                break;
            //是否震动
            case R.id.sw_on_off:
                sw_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            ToastUtils.showToast(SettingActivity.this, "打开");
                        } else {
                            ToastUtils.showToast(SettingActivity.this, "关闭");
                        }
                    }
                });
                break;
            //切换语言
            case R.id.ll_language:
                startActivity(new Intent(SettingActivity.this, LanguageActivity.class));
                break;
            //使用说明
            case R.id.ll_privacy:
                startActivity(new Intent(SettingActivity.this, WebActivity.class));
                break;
        }
    }


}
