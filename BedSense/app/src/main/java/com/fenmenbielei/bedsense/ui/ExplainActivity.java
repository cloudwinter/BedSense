package com.fenmenbielei.bedsense.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.fenmenbielei.bedsense.base.BaseActivity;
import com.fenmenbielei.bedsense.impl.ActionBarClickListener;
import com.fenmenbielei.bedsense.view.TranslucentActionBar;
import com.wnhz.shidaodianqi.R;



import butterknife.BindView;
import butterknife.ButterKnife;

public class ExplainActivity extends BaseActivity implements ActionBarClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);

        ButterKnife.bind(this);
        actionbar.setData("使用说明", R.mipmap.ic_default_return, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }
}
