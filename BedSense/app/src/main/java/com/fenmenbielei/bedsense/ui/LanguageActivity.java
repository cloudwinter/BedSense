package com.fenmenbielei.bedsense.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.fenmenbielei.bedsense.MainActivity;
import com.fenmenbielei.bedsense.base.BaseActivity;
import com.fenmenbielei.bedsense.impl.ActionBarClickListener;
import com.fenmenbielei.bedsense.uitls.LocaleUtils;
import com.fenmenbielei.bedsense.uitls.Prefer;
import com.fenmenbielei.bedsense.view.TranslucentActionBar;
import com.wnhz.shidaodianqi.R;


import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguageActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.ll_chinese)
    LinearLayout ll_chinese;
    @BindView(R.id.ll_english)
    LinearLayout ll_english;
    @BindView(R.id.ic_chinese)
    ImageView ic_chinese;
    @BindView(R.id.ic_english)
    ImageView ic_english;
    private int tagType = 1;//1 中文 2 英文

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        actionbar.setData("选择语言", R.mipmap.ic_default_return, null, 0, "完成", this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        initView();
    }

    private void initView() {

        if (!TextUtils.isEmpty(Prefer.getInstance().isTypeLan())) {
            if (Prefer.getInstance().isTypeLan().equals("1")) {
                ic_chinese.setVisibility(View.VISIBLE);
                ic_english.setVisibility(View.GONE);
                tagType = 1;
            } else {
                ic_chinese.setVisibility(View.GONE);
                ic_english.setVisibility(View.VISIBLE);
                tagType = 2;
            }

        }
        ll_chinese.setOnClickListener(this);
        ll_english.setOnClickListener(this);

    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        //点击保存
        Prefer.getInstance().setTypeLan(tagType + "");
        switchLan(tagType);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_chinese:
                ic_chinese.setVisibility(View.VISIBLE);
                ic_english.setVisibility(View.GONE);
                tagType = 1;
                break;
            case R.id.ll_english:
                ic_chinese.setVisibility(View.GONE);
                ic_english.setVisibility(View.VISIBLE);
                tagType = 2;
                break;
        }
    }

    private void switchLan(int type) {
        if (type == 1) {
            if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_CHINESE)) {
                LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_CHINESE);
                restartAct();
            }
        } else {
            if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_ENGLISH)) {
                LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_ENGLISH);
                restartAct();
            }
        }
    }

    /**
     * 重启当前Activity
     */
    private void restartAct() {
//        finish();
        Intent _Intent = new Intent(this, MainActivity.class);
        _Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        _Intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(_Intent);
        //清除Activity退出和进入的动画
//        overridePendingTransition(0, 0);
    }
}
